package br.com.zupacademy.priscila.proposta.biometria;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.cartao.CartaoRepository;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/biometrias")
public class BiometriaController {

    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ExecutorTransacao transacao;

    @PostMapping("/{id}")
    public ResponseEntity<?> salvar(@RequestBody @Valid NovaBiometriaRequest request,
                                    @PathVariable("id") Long id,
                                    UriComponentsBuilder uriBuilder){

        Optional<Cartao> cartao = cartaoRepository.findById(id);

        if (cartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Biometria biometria = request.toModel(cartao.get());
        transacao.salvaEComita(biometria);
        URI uri = uriBuilder.path("/biometrias/{id}").buildAndExpand(biometria.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

}
