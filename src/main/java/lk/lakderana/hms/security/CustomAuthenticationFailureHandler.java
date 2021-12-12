package lk.lakderana.hms.security;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);

        System.out.println(exception.getClass().getSimpleName());

        if(exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
            response.sendRedirect("error1");
        } else if (exception.getClass().isAssignableFrom(LockedException.class)) {
            response.sendRedirect("error2");
        }
    }
}
