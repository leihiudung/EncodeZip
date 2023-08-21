package com.bvt.encodezip.config.filter;

import com.bvt.encodezip.util.JacksonUtils;
import com.bvt.encodezip.util.JwtUtils;
import com.bvt.encodezip.vo.Result;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.config.filter
 * @className: JwtFilter
 * @author: Tom
 * @description: TODO
 */
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        //后台管理路径外的请求直接跳过
        if (!request.getRequestURI().startsWith(request.getContextPath() + "/admin")) {
            chain.doFilter(request, res);
            return;
        }
        String jwt = request.getHeader("Authorization");
        if (JwtUtils.judgeTokenIsExist(jwt)) {
            try {
                Claims claims = JwtUtils.getTokenBody(jwt);
                String username = claims.getSubject();
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("application/json;charset=utf-8");
                Result result = Result.create(403, "凭证已失效，请重新登录！");
                PrintWriter out = response.getWriter();
                out.write(JacksonUtils.writeValueAsString(result));
                out.flush();
                out.close();
                return;
            }
        }
        chain.doFilter(req, res);
    }


}
