package requestHandler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import requestHandler.user.User;
import requestHandler.user.repository.UserRepository;

@SpringBootApplication
public class JpaSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaSecurityApplication.class, args);
    }

    /**
     *
     * для тестов доступа к эндпоинтам
     */

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            users.save(new User("user", encoder.encode("password"), "ROLE_USER"));
            users.save(new User("admin", encoder.encode("password"), "ROLE_ADMIN"));
            users.save(new User("operator", encoder.encode("password"), "ROLE_OPERATOR"));
        };
    }
}