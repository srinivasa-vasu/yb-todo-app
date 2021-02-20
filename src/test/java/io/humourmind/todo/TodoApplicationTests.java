package io.humourmind.todo;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(TodoService.class)
class TodoApplicationTests {

	@MockBean
	private ITodoRepository repository;

	@Autowired
	private WebTestClient webClient;

	@Test
	void createTodo() {
		Todo todo = new Todo(UUID.randomUUID(), "todo", true);

		Mockito.when(repository.save(todo)).thenReturn(Mono.just(todo));

		webClient.post().uri("/todo").accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON).body(BodyInserters.fromValue(todo))
				.exchange().expectStatus().isCreated().expectBody(Todo.class);

		Mockito.verify(repository).save(todo);
	}

	@Test
	void findTodoById() {
		UUID id = UUID.randomUUID();
		Todo todo = new Todo(id, "todo", true);

		Mockito.when(repository.findById(id)).thenReturn(Mono.just(todo));

		webClient.get().uri("/todo/{id}", id).accept(APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectBody(Todo.class).isEqualTo(todo);
		// .expectBody()
		// .jsonPath("$.id").isEqualTo(id.toString())
		// .jsonPath("$.task").isEqualTo("todo");

		Mockito.verify(repository).findById(id);
	}

	@Test
	void findAll() {
		Todo todo = new Todo(UUID.randomUUID(), "todo", true);

		Mockito.when(repository.findAll(Sort.by(Sort.Direction.DESC, "id")))
				.thenReturn(Flux.just(todo));

		webClient.get().uri("/todo").accept(APPLICATION_JSON).exchange().expectStatus()
				.isOk().expectBodyList(Todo.class);

		Mockito.verify(repository).findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	@Test
	void deleteTodoById() {
		UUID id = UUID.randomUUID();
		Mockito.when(repository.deleteById(id)).thenReturn(Mono.empty());

		webClient.delete().uri("/todo/{id}", id).exchange().expectStatus().isOk();
		Mockito.verify(repository).deleteById(id);
	}

}
