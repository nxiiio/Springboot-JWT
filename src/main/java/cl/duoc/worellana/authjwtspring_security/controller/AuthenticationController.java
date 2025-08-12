package cl.duoc.worellana.authjwtspring_security.controller;

import cl.duoc.worellana.authjwtspring_security.dto.AuthLoginRequest;
import cl.duoc.worellana.authjwtspring_security.dto.AuthRegisterRequest;
import cl.duoc.worellana.authjwtspring_security.dto.AuthResponse;
import cl.duoc.worellana.authjwtspring_security.service.UserDetailsServiceImpl;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@PermitAll
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@RequestBody @Valid AuthRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest request){
        return ResponseEntity.ok(userDetailsService.loginUser(request));
    }

}
