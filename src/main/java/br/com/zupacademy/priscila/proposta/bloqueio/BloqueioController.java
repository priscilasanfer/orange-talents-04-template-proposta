package br.com.zupacademy.priscila.proposta.bloqueio;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.cartao.CartaoRepository;
import br.com.zupacademy.priscila.proposta.feing.cartao.NovoBloqueioRequest;
import br.com.zupacademy.priscila.proposta.feing.cartao.NovoCartaoClient;
import br.com.zupacademy.priscila.proposta.proposta.PropostaController;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import br.com.zupacademy.priscila.proposta.util.exception.ErroPadronizado;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private NovoCartaoClient client;

    @PostMapping("/{id}")
    public ResponseEntity<?> salvar(@PathVariable Long id,
                                    HttpServletRequest servletRequest){
        Optional<Cartao> cartao = repository.findById(id);

        String userAgent = servletRequest.getHeader("User-Agent");
        String ip = servletRequest.getRemoteAddr();

        NovoBloqueioRequest bloqueioRequest = new NovoBloqueioRequest();
        bloqueioRequest.setSistemaResponsavel("proposta");

        if(cartao.isEmpty()){
            logger.info("Tentativa de bloqueio para um cartão inexistente");
            return ResponseEntity.notFound().build();
        }

        if(cartao.get().bloqueado()){
            logger.info("O cartão {} já esta bloqueado", cartao.get().getId());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Este cartão já está bloqueado")));
        }

        try{
            client.bloquear(cartao.get().getNumero(), bloqueioRequest);
            Bloqueio bloqueio = new Bloqueio(ip, userAgent, cartao.get());

            cartao.get().setBloqueio(bloqueio);
            executor.atualizaEComita(cartao.get());
            logger.info("O cartão {} foi bloqueado", cartao.get().getId());

            return ResponseEntity.ok().build();


        }catch (FeignException e){

           logger.info("Não foi possivel fazer o bloqueio do cartao {}, por favor tente mais tarde", cartao.get().getId());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Não foi possivel fazer o bloqueio, por favor tente novamente mais tarde")));
        }
    }
}
