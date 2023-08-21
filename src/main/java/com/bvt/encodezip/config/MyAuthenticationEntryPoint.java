package com.bvt.encodezip.config;

import com.bvt.encodezip.util.JacksonUtils;
import com.bvt.encodezip.vo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.config
 * @className: MyAuthenticationEntryPoint
 * @author: Tom
 * @description: TODO
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Result result = Result.create(403, "请登录");
        out.write(JacksonUtils.writeValueAsString(result));
        out.flush();
        out.close();
    }
}
