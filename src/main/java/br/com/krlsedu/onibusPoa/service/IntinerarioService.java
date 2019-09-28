package br.com.krlsedu.onibusPoa.service;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.repository.IntinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class IntinerarioService {
	private final IntinerarioRepository intinerarioRepository;
	
	@Autowired
	public IntinerarioService(IntinerarioRepository intinerarioRepository) {
		this.intinerarioRepository = intinerarioRepository;
	}
	
	public Mono<Intinerario> salva(Intinerario intinerario) {
		return intinerarioRepository.save(intinerario);
	}
	
	public Flux<Intinerario> buscaTodos() {
		return intinerarioRepository.findAll();
	}
	
	public Flux<Intinerario> buscaIntinerario(Linha linha){
		return intinerarioRepository.findByLinha(Mono.just(linha));
	}
}
