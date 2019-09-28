package br.com.krlsedu.onibusPoa.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Linha {
	@Id
	@NotNull(message = "O Id da Linha deve ser informado!")
	private Long id;
	
	@NotBlank(message = "O código da linha deva ser informado!")
	private String codigo;
	
	@NotBlank(message = "O nome da linha deva ser informado!")
	private String nome;
}
