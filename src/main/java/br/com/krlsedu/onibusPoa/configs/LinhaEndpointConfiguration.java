package br.com.krlsedu.onibusPoa.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class LinhaEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(LinhaHandler handler) {
        return route(GET("/linhas"), handler::all)
            .andRoute(GET("/linha/{codigo}"), handler::getByCodigo)
            .andRoute(POST("/linhas"), handler::create);
    }
}