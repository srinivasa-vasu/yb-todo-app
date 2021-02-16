package io.humourmind.todo;

import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
@RequestMapping("/todo")
public class TodoController {

	private final TodoService todoService;

	TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping
	public Flux<Todo> findAll() {
		return todoService.findAllBySort(Sort.by(Sort.Direction.DESC, "id"));
	}

	@GetMapping("/{id}")
	public Mono<Todo> findById(@PathVariable("id") UUID id) {
		return todoService.findById(id);
	}

	@PutMapping
	public Mono<Todo> update(@RequestBody Mono<Todo> todo) {
		return todoService.save(todo);
	}

	@PostMapping
	public Mono<Todo> create(@RequestBody Mono<Todo> todo) {
		return todoService.save(todo);
	}

	@DeleteMapping("/{id}")
	public Mono<Void> delete(@PathVariable("id") UUID id) {
		return todoService.deleteById(id);
	}
}
