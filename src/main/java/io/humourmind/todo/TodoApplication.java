package io.humourmind.todo;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;

@SpringBootApplication(proxyBeanMethods = false)
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> staticResourceRouter() {
		return RouterFunctions.resources("/**", new ClassPathResource("static/"))
				.andRoute(GET("/"), req -> ok().contentType(MediaType.TEXT_HTML)
						.bodyValue(new ClassPathResource("static/index.html")));
	}

	@Bean
	RouterFunction<ServerResponse> routeHandler(ITodoService todoService) {
		return RouterFunctions.route().path("/todo", builder -> builder
				.GET("/{id}", req -> ok().contentType(MediaType.APPLICATION_JSON)
						.body(todoService.findById(
								UUID.fromString(req.pathVariable("id"))), Todo.class)
						.switchIfEmpty(Mono.empty()))
				.POST(req -> created(null).contentType(MediaType.APPLICATION_JSON)
						.body(todoService.save(
								req.body(BodyExtractors.toMono(Todo.class))), Todo.class))
				.PUT(req -> ok().contentType(MediaType.APPLICATION_JSON)
						.body(todoService.save(
								req.body(BodyExtractors.toMono(Todo.class))), Todo.class))
				.DELETE("/{id}",
						req -> ok().body(
								todoService.deleteById(
										UUID.fromString(req.pathVariable("id"))),
								Void.class))
				.GET(req -> ok().contentType(MediaType.APPLICATION_JSON)
						.body(todoService.findAllBySort(
								Sort.by(Sort.Direction.DESC, "id")), Todo.class)))

				.build();
	}

	// @Bean
	public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(
				new ResourceDatabasePopulator(new ClassPathResource("data/schema.sql"),
						new ClassPathResource("data/data.sql")));
		return initializer;
	}

}
