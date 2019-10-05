package social;


import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
@EnableTransactionManagement
@EnableCaching
public class SocialRestApi {

    public static void main(String[] args) {
        SpringApplication.run(SocialRestApi.class, args);
    }
}
