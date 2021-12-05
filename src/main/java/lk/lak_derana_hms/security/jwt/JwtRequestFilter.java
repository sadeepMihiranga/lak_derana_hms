package lk.lak_derana_hms.security.jwt;

import com.google.common.base.Strings;
import lk.lak_derana_hms.security.ApplicationUser;
import lk.lak_derana_hms.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider,
                            UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;
        String username = null;

        final String authorizationHeader = request.getHeader("Authorization");

        if (!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            jwt =  authorizationHeader.substring(7, authorizationHeader.length());
            username = jwtTokenProvider.extractUsername(jwt);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final ApplicationUser applicationUser = userService.loadUserUserByUsername(username);

            if(jwtTokenProvider.validateToken(jwt, applicationUser)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(applicationUser, applicationUser.getPassword(), applicationUser.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
