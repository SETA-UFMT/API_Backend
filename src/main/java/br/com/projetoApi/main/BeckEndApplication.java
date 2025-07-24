package br.com.ProjetoApi.Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.projetoApi")
public class BeckEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeckEndApplication.class, args);
	}

}