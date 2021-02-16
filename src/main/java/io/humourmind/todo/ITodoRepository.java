package io.humourmind.todo;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository  
interface ITodoRepository extends R2dbcRepository<Todo, UUID> {}
