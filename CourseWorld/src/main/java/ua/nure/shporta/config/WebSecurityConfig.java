package ua.nure.shporta.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import ua.nure.shporta.service.impl.DefaultUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DefaultUserDetailsService userDetailsService;
    @Autowired
    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/manage/courses*").hasRole("MANAGER")
                .antMatchers("/courses/{courseId}/lectures").hasAnyRole("MANAGER","USER")
                .antMatchers("/courses*").hasRole("USER")
                .antMatchers("/user*").hasRole("USER")
                .antMatchers("/").hasRole("USER")
                .antMatchers("/pay*").hasRole("USER")
                .antMatchers("/login*", "/user/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(defaultAuthenticationSuccessHandler)
                .and()
                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}