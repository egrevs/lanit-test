package com.egrevs.project.lanittest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui.html
@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Lanit test")
                        .version("1.0")
                        .description("Api для Lanit test"));
    }
}
