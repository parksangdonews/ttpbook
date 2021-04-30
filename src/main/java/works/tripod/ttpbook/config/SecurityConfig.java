package works.tripod.ttpbook.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity // custom security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // super.configure(httpSecurity);
        httpSecurity.authorizeRequests()
                .mvcMatchers("/", "/index", "/login", "/sign-up", "/check-email", "/check-email-token"
                        , "/logout", "/email-login", "/check-email-login", "login-link", "/profile/*").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/**").permitAll() // api
                .mvcMatchers(HttpMethod.GET, "/profile/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/link/**").permitAll() // auto link
                .anyRequest().authenticated()
                /*.and()
                .httpBasic()*/
                .and()
                .formLogin()
                /*.loginPage("/login")*/
                .successForwardUrl("/")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();

        /*
                .logoutSuccessUrl("/")
        */

        // .logoutSuccessHandler("로그아웃 후 처리할 핸들러")

    }


    @Override
    protected void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // authentication manager (see below)
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }



    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        // 바로 안되던 이유, 아래 경로들이 스태틱 커먼즈 경로로 인식됨.
        /*
        Resources under "/css".
        Resources under "/js".
        Resources under "/images".
        Resources under "/webjars".
        */
        webSecurity.ignoring()
                .mvcMatchers("/assets/**") // static web resources // web templates using path.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}
