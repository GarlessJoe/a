package com.joe.security.filter;

import com.alibaba.fastjson.JSON;
import com.joe.common_.ResponseUtil.responseUtil;
import com.joe.common_.jwt.jwtHelper;
import com.joe.common_.result.Result;
import com.joe.common_.result.ResultCode;
import com.joe.vo.system.LoginVo;
import com.joe.security.custom.CustomUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.fasterxml.jackson.databind.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;
    public TokenLoginFilter(AuthenticationManager authenticationManager , RedisTemplate  redis) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        this.redisTemplate  = redis;
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            Authentication token = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUser user = (CustomUser)authResult.getPrincipal();
        String token = jwtHelper.createJWT(user.getSysUser().getId(), user.getSysUser().getUsername());
        redisTemplate.opsForValue().set(user.getUsername() ,JSON.toJSONString(user.getAuthorities()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("token" , token);
        responseUtil.out(response , Result.ok(map));
    }
    //登录失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if(e.getCause() instanceof RuntimeException) {
            responseUtil.out(response, Result.build(null, 204, e.getMessage()));
        } else {
            responseUtil.out(response, Result.build(null, ResultCode.LOGIN_MOBLE_ERROR));
        }
    }

}
