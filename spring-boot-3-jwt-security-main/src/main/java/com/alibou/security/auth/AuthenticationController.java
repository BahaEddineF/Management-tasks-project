package com.alibou.security.auth;

import com.alibou.security.emailSender.VerificationRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<ResponseEntity<Object>> register(
      @RequestBody RegisterRequest request
  ) throws MessagingException {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<Object> authenticate(
          @RequestBody AuthenticationRequest request
  ) throws MessagingException {
    return service.authenticate(request);
  }

  @PostMapping("/verify")
  public ResponseEntity<AuthenticationResponse> verifyCode(
          @RequestBody VerificationRequest request
  ) {
    return ResponseEntity.ok(service.verifyCode(request.getEmail(), request.getCode()));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
   return service.refreshToken(request, response);

  }


}
