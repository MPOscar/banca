package banca.uy.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("banca.uy.core.repository")
@Configuration
public class MongoDBConfig {
}
