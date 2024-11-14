package com.alibou.security.auth;

import com.alibou.security.user.Role;
import com.alibou.security.user.subclasses.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
  private Title title;
  private String phone;
}
