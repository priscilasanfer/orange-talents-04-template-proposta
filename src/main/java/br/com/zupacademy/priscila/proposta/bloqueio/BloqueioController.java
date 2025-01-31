package br.com.zupacademy.priscila.proposta.bloqueio;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.cartao.CartaoRepository;
import br.com.zupacademy.priscila.proposta.feing.cartao.NovoBloqueioRequest;
import br.com.zupacademy.priscila.proposta.feing.cartao.CartaoClient;
import br.com.zupacademy.priscila.proposta.proposta.PropostaController;
import br.com.zupacademy.priscila.proposta.util.ExecutorTransacao;
import br.com.zupacademy.priscila.proposta.util.exception.ErroPadronizado;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private CartaoClient client;

    @PostMapping("/{id}")
    public ResponseEntity<?> salvar(@PathVariable Long id,
                                    HttpServletRequest servletRequest,
                                    @AuthenticationPrincipal Jwt usuario){
        String email = (String) usuario.getClaims().get("email");

        Optional<Cartao> cartao = repository.findById(id);

        if(!email.equals(cartao.get().getEmail())) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        String userAgent = servletRequest.getHeader("User-Agent");
        String ip = servletRequest.getRemoteAddr();

        if(cartao.isEmpty()){
            logger.info("Tentativa de bloqueio para um cartão inexistente");
            return ResponseEntity.notFound().build();
        }

        try{
            client.bloquear(cartao.get().getNumero(), new NovoBloqueioRequest("proposta"));
            Bloqueio bloqueio = new Bloqueio(ip, userAgent, cartao.get());

            cartao.get().setBloqueio(bloqueio);
            executor.atualizaEComita(cartao.get());
            logger.info("O cartão {} foi bloqueado", cartao.get().getId());

            return ResponseEntity.ok().build();

        }catch (FeignException e){
            logger.info("O cartão {} já esta bloqueado", cartao.get().getId());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Este cartão já está bloqueado")));
        }
    }
}
