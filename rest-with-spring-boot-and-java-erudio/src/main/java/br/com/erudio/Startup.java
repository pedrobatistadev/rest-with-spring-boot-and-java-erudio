package br.com.erudio;

import br.com.erudio.controllers.GreetingController;
import br.com.erudio.model.Greeting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }

}
