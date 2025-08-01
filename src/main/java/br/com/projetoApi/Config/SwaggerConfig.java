package br.com.projetoApi.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Automação Predial IoT - API")
                        .version("1.0")
                        .description("API para gerenciamento de dispositivos IoT em automação predial para controle de energia, segurança e conforto."));
    }
}