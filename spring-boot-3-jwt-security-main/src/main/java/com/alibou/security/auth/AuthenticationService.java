package com.alibou.security.auth;

import com.alibou.security.config.JwtService;
import com.alibou.security.emailSender.EmailService;
import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import com.alibou.security.token.TokenType;
import com.alibou.security.user.Role;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import com.alibou.security.user.subclasses.admin.Admin;
import com.alibou.security.user.subclasses.employee.Employee;
import com.alibou.security.user.subclasses.manager.Manager;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailService emailService;


  public AuthenticationResponse verifyCode(String email, String code) {
    // Step 1: Find the user by email
    var user = repository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Step 2: Check if the verification code matches
    if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
      // Step 3: Mark the user as verified
      user.setVerified(true);
      user.setVerificationCode(null); // Clear the verification code
      repository.save(user);

      // Step 4: Generate JWT token after successful verification
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    } else {
      throw new IllegalArgumentException("Invalid verification code.");
    }
  }

  public ResponseEntity<Object> register(RegisterRequest request) throws MessagingException {
    User user;
    // Create an instance based on the role
    switch (request.getRole()) {
      case ADMIN:
        user = new Admin();
        user.setRole(Role.ADMIN);

        break;
      case MANAGER:
        user = new Manager();
        user.setRole(Role.MANAGER);

        break;
      case EMPLOYEE:
        user = new Employee();
        ((Employee) user).setTitle(request.getTitle());
        user.setRole(Role.EMPLOYEE);

        break;
      default:
        throw new IllegalArgumentException("Invalid role: " + request.getRole());
    }

    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setPhone(request.getPhone());
    // Save the user and generate tokens
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    saveUserToken(savedUser, jwtToken);

    return     this.emailService.sendWelcomeEmail(user.getEmail(),user.getFirstname(), user.getLastname(), request.getPassword());

  }


  public ResponseEntity<Object> authenticate(AuthenticationRequest request) throws MessagingException {
    // Step 1: Authenticate user with email and password
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    // Step 2: Find the user in the database
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

    // Step 3: Generate a verification code
    String verificationCode = generateVerificationCode();
    user.setVerificationCode(verificationCode);
    user.setVerified(false); // Set verified to false until the user verifies the code
    repository.save(user);

    // Step 4: Send the verification code to the user's email

    return emailService.sendVerificationEmail(user.getEmail(),verificationCode);

  }

  private String generateVerificationCode() {
    return String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit numeric code
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public ResponseEntity<?> refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or malformed.");
    }

    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail).orElseThrow();

      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // Return response entity with auth response and status OK
        return ResponseEntity.ok(authResponse);
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token.");
      }
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User email not found.");
  }
}
