package cz.anycoders.anyapi.controller;

import cz.anycoders.anyapi.types.api.ApiResponse;
import cz.anycoders.anyapi.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final UsersService usersService;
    private final View error;

    /**
     * This method is called when a GET request is made
     * URL: localhost:8080/api/v1/posts/
     * Purpose: Fetches all the posts in the posts table
     * @return List of Post
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getAll(){
        return ResponseEntity.ok().body(usersService.getCount());
    }




}
