package com.hengyu.lab.system.user.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser implements UserDetails {

  private  User user;
  private  Collection<? extends GrantedAuthority> authorities;
  private  Long loginTime;
  private  Long expireTime;
  private String uniqueKey;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // 将 user 中的身份转为 Security 认识的角色
    //TODO 权限认证后边再写
    return this.authorities;
  }

  @JsonIgnore
  public Long getUserId(){
    return user.getId();
  }

  @JsonIgnore
  public IdentityType getIdentityType(){
    return  user.getIdentityType();
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}
