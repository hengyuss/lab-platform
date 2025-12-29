package com.hengyu.lab.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

  private  Collection<? extends GrantedAuthority> authorities;
  private String username;
  private String password;
  private  Long loginTime;
  private  Long expireTime;
  private  Long id;
  private  Integer identityType;
  private  Integer status;
  private String uniqueKey;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // 将 user 中的身份转为 Security 认识的角色
    //TODO 权限认证后边再写
    return this.authorities;
  }

  @JsonIgnore
  public Long getUserId(){
    return this.id;
  }

  @JsonIgnore
  public Integer getIdentityType(){
    return  this.identityType;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return this.password;
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return this.username;
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
