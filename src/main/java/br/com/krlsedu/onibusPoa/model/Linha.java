package br.com.krlsedu.onibusPoa.model;

import io.swagger.annotations.ApiModelProperty;
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
	@ApiModelProperty(notes = "Identificador único da linha", position = 1)
	private String id;
	
	@NotBlank(message = "O código da linha deva ser informado!")
	@ApiModelProperty(notes = "Codigo identificador da linha na EPTC", position = 1, example = "T11-2")
	private String codigo;
	
	@NotBlank(message = "O nome da linha deva ser informado!")
	@ApiModelProperty(notes = "Nome da linha", position = 1, example = "3ª PERIMETRAL")
	private String nome;
}
