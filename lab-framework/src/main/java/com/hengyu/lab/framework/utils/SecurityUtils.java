package com.hengyu.lab.framework.utils;

import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.security.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  /**
   * 获取用户
   **/
  public static AuthUser getAuthUser()
  {
    try
    {
      return (AuthUser) getAuthentication().getPrincipal();
    }
    catch (Exception e)
    {
      throw new BizException(ResultCode.UN_AUTHORIZED);
    }
  }

  /**
   * 获取Authentication
   */
  public static Authentication getAuthentication()
  {
    return SecurityContextHolder.getContext().getAuthentication();
  }


}
