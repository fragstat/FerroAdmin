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
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/products.html", "/products", "/resources/**", "/index", "/static/**", "/action/**", "/api/**").permitAll()
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
        web.ignoring().antMatchers("/css/**", "/js/**", "/assets/**", "/resources/**", "/public/**");
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
        UserDetails nekita =
                User.withUsername("nik")
                        .password(encoder.encode("1234"))
                        .roles("USER")
                        .build();
        UserDetails kolokoltsev =
                User.withUsername("kolokoltsev")
                        .password(encoder.encode("kolokol72"))
                        .roles("SELLER")
                        .build();
        UserDetails ferro_sklad =
                User.withUsername("ferroSklad")
                        .password(encoder.encode("beloretskS"))
                        .roles("USER")
                        .build();
        UserDetails artem_beloretsk =
                User.withUsername("artemBel")
                        .password(encoder.encode("arsenal2021A"))
                        .roles("SELLER")
                        .build();
        UserDetails evegenia_beloretsk =
                User.withUsername("evgeniaBel")
                        .password(encoder.encode("arsenal2021E"))
                        .roles("SELLER")
                        .build();

        return new InMemoryUserDetailsManager(serg, ekat, ksenia, dir, nekita, kolokoltsev, ferro_sklad,
                artem_beloretsk, evegenia_beloretsk);
    }
}

