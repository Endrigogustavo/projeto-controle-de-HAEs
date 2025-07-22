package br.com.fateczl.apihae.driver.config.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;


@Configuration
@SecurityScheme(
    name = "cookieAuth",              // nome do esquema de segurança
    type = SecuritySchemeType.APIKEY, // tipo apiKey
    in = SecuritySchemeIn.COOKIE,     // usar o cookie
    paramName = "auth_token"          // nome do cookie que contem o JWT
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API HAE")
                .description("\n" + //
                        "Essa API foi desenvolvida para o gerenciamento de Horas de Atividades Específicas (HAE) da Fatec Zona Leste.")
                .version("1.0")
                .contact(new Contact()
                    .name("Fatec Zona Leste")
                    .url("https://fateczl.cps.sp.gov.br")
                    .email("contato@linkline.com"))
            );
    }
}