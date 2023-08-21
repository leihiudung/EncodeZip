package com.bvt.encodezip.config;

import com.bvt.encodezip.entity.User;
import com.bvt.encodezip.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.config
 * @className: LoginValidateAuthenticationProvider
 * @author: Tom
 * @description: TODO
 */
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserServiceImpl userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取输入的用户名
        String username = authentication.getName();
        //获取输入的明文
        String rawPassword = (String) authentication.getCredentials();
        //查询用户是否存在
        User user = (User) userService.loadUserByUsername(username);
        if (!user.isEnabled()) {
            throw new DisabledException("该账户已被禁用，请联系管理员");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期，请联系管理员");
        } else if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录");
        }
        //验证密码
        if (!(new BCryptPasswordEncoder()).matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("输入密码错误!");
        }
        return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());


    }

    @Override
    public boolean supports(Class<?> aClass) {
        //确保authentication能转成该类
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
