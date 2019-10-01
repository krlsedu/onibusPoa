package br.com.krlsedu.onibusPoa.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Localizacao {
	private Double latitude;
	private Double longitude;
	private Double raio;
}
