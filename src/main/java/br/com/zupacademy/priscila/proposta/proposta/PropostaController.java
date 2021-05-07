package br.com.zupacademy.priscila.proposta.proposta;

import br.com.zupacademy.priscila.proposta.analise.AnaliseFinanceiraClient;
import br.com.zupacademy.priscila.proposta.analise.AnaliseFinanceiraRequest;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import br.com.zupacademy.priscila.proposta.util.exception.ErroPadronizado;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @Autowired
    private AnaliseFinanceiraClient analiseFinanceiraClient;

    @Autowired
    private PropostaRepository repository;

    @Autowired
    private ExecutorTransacao executor;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid NovaPropostaRequest request,
                                    UriComponentsBuilder uriBuilder){

        if(jaExistePropostaParaEsseDocumento(request.getDocumento())) {
            logger.info("Já existe uma proposta para o documento: {}", request.getDocumento());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Já Existe uma proposta para esse CPF / CNPJ")));
        } else{

            Proposta novaProposta = request.toModel();

             executor.salvaEComita(novaProposta);

            consultaFinanceira(novaProposta);

            executor.atualizaEComita(novaProposta);

            URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();
            logger.info("Proposta criada para documento: {}", request.getDocumento());
            return ResponseEntity.created(uri).build();
        }
    }

    public void consultaFinanceira(Proposta proposta){
        Status status;
        try{
            logger.info("Enviando proposta {} para analise financeira", proposta.getId());
            analiseFinanceiraClient.consulta(new AnaliseFinanceiraRequest(proposta));
            status = Status.ELEGIVEL;
        }catch (FeignException.UnprocessableEntity e){
            logger.error("Proposta numero {} com restrição financeira.", proposta.getId());
            status = Status.NAO_ELEGIVEL;
        }
        proposta.setEstadoDaProposta(status);
    }

    @Transactional
    private boolean jaExistePropostaParaEsseDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }
}
