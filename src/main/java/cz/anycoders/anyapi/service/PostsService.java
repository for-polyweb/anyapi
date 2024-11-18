package cz.anycoders.anyapi.service;

import cz.anycoders.anyapi.entity.*;
import cz.anycoders.anyapi.repository.*;

import cz.anycoders.anyapi.types.api.comments.CommentAdd;
import cz.anycoders.anyapi.types.api.posts.PostRequest;
import cz.anycoders.anyapi.types.api.ApiResponse;
import cz.anycoders.anyapi.types.api.posts.PostUpdateRequest;
import cz.anycoders.anyapi.types.api.rating.RateUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {
    private final PostsRepo postsRepo;
    private final PostDetailRepo postDetailRepo;
    private final StatesRepo statesRepo;
    private final UsersRepo usersRepo;
    private final TypesRepo typesRepo;
    private final ErrorsRepo errorsRepo;
    private final RatingRepo ratingRepo;
    private final CommentsRepo commentsRepo;

    private int testAddRequest(PostRequest request) {
        int error = 0;
        if (
                request.getSecretKey() == null || request.getSecretKey().isEmpty()
                        || request.getTitle() == null || request.getTitle().isBlank()
                        || request.getDescription() == null || request.getDescription().isBlank()
                        || request.getUserId() == null
                        || request.getStateId() == null
                        || request.getTypeId() == null
                        || request.getPros() == null
                        || request.getCons() == null

        ) {
            error = 1;
        } else {
            if (!testSecretKey(request.getUserId(), request.getSecretKey())) {
                error = 503;
            }
        }
        return error;
    }

    private boolean postExists(Long id) {
        if (id != null) {
            return postsRepo.findById(id).isPresent();
        }
        return false;
    }

    private Boolean userExists(Long userId) {
        if (userId != null) {
            Optional<User> optionalUser = usersRepo.findById(userId);
            return optionalUser.isPresent();
        }
        return false;
    }

    private boolean isUserPost(long userId, Long postId) {
        Optional<PostsList> oPost = postsRepo.findById(postId);
        if (userExists(userId)) {
            if (oPost.isPresent()) {
                PostsList postsList = oPost.get();
                return postsList.getAuthor().getId() == userId;
            }
        }
        return false;
    }

    private boolean testSecretKey(long userId, String secretKey) {
        boolean result = false;
        if (userExists(userId)) {
            result = "alkasklglfkgjlkjsdgk".equals(secretKey);
            //v budoucnu se bude divat do DB. Zatim se pouziva provizorni klic.
        }
        return result;
    }

    private boolean testUserPermission(Long postId, Long userId, String secretKey) {
        return (testSecretKey(userId, secretKey) && isUserPost(userId, postId));
    }

    private boolean testUpdateRequest(PostUpdateRequest request) {
        return !(
                request.getSecretKey() == null || request.getSecretKey().isEmpty()
                        || request.getTitle() == null || request.getTitle().isBlank()
                        || request.getDescription() == null || request.getDescription().isBlank()
                        || request.getUserId() == null
                        || request.getStateId() == null
                        || request.getPros() == null
                        || request.getCons() == null
        );
    }

    private boolean testRateRequest(RateUpdate request) {
        return !(request.getSecretKey() == null || request.getSecretKey().isEmpty()
                || request.getUserId() == null
                || request.getValue() == null
        );
    }

    private boolean testAddCommentRequest(CommentAdd request) {
        return !(request.getSecretKey() == null || request.getSecretKey().isEmpty()
                || request.getUserId() == null
                || request.getText() == null || request.getText().isBlank()
        );
    }


    public ApiResponse findAllOrdered() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("success");
        apiResponse.setData(postsRepo.findAllOrdered());
        return apiResponse;
    }

    public ApiResponse getById(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Optional<PostDetail> optionalIdea = postDetailRepo.findById(id);
        if (optionalIdea.isPresent()) {
            apiResponse.setResult("success");
            apiResponse.setData(optionalIdea.get());
        } else {
            apiResponse.setResult("error");
            apiResponse.setError(errorsRepo.findById(404).orElse(null));
        }

        return apiResponse;
    }

    public ApiResponse add(PostRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        int error = testAddRequest(request);
        if (error != 0) {
            apiResponse.setResult("error");
            apiResponse.setError(errorsRepo.findById(error).orElse(null));
        } else {
            PostDetail postDetail = new PostDetail();
            Optional<State> stateOpt = statesRepo.findById(request.getStateId());
            Optional<User> userOpt = usersRepo.findById(request.getUserId());
            Optional<Type> typeOpt = typesRepo.findById(request.getTypeId());
            if (stateOpt.isEmpty()) error = 3;
            else if (userOpt.isEmpty()) error = 4;
            else if (typeOpt.isEmpty()) error = 5;
            if (error == 0) {
                postDetail.setTitle(request.getTitle());
                postDetail.setDescription(request.getDescription());
                postDetail.setAuthor(userOpt.get());
                postDetail.setState(stateOpt.get());
                postDetail.setPros(request.getPros());
                postDetail.setCons(request.getCons());
                postDetail.setType(typeOpt.get());
                LocalDateTime now = LocalDateTime.now();
                postDetail.setCreated(now);
                postDetail.setUpdated(now);
                apiResponse.setData(postDetailRepo.save(postDetail));
                apiResponse.setResult("success");
            } else {
                apiResponse.setResult("error");
                apiResponse.setError(errorsRepo.findById(3).orElse(null));
            }

        }
        return apiResponse;
    }

    public ApiResponse update(Long id, PostUpdateRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        if (testUpdateRequest(request)) {
            if (postExists(id)) {
                if (testUserPermission(id, request.getUserId(), request.getSecretKey())) {
                    int error = 0;
                    Optional<State> oState = statesRepo.findById(request.getStateId());
                    if (oState.isEmpty()) error = 3;
                    if (error == 0) {
                        PostDetail post = postDetailRepo.findById(id).get();
                        post.setTitle(request.getTitle());
                        post.setDescription(request.getDescription());
                        post.setState(oState.get());
                        post.setPros(request.getPros());
                        post.setCons(request.getCons());
                        post.setUpdated(LocalDateTime.now());
                        apiResponse.setData(postDetailRepo.save(post));
                        apiResponse.setResult("success");
                    } else {
                        apiResponse.setResult("error");
                        apiResponse.setError(errorsRepo.findById(error).orElse(null));
                    }
                } else {
                    apiResponse.setResult("error");
                    apiResponse.setError(errorsRepo.findById(503).orElse(null));
                }
            } else {
                apiResponse.setResult("error");
                apiResponse.setError(errorsRepo.findById(404).orElse(null));
            }
        } else {
            apiResponse.setResult("error");
            apiResponse.setError(errorsRepo.findById(1).orElse(null));
        }
        return apiResponse;
    }

    public ApiResponse rate(Long id, RateUpdate request) {
        ApiResponse apiResponse = new ApiResponse();
        if (testRateRequest(request)) {
            if (request.getValue() < 1 || request.getValue() > 10) {
                apiResponse.setResult("error");
                apiResponse.setData(errorsRepo.findById(7).orElse(null));
            } else {
                if (testSecretKey(request.getUserId(), request.getSecretKey())) {
                    if (postExists(id)) {
                        PostDetail post = postDetailRepo.findById(id).get();
                        List<Rate> rating = post.getRating();
                        boolean userRateExists = false;
                        for (Rate pRate : rating) {
                            if (Objects.equals(pRate.getUser().getId(), request.getUserId())) {
                                userRateExists = true;
                                pRate.setValue(request.getValue());
                                pRate.setText(request.getText());
                            }
                        }

                        if (!userRateExists) {
                            Rate rate = new Rate();
                            rate.setPostId(id);
                            User user = usersRepo.findById(request.getUserId()).get();
                            rate.setUser(user);
                            rate.setValue(request.getValue());
                            rate.setText(request.getText());
                            rate.setTimestamp(LocalDateTime.now());
                            rating.add(rate);
                        }

                        post.setRating(rating);
                        postDetailRepo.save(post);
                        apiResponse.setResult("success");
                    } else {
                        apiResponse.setResult("error");
                        apiResponse.setError(errorsRepo.findById(404).orElse(null));
                    }
                } else {
                    apiResponse.setResult("error");
                    apiResponse.setError(errorsRepo.findById(503).orElse(null));
                }
            }
        } else {
            apiResponse.setResult("error");
            apiResponse.setData(errorsRepo.findById(6).orElse(null));
        }
        return apiResponse;
    }

    public ApiResponse deleteById(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        if (id == null) {
            apiResponse.setError(errorsRepo.findById(2).orElse(null));
        } else {
            Optional<PostDetail> optionalIdea = postDetailRepo.findById(id);
            if (optionalIdea.isPresent()) {
                postDetailRepo.deleteById(id);
                apiResponse.setResult("success");
            } else {
                apiResponse.setResult("error");
                apiResponse.setError(errorsRepo.findById(404).orElse(null));
            }
        }
        return apiResponse;
    }

    public ApiResponse addComment(Long id, CommentAdd body) {
        ApiResponse apiResponse = new ApiResponse();
        if (testAddCommentRequest(body)) {
            if (testSecretKey(body.getUserId(), body.getSecretKey())) {
                if (postExists(id)) {
                    Comment comment = new Comment();
                    comment.setPostId(id);
                    comment.setText(body.getText());
                    comment.setAuthor(usersRepo.findById(body.getUserId()).get());
                    comment.setTimestamp(LocalDateTime.now());
                    commentsRepo.save(comment);
                    apiResponse.setResult("success");
                }else {
                    apiResponse.setResult("error");
                    apiResponse.setError(errorsRepo.findById(404).orElse(null));
                }
            }else {
                apiResponse.setResult("error");
                apiResponse.setError(errorsRepo.findById(503).orElse(null));
            }
        }else{
            apiResponse.setResult("error");
            apiResponse.setData(errorsRepo.findById(6).orElse(null));
        }
        return apiResponse;
    }
}
