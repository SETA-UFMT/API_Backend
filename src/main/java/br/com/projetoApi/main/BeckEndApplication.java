package br.com.projetoApi.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.projetoApi")
@EnableJpaRepositories(basePackages = "br.com.projetoApi.Entity.User.Repository")
@EntityScan(basePackages = "br.com.projetoApi.Entity.User.Model")
public class BeckEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeckEndApplication.class, args);
    }
}