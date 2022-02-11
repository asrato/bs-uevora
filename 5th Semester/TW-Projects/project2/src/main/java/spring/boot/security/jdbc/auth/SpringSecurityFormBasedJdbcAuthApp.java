package spring.boot.security.jdbc.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "spring.boot.security.jdbc.auth")
public class SpringSecurityFormBasedJdbcAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityFormBasedJdbcAuthApp.class, args);

        System.out.println("Galaxy Runners is live at http://localhost:8080/");
    }

}
