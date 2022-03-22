package io.humourmind.todo;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "todo")
public record Todo(@Id UUID id, String task, boolean status) {

}
