package bouraouiechaib.space.demo.webtoken;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private static final String SECRET = "638DAE3A90B0303CF3808F40A95A7F02F24B4B5D029C954CF543F78E9EF1DC0384BE681C249F1223F6B55AA21DC070914834CA22C8DD98E14A872CA010091ACC";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        return getClaims(jwt).getSubject();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isTokenValid(String jwt) {
        Date expiration = getClaims(jwt).getExpiration();
        return expiration.after(new Date());
    }
}
