package lk.lakderana.hms.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    /** return login user's user code */
    @Override
    public Optional<String> getCurrentAuditor() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            //return Optional.of(request.getHeader("PARTY_CODE"));
            return Optional.of("ADMIN");
        } else {
            return Optional.of("ADMIN");
        }
    }
}