package br.com.krlsedu.onibusPoa.configs;

import br.com.krlsedu.onibusPoa.model.Linha;
import org.springframework.context.ApplicationEvent;

public class LinhaCriadaEvent extends ApplicationEvent {

    public LinhaCriadaEvent(Linha linha) {
        super(linha);
    }
}
