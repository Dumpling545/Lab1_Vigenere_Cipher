package lab1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class Starter implements CommandLineRunner {
	@Autowired
	private Controller controller;
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		App.setController(controller);
		App.main(args);
	}
}
