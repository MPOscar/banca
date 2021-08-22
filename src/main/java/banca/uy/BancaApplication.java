package banca.uy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@EnableSwagger2WebMvc
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class BancaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancaApplication.class, args);
    }

}
