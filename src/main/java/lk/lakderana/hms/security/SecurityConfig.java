package lk.lakderana.hms.security;

import lk.lakderana.hms.security.filter.CustomAuthenticationFilter;
import lk.lakderana.hms.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/token");

        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //http.authorizeHttpRequests().antMatchers("/api/token/**").permitAll();
        http.authorizeHttpRequests().antMatchers("/api/**").permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static final String DEFAULT_KEY_TIMESTAMP = "timestamp";
    private static final String DEFAULT_KEY_STATUS = "status";
    private static final String DEFAULT_KEY_ERROR = "error";
    private static final String DEFAULT_KEY_ERRORS = "errors";
    private static final String DEFAULT_KEY_MESSAGE = "message";
    private static final String DEFAULT_KEY_PATH = "path";

    public static final String KEY_STATUS = "status";
    public static final String KEY_CODE = "code";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ERROR = "error";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_ERRORS = "errors";

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

                Map<String ,Object> defaultMap = super.getErrorAttributes(webRequest, options);

                Map<String ,Object> errorAttributes = new LinkedHashMap<>();
                Map<String ,Object> errorAttributesInner = new LinkedHashMap<>();

                errorAttributes.put(KEY_ERROR, errorAttributesInner);
                errorAttributesInner.put(KEY_MESSAGE, defaultMap.get(KEY_MESSAGE));
                errorAttributesInner.put(KEY_CODE, defaultMap.get(KEY_STATUS));
                errorAttributesInner.put(KEY_DESCRIPTION, defaultMap.get(KEY_ERROR));

                return errorAttributes;
            }
        };
    }
}
