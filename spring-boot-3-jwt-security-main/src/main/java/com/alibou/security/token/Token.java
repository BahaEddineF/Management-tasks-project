package com.alibou.security.token;

import com.alibou.security.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "unique_token_constraint", columnNames = "token")
})
public class Token {

  @Id
  @GeneratedValue
  public Integer id;

  // Apply unique constraint with a custom name
  @Column(name = "token", nullable = false)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  // Name the foreign key constraint for the user_id field
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", foreignKey = @jakarta.persistence.ForeignKey(name = "fk_token_user"))
  public User user;
}
