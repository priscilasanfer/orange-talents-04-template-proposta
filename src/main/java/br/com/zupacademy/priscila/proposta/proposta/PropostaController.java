package br.com.zupacademy.priscila.proposta.proposta;

import br.com.zupacademy.priscila.proposta.feing.analise.AnaliseFinanceiraClient;
import br.com.zupacademy.priscila.proposta.feing.analise.AnaliseFinanceiraRequest;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import br.com.zupacademy.priscila.proposta.util.exception.ErroPadronizado;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private Tracer tracer;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid NovaPropostaRequest request,
                                    UriComponentsBuilder uriBuilder){
        if(jaExistePropostaParaEsseDocumento(request.getDocumento())) {
            logger.info("Já existe uma proposta para o documento: {}", request.getDocumento());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Já Existe uma proposta para esse CPF / CNPJ")));
        } else{
            Proposta novaProposta = request.toModel();

            Span activeSpan = tracer.activeSpan();
            activeSpan.setTag("user.email", novaProposta.getEmail());
            String userEmail = activeSpan.getBaggageItem(novaProposta.getEmail());
            activeSpan.setBaggageItem("user.email", userEmail);
            activeSpan.log("Testando Logs, Baggage e Tags do Jaeger. Log é  desnecessário pois o Spring ja envia automaticamente os logs");

            executor.salvaEComita(novaProposta);
            consultaFinanceira(novaProposta);
            executor.atualizaEComita(novaProposta);
            URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();
            logger.info("Proposta criada para documento: {}", request.getDocumento());
            return ResponseEntity.created(uri).build();
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> detalhar (@PathVariable String uuid){
        Optional<Proposta> proposta = repository.findByCodigo(uuid);
        if (proposta.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroPadronizado(List.of("Proposta não encontrada")));
        }
        return ResponseEntity.ok(new DetalhePropostaResponse(proposta.get()));
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
