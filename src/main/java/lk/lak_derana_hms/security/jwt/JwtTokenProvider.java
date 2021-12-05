package lk.lak_derana_hms.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lk.lak_derana_hms.security.ApplicationUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class JwtTokenProvider {

    private String SECRET_KEY = "secret";
    private Integer tokenValidPeriod = 30;
    private String realm = "LakDerana-Web";
    private String clientId = "lakderanaweb";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(ApplicationUser applicationUser) {
        Map<String, Object> claims = new HashMap<>();
        return creatToken(claims, applicationUser.getUsername());
    }

    private String creatToken(Map<String, Object> claims, String username) {

        /*ApplicationUser user = (ApplicationUser) authentication.getPrincipal();

        claims.put("roles",
                user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        claims.put("functions",
                user
                        .getPermittedFunctions()
                        .stream()
                        .map(FunctionDTO::getFunctionId)
                        .collect(Collectors.toList())
        );*/

        return Jwts.builder()
                .setClaims(claims)
                .claim("user_name", username)
                .claim("user_description", username)
                .setSubject(username)
                .setAudience(clientId)
                .setIssuer(realm)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(tokenValidPeriod).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Boolean validateToken(String token, ApplicationUser applicationUser) {
        final String username = extractUsername(token);
        return (username.equals(applicationUser.getUsername()) && !isTokenExpired(token));
    }
}
