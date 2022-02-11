package spring.boot.security.jdbc.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String sqlUserName = "select u.user_name, u.user_pass, u.enable from userinfo u where u.user_name = ?";
        final String sqlAuthorities = "select ur.user_name, ur.user_role from user_role ur where ur.user_name = ?";
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(sqlUserName)
                .authoritiesByUsernameQuery(sqlAuthorities).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                /** STAFF ACCESS **/
                .antMatchers("/newevent", "/newtime").access("hasRole('STAFF')")
                /** ATLETA ACCESS **/
                .antMatchers("/newinscricao", "/inscricoes_atleta", "/payment").access("hasRole('ATLETA')")
                /** PUBLIC ACCESS **/
                .antMatchers("/", "/about_us", "/track_participants", "/search_events", "/register",
                        "/classification", "/newuser", "/event_info", "/previous_events", "/live_events",
                        "/future_events", "/event_info", "/login", "/static/**", "/error**", "/confirmations")
                .permitAll().anyRequest().authenticated()// ,																							// /,login
                .and()// ,
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
