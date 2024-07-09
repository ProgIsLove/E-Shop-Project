package com.example.shopmebe.user;

import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.request.CheckUniqueEmailRequest;
import com.shopme.common.response.CheckUniqueResponse;
import com.shopme.common.response.CheckUniqueStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserRestController {

    private UserService userService;

    @PostMapping(value ="/users/check-email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckUniqueResponse> checkDuplicateEmail(@RequestBody CheckUniqueEmailRequest request) throws ConflictException {
        userService.isEmailUnique(request);


        return ResponseEntity.ok(new CheckUniqueResponse(CheckUniqueStatus.OK));
    }
}
