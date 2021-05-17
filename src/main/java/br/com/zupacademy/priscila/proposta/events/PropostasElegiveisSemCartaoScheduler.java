package br.com.zupacademy.priscila.proposta.events;

import br.com.zupacademy.priscila.proposta.proposta.Proposta;
import br.com.zupacademy.priscila.proposta.proposta.PropostaRepository;
import br.com.zupacademy.priscila.proposta.proposta.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropostasElegiveisSemCartaoScheduler {

    private final Logger logger = LoggerFactory.getLogger(PropostasElegiveisSemCartaoScheduler.class);

    @Autowired
    private PropostaRepository repository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Scheduled(fixedRateString = "${periodicidade.proposta.sem.cartao}")
    public void verificaPropostaElegivelSemCartao() {
        logger.info("Job: Verificando se existe propostas elegiveis sem cartão");
        List<Proposta> propostas = repository.findPropostaByStatusAndCartaoNumero(Status.ELEGIVEL, null);

        for (Proposta proposta: propostas) {
            publisher.publishEvent(new PropostaElegivelEvent(proposta));
        }
    }
}
