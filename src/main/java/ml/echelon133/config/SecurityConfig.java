package ml.echelon133.config;

import ml.echelon133.security.jwt.JWTAuthenticationFilter;
import ml.echelon133.security.jwt.JWTAuthenticationManager;
import ml.echelon133.security.jwt.JWTAuthenticationProvider;
import ml.echelon133.service.CustomUserDetailsService;
import ml.echelon133.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Order(1)
    @Configuration
    public static class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public AuthenticationProvider jwtAuthenticationProvider() {
            return new JWTAuthenticationProvider();
        }

        @Bean
        public AuthenticationManager jwtAuthenticationManager() {
            return new JWTAuthenticationManager(jwtAuthenticationProvider());
        }

        @Bean
        public RequestMatcher jwtRequestMatcher() {
            return new AntPathRequestMatcher("/api/**");
        }

        @Bean
        public JWTAuthenticationFilter jwtAuthenticationFilter() {
            JWTAuthenticationFilter filter = new JWTAuthenticationFilter();
            filter.setAuthenticationManager(jwtAuthenticationManager());
            filter.setRequiresAuthenticationRequestMatcher(jwtRequestMatcher());
            filter.setContinueChainBeforeSuccessfulAuthentication(true);
            return filter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .anonymous().disable()
                    .httpBasic().disable()
                    .requestMatcher(jwtRequestMatcher())
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET,"/api/authors**").hasRole("USER")
                    .antMatchers(HttpMethod.POST,"/api/authors**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/authors/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/authors/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/genres**").hasRole("USER")
                    .antMatchers(HttpMethod.POST, "/api/genres**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/genres/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/genres/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/books**").hasRole("USER")
                    .antMatchers(HttpMethod.POST, "/api/books**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PATCH, "/api/books/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling()
                    .and()
                    .addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        }
    }

    @Order(2)
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
            return new AntPathRequestMatcher("/users/**");
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
                    .antMatchers("/users/get-token").authenticated()
                    .antMatchers("/users/register").anonymous()
                    .and()
                    .httpBasic()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling();
        }
    }
}
