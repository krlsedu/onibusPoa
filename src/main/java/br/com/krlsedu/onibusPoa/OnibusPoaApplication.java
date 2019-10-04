package br.com.krlsedu.onibusPoa;

import br.com.krlsedu.onibusPoa.service.LinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
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
