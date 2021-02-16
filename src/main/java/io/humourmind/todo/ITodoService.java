package io.humourmind.todo;

import java.util.UUID;

import org.springframework.data.domain.Sort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

	Flux<Todo> findAllBySort(Sort sortOrder);

	Mono<Todo> findById(UUID id);

	Mono<Todo> save(Mono<Todo> resource);

	Mono<Void> deleteById(UUID id);

}
