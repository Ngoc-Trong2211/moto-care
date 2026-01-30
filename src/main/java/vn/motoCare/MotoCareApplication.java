package vn.motoCare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MotoCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotoCareApplication.class, args);
	}

}
