package com.joe.common_.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;


import java.util.Date;



public class jwtHelper {

    /**
     * 生成token  header.payload.singature
     */
    private static final String SING = "123456";
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    /**
     * 验证token  合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息方法
     */
    public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }
    public static Long getUserId(String token){
        try {
            if (token == null) return null;

            Claim userId = getTokenInfo(token).getClaim("userId");
            Integer anInt = userId.asInt();
            return anInt.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String  getUserName(String token){
        try {
            if (token == null) return null;

            Claim userName= getTokenInfo(token).getClaim("userName");
            String s = userName.asString();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String createJWT(Long userid , String username){
       return  JWT.create()
                .withClaim("userId", userid)  //payload
                .withClaim("userName", username)
                .withExpiresAt(new Date(System.currentTimeMillis()+ tokenExpiration)) // 指定令牌的过期时间
                .sign(Algorithm.HMAC256(SING));//签名
    }

    public static void main(String[] args) {
        String token = getUserName("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6Imxpc2kiLCJleHAiOjE3MTg5OTExNTMsInVzZXJJZCI6NH0.sFsYcRyBGuabtXZrRHXJQosW9hQa9BxKzEwTsIDchs4");
        System.out.println(token);

    }
}
