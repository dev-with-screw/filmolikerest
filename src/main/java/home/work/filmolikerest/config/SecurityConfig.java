package home.work.filmolikerest.config;


import home.work.filmolikerest.security.jwt.JwtConfigurer;
import home.work.filmolikerest.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    public void configure(final WebSecurity webSecurity) {
//        webSecurity.ignoring().antMatchers("/test/**", "/test2/**", "/register", "/login");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtConfigurer admin = http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/register", "/login", "/test/**", "/test2/**", "/test3").permitAll()
                .antMatchers("/notes", "/note/**").hasRole("USER")
                .antMatchers("/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
