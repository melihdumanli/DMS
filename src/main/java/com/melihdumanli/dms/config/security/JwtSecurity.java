package com.melihdumanli.dms.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtSecurity {

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256("ets");
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        Claim usernameClaim = (Claim) claims.get("username");
        return usernameClaim.asString();
    }

    public String generateToken(UserDetails userDetails) {
        try {
            return JWT.create()
                    .withAudience()
                    .withClaim("username", userDetails.getUsername())
                    .withIssuer("com.melihdumanli.dms")
                    .withExpiresAt(new Date(System.currentTimeMillis() + 300000))
                    .withIssuedAt(new Date())
                    .sign(getAlgorithm());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String generateRefreshToken(UserDetails userDetails) {
        try {
            return JWT.create()
                    .withAudience()
                    .withClaim("username", userDetails.getUsername())
                    .withIssuer("com.melihdumanli.dms")
                    .withExpiresAt(new Date(System.currentTimeMillis() + 300000))
                    .withIssuedAt(new Date())
                    .sign(getAlgorithm());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getExpiresAt();
    }

    public static Claims extractClaims(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claimMap = jwt.getClaims();
        return new DefaultClaims(claimMap);
    }
}