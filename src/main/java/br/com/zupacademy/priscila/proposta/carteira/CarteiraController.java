package br.com.zupacademy.priscila.proposta.carteira;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.cartao.CartaoRepository;
import br.com.zupacademy.priscila.proposta.feing.cartao.CartaoClient;
import br.com.zupacademy.priscila.proposta.proposta.PropostaController;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import br.com.zupacademy.priscila.proposta.util.exception.ErroPadronizado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/carteiras")
public class CarteiraController {

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @Autowired
    private ExecutorTransacao executor;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private CartaoClient client;

    @PostMapping("/{id}")
    public ResponseEntity<?> salvar(@PathVariable Long id,
                                    @RequestBody @Valid NovaCarteiraRequest request,
                                    UriComponentsBuilder uriBuilder){

        Optional<Cartao> cartao = cartaoRepository.findById(id);

        if(cartao.isEmpty()){
            logger.info("Tentativa de cadastro de uma nova carteira para um cartão inexistente");
            return ResponseEntity.notFound().build();
        }

        if(possuiCarteira(request.getCarteira(), cartao.get())){
            logger.info("Carteira já esta associada ao cartão {}", cartao.get().getId());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Esta carteira já esta associada ao cartão")));
        } else{
            client.associaCarteira(cartao.get().getNumero(), request);
            Carteira novaCarteira = request.toModel(cartao.get());
            executor.salvaEComita(novaCarteira);
            URI uri = uriBuilder.path("/carteiras/{id}").buildAndExpand(novaCarteira.getId()).toUri();
            logger.info("Nova carteria associada ao cartao {}", cartao.get().getId());
            return ResponseEntity.created(uri).build();
        }
    }

    @Transactional
    private boolean possuiCarteira(TipoCarteira carteira, Cartao cartao) {
        return carteiraRepository.existsByCarteiraAndCartao(carteira, cartao);
    }
}
