package br.com.krlsedu.onibusPoa.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
@EqualsAndHashCode
public class Intinerario {
	
	@Id
	@NotNull(message = "O ponto deve ser informado!")
	@ApiModelProperty(notes = "O identificador do ponto onde a estutura é codigo_linha-sequencial_ponto", position = 1, example = "T11-2-0")
	private String ponto;
	
	@NotNull(message = "A linha deve ser informada!")
	@ApiModelProperty(notes = "Os dados da linha são informados", position = 2)
	private Linha linha;
	
	@GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2D)
	@ApiModelProperty(notes = "array contendo as coordenadas do ponto latitude,longitude", position = 3)
	private Double[] location;
}
