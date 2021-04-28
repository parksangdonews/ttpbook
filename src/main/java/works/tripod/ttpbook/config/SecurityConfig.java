package works.tripod.ttpbook.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity // custom security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // super.configure(httpSecurity);
        httpSecurity.authorizeRequests()
                .mvcMatchers("/", "/index", "/login", "/sign-up", "/check-email", "/check-email-token",
                        "/email-login", "/check-email-login", "login-link", "/profile/*").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/**").permitAll() // api
                .mvcMatchers(HttpMethod.GET, "/profile/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/link/**").permitAll() // auto link
                .mvcMatchers("/assets/**").permitAll() // static web resources // web templates using path.
                .anyRequest().authenticated();
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
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}
