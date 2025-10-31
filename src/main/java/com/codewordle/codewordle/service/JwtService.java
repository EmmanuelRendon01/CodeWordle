package com.codewordle.codewordle.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service dedicated to handling JSON Web Tokens (JWT).
 * Responsibilities include generating tokens, validating them, and extracting
 * information from them.
 */
@Service
public class JwtService {

    // IMPORTANT: This secret key should be stored securely in application.properties
    // and not be hardcoded. It must be a long, complex string encoded in Base64.
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;


    /**
     * Extracts the username from a given JWT.
     * @param token The JWT string.
     * @return The username (subject claim) from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * A generic method to extract a specific claim from a token.
     * @param token The JWT string.
     * @param claimsResolver A function to extract the desired claim.
     * @return The claim of type T.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT for a given user without extra claims.
     * @param userDetails The user details for whom the token is being generated.
     * @return The generated JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT with extra claims for a given user.
     * The token includes the user's username as the subject, an issued-at date,
     * an expiration date, and is signed with the HS256 algorithm.
     * @param extraClaims A map of extra claims to include in the token payload.
     * @param userDetails The user details.
     * @return The generated JWT string.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a token by checking if the username matches and if the token has not expired.
     * @param token The JWT string to validate.
     * @param userDetails The user details to validate against.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Parses the JWT to extract all its claims.
     * @param token The JWT string.
     * @return The Claims object containing the token's payload.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Decodes the Base64 secret key and returns it as a Key object
     * suitable for signing.
     * @return The cryptographic Key for signing and validation.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}