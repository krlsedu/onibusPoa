package br.com.krlsedu.onibusPoa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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
public class Intinerario {
	
	@Id
	@NotNull(message = "O ponto deve ser informado!")
	private String ponto;
	
	@NotNull(message = "A linha deve ser informada!")
	private Linha linha;
	
	@GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2D)
	private Double[] location;
	
}
