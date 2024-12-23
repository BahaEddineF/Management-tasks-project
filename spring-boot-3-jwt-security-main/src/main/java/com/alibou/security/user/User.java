package com.alibou.security.user;

import com.alibou.security.token.Token;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @jakarta.persistence.UniqueConstraint(name = "unique_email_constraint", columnNames = "email")
        }
)@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false)
  @NotBlank
  private String password;

  @Column(nullable = false)
  @NotBlank
  private String firstname;

  @Column(nullable = false)
  @NotBlank
  private String lastname;

  @Column(nullable = false, unique = true)
  @NotBlank
  @Email
  private String email;

  @Column(nullable = false, unique = true)
  @NotBlank
  private String phone;

  // No need for insertable = false, updatable = false since it's managed by the discriminator column
  @Enumerated(EnumType.STRING)
  @Column(name = "user_type", insertable=false, updatable=false)
  private Role role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Token> tokens;

  private boolean isVerified;

  private String verificationCode;
  private String profileImage; // File name or path for the stored image



  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
