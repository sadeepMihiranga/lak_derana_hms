package lk.lakderana.hms.security;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    public static final String SECRET_KEY = "secret";
    public static final String ROLES = "roles";
    public static final String TOKEN_PREFIX_BEARER = "Bearer ";

    public static final Date ACCESS_TOKEN_EXPIRE_10_MIN = new Date(System.currentTimeMillis() + (10 * 60 * 1000));
    public static final Date REFRESH_TOKEN_EXPIRE_30_MIN = new Date(System.currentTimeMillis() + (30 * 60 * 1000));
}
