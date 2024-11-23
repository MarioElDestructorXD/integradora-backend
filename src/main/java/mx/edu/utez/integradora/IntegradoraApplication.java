package mx.edu.utez.integradora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "mx.edu.utez")
public class IntegradoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegradoraApplication.class, args);
	}

}
