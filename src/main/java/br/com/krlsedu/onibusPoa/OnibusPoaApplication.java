package br.com.krlsedu.onibusPoa;

import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@SpringBootApplication
public class OnibusPoaApplication implements ApplicationRunner {
	private final LinhaService linhaService;
	
	@Autowired
	public OnibusPoaApplication(LinhaService linhaService) {
		this.linhaService = linhaService;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(OnibusPoaApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
	
		
	}
	
}
