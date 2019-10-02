package br.com.krlsedu.onibusPoa.service;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.model.Localizacao;
import br.com.krlsedu.onibusPoa.repository.IntinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
	
	public Flux<Intinerario> salvaTodos(List<Intinerario> intinerarios) {
		return intinerarioRepository.saveAll(intinerarios);
	}
	
	public Flux<Intinerario> buscaTodos() {
		return intinerarioRepository.findAll();
	}
	
	public Flux<Intinerario> buscaIntinerario(Linha linha) {
		return intinerarioRepository.findByLinha(Mono.just(linha));
	}
	
	public Flux<Intinerario> buscaPorLocalizacao(Point p, Distance d) {
		return intinerarioRepository.findByLocationNear(p, d);
	}
	
	public Flux<Intinerario> buscaPorPonto(String ponto) {
		return intinerarioRepository.findByPonto(Mono.just(ponto));
	}
	
	public Mono<Void> deletePorPonto(String ponto) {
		return intinerarioRepository.deleteAll(buscaPorPonto(ponto));
	}
}
