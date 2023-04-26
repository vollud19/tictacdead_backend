package at.kaindorf.tictacdead;

import at.kaindorf.tictacdead.service.BackendLogic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TictacdeadApplication {

	public static void main(String[] args) {
//		new BackendLogic();
		SpringApplication.run(TictacdeadApplication.class, args);
	}

}
