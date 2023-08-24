package cc.cybereflex.common.utils;

import cc.cybereflex.common.constants.JwtConstants;
import cc.cybereflex.common.model.UserTypeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.MapUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JwtUtil {

    /**
     * 生成 token
     *
     * @param uid      用户id
     * @param username 用户名
     * @param data     扩展数据
     * @return token
     */
    public static String generateToken(
            String uid,
            String username,
            String ip,
            UserTypeEnum userTypeEnum,
            Map<String, Object> data) {

        Map<String, Object> claims = new HashMap<>();

        if (MapUtils.isNotEmpty(data)) {
            claims.putAll(data);
        }

        if (Objects.equals(userTypeEnum, UserTypeEnum.DEVICE)) {
            claims.put(JwtConstants.JWT_IP, ip);
        }
        claims.put(JwtConstants.JWT_UID, uid);
        claims.put(JwtConstants.JWT_USERNAME, username);

        Date expireTime = DateTimeUtil.toDate(LocalDateTime.now().plusHours(8));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.ES256, JwtConstants.JWT_SECRET)
                .setExpiration(expireTime)
                .compact();
    }

    /**
     * 解析 token
     *
     * @param token token
     * @return claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 获取用户ID
     *
     * @param token token
     * @return 用户ID
     */
    public static String parseUID(String token) {
        return parseToken(token).get(JwtConstants.JWT_UID, String.class);
    }


    /**
     * 获取用户名
     *
     * @param token token
     * @return 用户名
     */
    public static String parseUsername(String token) {
        return parseToken(token).get(JwtConstants.JWT_USERNAME, String.class);
    }

    /**
     * 获取设备登陆的IP地址
     *
     * @param token token
     * @return 用户名
     */
    public static String parseIp(String token) {
        return parseToken(token).get(JwtConstants.JWT_IP, String.class);
    }


    /**
     * token 是否是无效的
     *
     * @param token token
     * @return true 无效，false 有效
     */
    public static boolean isInvalidToken(String token) {
        try {
            Claims claims = parseToken(token);
            Date expireTime = claims.getExpiration();
            return new Date().after(expireTime);
        } catch (Exception ignore) {
        }
        return true;
    }

}

