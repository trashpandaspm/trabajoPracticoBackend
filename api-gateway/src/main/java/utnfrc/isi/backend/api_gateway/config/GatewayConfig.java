package utnfrc.isi.backend.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("servicio-pedidos", r -> r.path("/api/clientes/**", "/api/contenedores/**", "/api/solicitudes/**")
                        .uri("http://servicio-pedidos:8080"))
                .route("servicio-logistica", r -> r.path("/api/ciudades/**", "/api/depositos/**", "/api/camiones/**", "/api/tarifas/**")
                        .uri("http://servicio-logistica:8083")) // El nombre del servicio en docker-compose

                .build();
    }

}
