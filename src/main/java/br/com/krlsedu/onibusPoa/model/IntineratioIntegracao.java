package br.com.krlsedu.onibusPoa.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IntineratioIntegracao {
	private String idlinha;
	private String codigo;
	private String nome;
	private LocalizacaoIntegracao[] localizacoes;
}
