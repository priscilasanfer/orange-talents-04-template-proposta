package br.com.zupacademy.priscila.proposta.proposta;

import br.com.zupacademy.priscila.proposta.util.validation.ProibeNovaPropostaParaMesmoDocumentoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository repository;

    @Autowired
    private ProibeNovaPropostaParaMesmoDocumentoValidator validator;

    @InitBinder
    public void init(WebDataBinder binder){
        binder.addValidators(validator);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Valid NovaPropostaRequest request,
                                    UriComponentsBuilder uriBuilder){
        Proposta novaProposta = request.toModel();
        repository.save(novaProposta);
        URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
