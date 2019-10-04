package br.com.krlsedu.onibusPoa.configs;

import br.com.krlsedu.onibusPoa.model.Linha;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class LinhaCriadaEvent extends ApplicationEvent {
    
    @Getter
    private final Linha linha;
    
    public LinhaCriadaEvent(Linha linha) {
        super(linha);
        this.linha = linha;
    }
    
    
}
