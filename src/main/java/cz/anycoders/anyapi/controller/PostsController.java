package cz.anycoders.anyapi.controller;

import cz.anycoders.anyapi.types.api.comments.CommentAdd;
import cz.anycoders.anyapi.types.api.posts.PostRequest;
import cz.anycoders.anyapi.types.api.ApiResponse;
import cz.anycoders.anyapi.entity.Rate;
import cz.anycoders.anyapi.service.PostsService;
import cz.anycoders.anyapi.types.api.posts.PostUpdateRequest;
import cz.anycoders.anyapi.types.api.rating.RateUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostsController {

    private final PostsService postsService;
    private final View error;

    /**
     * This method is called when a GET request is made
     * URL: localhost:8080/api/v1/posts/
     * Purpose: Fetches all the posts in the posts table
     * @return List of Post
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(){

        return ResponseEntity.ok().body(postsService.findAllOrdered());
    }

    /**
     * This method is called when a GET request is made
     * URL: localhost:8080/api/v1/posts/1 (or any other id)
     * Purpose: Fetches post with the given id
     * @param id - post id
     * @return Post with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id)
    {
        return ResponseEntity.ok().body(postsService.getById(id));
    }

    /**
     * This method is called when a POST request is made
     * URL: localhost:8080/api/v1/posts/
     * Purpose: Save a Post entity
     * @param request - APIPostsRequest
     * @return Saved Post entity
     */
    @PostMapping("/")
    public ResponseEntity<Object> add(@RequestBody PostRequest request)
    {
        request.setTypeId(1L);
        return ResponseEntity.ok().body(postsService.add(request));
    }

    /**
     * This method is called when a PUT request is made
     * URL: localhost:8080/api/v1/posts/
     * Purpose: Update a Post entity
     * @param body - RAPIPostsRequest
     * @return Updated Post
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody PostUpdateRequest body)
    {
        return ResponseEntity.ok().body(postsService.update(id,body));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<ApiResponse> rate(@PathVariable Long id, @RequestBody RateUpdate body){
        return ResponseEntity.ok().body(postsService.rate(id,body));
    }
    @PostMapping("/{id}/comment")
    public ResponseEntity<ApiResponse> addComment(@PathVariable Long id, @RequestBody CommentAdd body){
        return ResponseEntity.ok().body(postsService.addComment(id,body));
    }

    /**
     * This method is called when a PUT request is made
     * URL: localhost:8080/pi/v1/posts/1 (or any other id)
     * Purpose: Delete a Post entity
     * @param id - post's id to be deleted
     * @return a String message indicating post record has been deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long id)
    {
        return ResponseEntity.ok().body(postsService.deleteById(id));
    }


}
