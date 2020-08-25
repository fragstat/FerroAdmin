package arsenal.metiz.AresenalMetiz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/", "/**", "/products", "/index", "/static/**", "/action/**").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/products.html", "/products", "/index", "/static/**", "/action/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/assets/**", "/resources/public/**", "/public/**");
    }

//        @Bean
//        public SpringTemplateEngine templateEngine() {
//            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//            templateEngine.addDialect(new SpringSecurityDialect());
//            return templateEngine;
//        }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails serg =
                User.withUsername("sergey")
                        .password(encoder.encode("vAlAvin2002"))
                        .roles("ADMIN")
                        .build();
        UserDetails ekat =
                User.withUsername("ekat")
                        .password(encoder.encode("12345678"))
                        .roles("USER")
                        .build();
        UserDetails danil =
                User.withUsername("danil")
                        .password(encoder.encode("fragstag"))
                        .roles("USER")
                        .build();
        UserDetails ksenia =
                User.withUsername("ksenia")
                        .password(encoder.encode("valavin12345"))
                        .roles("USER")
                        .build();
        UserDetails dir =
                User.withUsername("direktor")
                        .password(encoder.encode("val130502"))
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(serg, ekat, danil, ksenia, dir);
    }
}

