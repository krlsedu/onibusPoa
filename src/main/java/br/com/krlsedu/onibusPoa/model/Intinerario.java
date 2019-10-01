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
	private Long id;
	
	@NotNull(message = "A linha deve ser informada!")
	private Linha linha;
	
	@NotNull(message = "O ponto deve ser informado!")
	private String ponto;
	
	
	@GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2D)
	private Double[] location;
//	private GeoJsonPoint location;
	
//	@JsonCreator
//	public Intinerario(@JsonProperty("id") Long id,
//	                   @JsonProperty("linha") Linha linha,
//	                   @JsonProperty("ponto") String ponto,
//	                   @JsonProperty("location") Localizacao location) {
//		this.id = id;
//		this.linha = linha;
//		this.ponto = ponto;
//		this.location = new GeoJsonPoint(location.getLatitude(), location.getLongitude());
//	}
	
}
