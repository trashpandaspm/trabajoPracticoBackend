package utnfrc.isi.backend.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
/*
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para la UI de Swagger
                .route("swagger-ui", r -> r.path("/swagger-ui.html", "/webjars/swagger-ui/**", "/swagger-ui/**")
                        .uri("lb://servicio-logistica")) // Apunta a CUALQUIER servicio con la dependencia de Swagger UI

                // Ruta para la configuración de la UI de Swagger
                .route("swagger-config", r -> r.path("/v3/api-docs/swagger-config")
                        .uri("lb://api-gateway"))

                // Ruta para los datos de la API de servicio-pedidos
                .route("servicio-pedidos-docs", r -> r.path("/v3/api-docs/servicio-pedidos")
                        .uri("lb://servicio-pedidos"))

                // Ruta para los datos de la API de servicio-logistica
                .route("servicio-logistica-docs", r -> r.path("/v3/api-docs/servicio-logistica")
                        .uri("lb://servicio-logistica"))

                // Rutas para la API real de servicio-pedidos
                .route("servicio-pedidos-api", r -> r.path("/api/clientes/**", "/api/contenedores/**", "/api-gsolicitudes/**")
                        .uri("lb://servicio-pedidos"))

                // Rutas para la API real de servicio-logistica
                .route("servicio-logistica-api", r -> r.path("/api/ciudades/**", "/api/depositos/**", "/api/camiones/**", "/api/tarifas/**")
                        .uri("lb://servicio-logistica"))
                .build();
    }*/
}