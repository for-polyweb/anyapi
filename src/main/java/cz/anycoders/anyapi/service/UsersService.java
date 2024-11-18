package cz.anycoders.anyapi.service;

import cz.anycoders.anyapi.entity.APIError;
import cz.anycoders.anyapi.entity.PostDetail;
import cz.anycoders.anyapi.repository.ErrorsRepo;
import cz.anycoders.anyapi.repository.UsersRepo;
import cz.anycoders.anyapi.types.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {
    private final UsersRepo usersRepo;
    private final ErrorsRepo errorsRepo;

    private APIError getError(int code) {
        Optional<APIError> optionalIdea = errorsRepo.findById(code);
        return optionalIdea.orElse(null);
    }

    public ApiResponse getCount() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("success");
        apiResponse.setData(usersRepo.count());
        return apiResponse;
    }


}
