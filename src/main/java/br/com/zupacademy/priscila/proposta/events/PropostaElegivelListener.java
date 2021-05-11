package br.com.zupacademy.priscila.proposta.events;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.feing.cartao.NovoCartaoClient;
import br.com.zupacademy.priscila.proposta.feing.cartao.NovoCartaoResponse;
import br.com.zupacademy.priscila.proposta.proposta.Proposta;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PropostaElegivelListener {

    private final Logger logger = LoggerFactory.getLogger(PropostaElegivelListener.class);

    @Autowired
    private NovoCartaoClient novoCartaoClient;

    @Autowired
    private ExecutorTransacao executor;

    @Async
    @EventListener
    public void novoCartaoParaPropostaElegivel(PropostaElegivelEvent event){
        Proposta proposta = event.getProposta();

        try{
            NovoCartaoResponse cartao = novoCartaoClient.solicita(proposta.getId());
            proposta.setCartao(new Cartao(cartao.getId()));
            logger.info("Novo cartao associado a proposta {}", proposta.getId());
            executor.atualizaEComita(proposta);

        }catch (Exception e){
            logger.error("Um erro inexperado aconteceu, causa: {} e mensagem: {}", e.getCause(), e.getMessage());
        }
    }
}
