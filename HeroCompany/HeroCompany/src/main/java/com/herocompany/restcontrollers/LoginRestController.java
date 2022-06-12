package com.herocompany.restcontrollers;

import com.herocompany.entities.Login;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginRestController {
    final UserDetailService userDetailService;

    public LoginRestController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity login (@Valid @RequestBody Login login){
        return  userDetailService.login(login);
    }

}
