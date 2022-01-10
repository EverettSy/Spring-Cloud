package com.syraven.cloud.utlis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName: JwtUtil
 * @Description: JwtUtil 工具类
 * @Author syrobin
 * @Date 2021-12-06 3:39 PM
 * @Version V1.0
 */
@Data
@EnableConfigurationProperties(JwtUtil.class)
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    // 设置私钥，不能告诉别人
    private String key;
    // 设置过期时间
    private long ttl;

    // 生成 Jwt (加密)
    public String createJwt(String id, String name, Map<String, Object> map) {
        long now = System.currentTimeMillis();
        long exp = now + ttl;
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(id)
                .setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                ;
        /*jwtBuilder.setId("1.0"); // 编号/版本
        jwtBuilder.setIssuer("ISSUER"); // 发行人
        jwtBuilder.setSubject("SUBJECT"); // 主题
        jwtBuilder.setAudience("AUDIENCE"); // 受众
        jwtBuilder.setIssuedAt(new Date(nowMillis)); // 签发时间
        jwtBuilder.setNotBefore(new Date(nowMillis)); // 生效时间
        jwtBuilder.setExpiration(new Date(nowMillis + (60 * 60 * 1000))); // 失效时间*/


        // jwtBuilder.setClaims(map);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jwtBuilder.claim(entry.getKey(), entry.getValue());
        }
        if (ttl > 0) {
            jwtBuilder.setExpiration(new Date(exp));
        }
        // 获取token字符串
        String token = jwtBuilder.compact();
        return token;
    }

    // 解析 Jwt (解密)
    public Claims parseJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
