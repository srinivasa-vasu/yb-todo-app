package io.humourmind.todo;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(value = "todo")
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

	@Id
	private UUID id;
	private String task;
	private boolean status;

}
