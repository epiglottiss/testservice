package com.cus.zbp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.cus.zbp.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

  private final UserService userService;

  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserAuthenticationFailureHandler getFailureHandler() {
    return new UserAuthenticationFailureHandler();
  }

  // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();

    http.authorizeRequests().antMatchers("/", "/user/login", "/user/register", "/user/email-auth",
        "/user/find/password", "/user/reset/password").permitAll();

    http.formLogin().loginPage("/user/login").failureHandler(getFailureHandler()).permitAll();

    http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/").invalidateHttpSession(true);

    return http.build();
  }

}
