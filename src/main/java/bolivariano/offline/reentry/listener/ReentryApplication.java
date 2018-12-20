package bolivariano.offline.reentry.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@ImportResource("classpath:camel-config.xml")
public class ReentryApplication {

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext context = SpringApplication.run(ReentryApplication.class, args);

	} 

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	

}
