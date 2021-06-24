package io.humourmind.todo;

import java.util.UUID;

import io.r2dbc.spi.ConnectionFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import reactor.core.publisher.Mono;

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

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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

	@RouterOperations({@RouterOperation(path = "/todo", beanClass = TodoService.class, beanMethod = "findAllBySort", method = GET),
			@RouterOperation(path = "/todo/{id}", beanClass = TodoService.class, beanMethod = "findById", method = GET,
					operation = @Operation(operationId = "findById", parameters = {@Parameter(in = PATH, name = "id", description = "todo-id to find")})),
			@RouterOperation(path = "/todo", beanClass = TodoService.class, beanMethod = "save", method = {POST, PUT}),
			@RouterOperation(path = "/todo/{id}", beanClass = TodoService.class, beanMethod = "deleteById", method = DELETE,
					operation = @Operation(operationId = "deleteById", parameters = {@Parameter(in = PATH, name = "id", description = "todo-id to delete")}))})
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
