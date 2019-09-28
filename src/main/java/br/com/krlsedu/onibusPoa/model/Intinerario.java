package br.com.krlsedu.onibusPoa.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Intinerario {
	@Id
	private Long id;
	
	@NotNull(message = "A linha deve ser informada!")
	private Linha linha;
	
	@NotNull(message = "O ponto deve ser informado!")
	private String ponto;
	
	private String latitude;
	
	private String longitude;
	
}
