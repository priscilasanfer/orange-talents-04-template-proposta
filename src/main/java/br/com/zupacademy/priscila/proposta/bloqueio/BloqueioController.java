package br.com.zupacademy.priscila.proposta.bloqueio;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.cartao.CartaoRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bloqueios")
public class BloqueioController {

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @Autowired
    private ExecutorTransacao executor;

    @Autowired
    private CartaoRepository repository;

    @PostMapping("/{id}")
    public ResponseEntity<?> salvar(@PathVariable Long id, UriComponentsBuilder uriBuilder, HttpServletRequest request){
        Optional<Cartao> possivelCartao = repository.findById(id);

        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        if(possivelCartao.isEmpty()){
            logger.info("Tentativa de bloqueio para cartão inexistente");
            return ResponseEntity.notFound().build();
        }

       Cartao cartao = possivelCartao.get();

        if(cartao.bloqueado()){
            logger.info("O cartão {} já esta bloqueado", cartao.getId());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Este cartão já está bloqueado")));
        }

        Bloqueio bloqueio = new Bloqueio(ip, userAgent, cartao);
        cartao.setBloqueio(bloqueio);

        executor.salvaEComita(cartao);
        logger.info("O cartão {} foi bloqueado", cartao.getId());
        return ResponseEntity.ok().build();
    }
}
