package com.hengyu.lab.system.user.infrastructure.security;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@Builder
public class AuthUser implements UserDetails {

  private final User user;
  private final Collection<? extends GrantedAuthority> authorities;
  private  Long loginTime;
  private  Long expireTime;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // 将 user 中的身份转为 Security 认识的角色
    //TODO 权限认证后边再写
    return this.authorities;
  }

  public Long getUserId(){
    return user.getId();
  }

  public IdentityType getIdentityType(){
    return  user.getIdentityType();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
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
