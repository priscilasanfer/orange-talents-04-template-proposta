package br.com.zupacademy.priscila.proposta.proposta;

import br.com.zupacademy.priscila.proposta.util.exception.ErroPadronizado;
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

    @Autowired
    private PropostaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Valid NovaPropostaRequest request,
                                    UriComponentsBuilder uriBuilder){

        if(jaExistePropostaParaEsseDocumento(request.getDocumento())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Já Existe uma proposta para esse CPF / CNPJ")));
        } else{
            Proposta novaProposta = request.toModel();
            repository.save(novaProposta);
            URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }
    }

    private boolean jaExistePropostaParaEsseDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }
}
