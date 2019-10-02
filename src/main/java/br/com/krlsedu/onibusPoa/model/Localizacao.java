package br.com.krlsedu.onibusPoa.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Localizacao {
	
	@ApiModelProperty(notes = "Latitude do ponto de referência para consultar as linhas", position = 1, example = "-30.038185")
	private Double latitude;
	
	@ApiModelProperty(notes = "Latitude do ponto de referência para consultar as linhas", position = 1, example = "-51.177003")
	private Double longitude;
	
	@ApiModelProperty(notes = "Raio em kms para consultar as linhas", position = 1, example = "1.0")
	@Positive(message = "o raio deve ser maior que zero")
	private Double raio;
}
