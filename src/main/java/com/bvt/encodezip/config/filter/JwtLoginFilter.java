package com.bvt.encodezip.config.filter;

import com.bvt.encodezip.entity.User;
import com.bvt.encodezip.service.LoginLogService;
import com.bvt.encodezip.util.JacksonUtils;
import com.bvt.encodezip.util.JwtUtils;
import com.bvt.encodezip.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: JWT登录过滤器 (表单方式可以使用UsernamePasswordAuthenticationFilter ,非表单使用当前抽象类)
 * @Author: Tom
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    LoginLogService loginLogService;

    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, LoginLogService loginLogService) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
        this.loginLogService = loginLogService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        User user = JacksonUtils.readValue(httpServletRequest.getInputStream(), User.class);
        Authentication authentication = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String jwt = JwtUtils.generateToken(authResult.getName(), authResult.getAuthorities());
        response.setContentType("application/json;charset=utf-8");
        User user = (User) authResult.getPrincipal();
        user.setPassword(null);
        Map<String, Object> map = new HashMap<>(4);
        map.put("user", user);
        map.put("token", jwt);
        Result result = Result.ok("登录成功", map);
        PrintWriter out = response.getWriter();
        out.write(JacksonUtils.writeValueAsString(result));
        out.flush();
        out.close();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        String msg = failed.getMessage();
        //登录不成功时，会抛出对应的异常
        if (failed instanceof LockedException) {
            msg = "账号被锁定";
        } else if (failed instanceof CredentialsExpiredException) {
            msg = "密码过期";
        } else if (failed instanceof AccountExpiredException) {
            msg = "账号过期";
        } else if (failed instanceof DisabledException) {
            msg = "账号被禁用";
        } else if (failed instanceof BadCredentialsException) {
            msg = "用户名或密码错误";
        }
        PrintWriter out = response.getWriter();
        out.write(JacksonUtils.writeValueAsString(Result.create(401, msg)));
        out.flush();
        out.close();
    }
}
