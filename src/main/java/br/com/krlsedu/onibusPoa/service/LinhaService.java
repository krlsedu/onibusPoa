package br.com.krlsedu.onibusPoa.service;

import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.repository.LinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class LinhaService {
	
	private final LinhaRepository linhaRepository;
	
	@Autowired
	public LinhaService(LinhaRepository linhaRepository) {
		this.linhaRepository = linhaRepository;
	}
	
	public Mono<Linha> salva(Linha linha) {
		return linhaRepository.save(linha);
	}
	
	public Flux<Linha> buscaTodos() {
		return linhaRepository.findAll();
	}
	
	public Flux<Linha> buscaPorCodigo(String codigo){
		return linhaRepository.findByCodigo(Mono.just(codigo));
	}
	
	public Flux<Linha> buscaPorNome(String nome){
		return linhaRepository.findByNome(Mono.just(nome));
	}
	
	public Mono<Void> deletePorCodigo(String codigo){
		return linhaRepository.deleteAll(buscaPorCodigo(codigo));
	}
}
