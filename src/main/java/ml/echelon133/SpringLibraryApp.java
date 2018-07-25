package ml.echelon133;

import ml.echelon133.model.message.ErrorMessage;
import ml.echelon133.model.message.IErrorMessage;
import ml.echelon133.security.secret.ISecretGenerator;
import ml.echelon133.security.secret.SecretGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
public class SpringLibraryApp {

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
    public IErrorMessage errorMessage() {
        return new ErrorMessage();
    }

    @Bean
    public ISecretGenerator secretGenerator() {
        return new SecretGenerator(16);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringLibraryApp.class, args);
    }
}
