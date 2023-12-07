package com.aluracursos.casadelcodigo;

import com.aluracursos.casadelcodigo.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CasaDelCodigoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CasaDelCodigoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muestraElMenu();
	}
}
