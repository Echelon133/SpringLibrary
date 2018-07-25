package ml.echelon133.config;

import ml.echelon133.service.CustomUserDetailsService;
import ml.echelon133.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Order(1)
    @Configuration
    public static class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

        private ApplicationContext context;
        private PasswordEncoder passwordEncoder;

        @Autowired
        public BasicSecurityConfig(ApplicationContext context, PasswordEncoder passwordEncoder) {
            this.context = context;
            this.passwordEncoder = passwordEncoder;
        }

        @Bean
        public RequestMatcher basicRequestMatcher() {
            return new AntPathRequestMatcher("/api/users/**");
        }

        @Bean
        public UserDetailsService userDetailsService() {
            IUserService userService = context.getBean(IUserService.class);
            return new CustomUserDetailsService(userService);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .requestMatcher(basicRequestMatcher())
                    .authorizeRequests()
                    .antMatchers("/api/users/get-token").authenticated()
                    .antMatchers("/api/users/register").anonymous()
                    .and()
                    .httpBasic()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling();
        }
    }
}
