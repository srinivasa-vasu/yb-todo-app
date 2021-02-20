package io.humourmind.todo;

import java.util.UUID;

import org.springframework.data.domain.Sort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TodoService implements ITodoService {

	private final ITodoRepository todoRepository;

	TodoService(ITodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Override
	public Flux<Todo> findAllBySort(Sort sortOrder) {
		return todoRepository.findAll(sortOrder);
	}

	@Override
	public Mono<Todo> findById(UUID id) {
		return todoRepository.findById(id);
	}

	@Override
	public Mono<Todo> save(Mono<Todo> resource) {
		return resource.flatMap(r -> todoRepository.save(r));
	}

	@Override
	public Mono<Void> deleteById(UUID id) {
		return todoRepository.deleteById(id);
	}
}
