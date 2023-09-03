//package com.rishu.springauthentication.securiry.jwt;
//
//import java.security.Key;
//import java.util.Date;
//
//import com.rishu.springauthentication.securiry.services.UserDetailsImpl;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseCookie;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.WebUtils;
//
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//
///*
//    This class has 3 main functions:
//
//    getJwtFromCookies: get JWT from Cookies by Cookie name
//    generateJwtCookie: generate a Cookie containing JWT from username, date, expiration, secret
//    getCleanJwtCookie: return Cookie with null value (used for clean Cookie)
//    getUserNameFromJwtToken: get username from JWT
//    validateJwtToken: validate a JWT with a secret
//
// */
//
//
//@Component
//public class JwtUtils {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
//
//    @Value("${rishu.app.jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${rishu.app.jwtExpirationMs}")
//    private int jwtExpirationMs;
//
//    @Value("${rishu.app.jwtCookieName}")
//    private String jwtCookie;
//
//    public String getJwtFromCookies(HttpServletRequest request){
//        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
//        if(cookie != null){
//            return cookie.getValue();
//        }else {
//            return null;
//        }
//    }
//
//
//    public ResponseCookie getCleanJwtCookie() {
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
//        return cookie;
//    }
//
//    public String getUserNameFromJwtToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(key()).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//
//    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
//        String jwt = generateTokenFromUserName(userPrincipal.getUsername());
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
//        return cookie;
//    }
//
//    private Key key(){
//        //return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
//            return true;
//        } catch (MalformedJwtException e) {
//            logger.error("Invalid JWT token: {}", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            logger.error("JWT token is expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            logger.error("JWT token is unsupported: {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            logger.error("JWT claims string is empty: {}", e.getMessage());
//        }
//
//        return false;
//    }
//
//    public String generateTokenFromUserName(String username){
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//
//}
//
//
