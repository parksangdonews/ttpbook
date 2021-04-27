package works.tripod.ttpbook.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity // custom security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // super.configure(httpSecurity);
        httpSecurity.authorizeRequests()
                .mvcMatchers("/", "/index", "/login", "/sign-up", "/check-email", "/check-eamil-token",
                        "/email-login", "/check-eamil-login", "login-link", "/profile/*").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/**").permitAll() // api
                .mvcMatchers(HttpMethod.GET, "/profile/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/link/**").permitAll() // auto link
                .mvcMatchers("/assets/**").permitAll() // static web resources
                .anyRequest().authenticated();


    }


}
