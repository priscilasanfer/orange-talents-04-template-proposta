package br.com.zupacademy.priscila.proposta.viagem;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.cartao.CartaoRepository;
import br.com.zupacademy.priscila.proposta.feing.cartao.CartaoClient;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/viagens")
public class AvisoViagemController {

    private final Logger logger = LoggerFactory.getLogger(AvisoViagemController.class);

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ExecutorTransacao transacao;

    @Autowired
    private CartaoClient client;

    @PostMapping("/{id}")
    public ResponseEntity<?> salvar( @PathVariable Long id,
                                     @RequestBody @Valid NovoAvisoViagemRequest request,
                                     HttpServletRequest servletRequest) {

        String userAgent = servletRequest.getHeader("User-Agent");
        String ip = servletRequest.getRemoteAddr();

        Optional<Cartao> possivelCartao = cartaoRepository.findById(id);

        if (possivelCartao.isEmpty()) {
            logger.info("Tentativa de aviso viagem para um cartão inexistente");
            return ResponseEntity.notFound().build();
        }

        try{
            client.avisoViagem(possivelCartao.get().getNumero(), request);
            AvisoViagem avisoViagem = request.toModel(possivelCartao.get(), ip, userAgent);
            transacao.salvaEComita(avisoViagem);
            return ResponseEntity.ok().build();
        }catch (FeignException.UnprocessableEntity e) {
            logger.info("Já existe um aviso viagem para o cartao {} com  a cidade fornecida", id);
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErroPadronizado(List.of("Já existe um aviso de viagem para a cidade fornecida.")));
        } catch (Exception e){
            logger.info("Erro ao tentar conectar com a api de cartões!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErroPadronizado(List.of("Erro ao tentar se conectar com o serviço externo. Por favor tente novamente mais tarde!")));
        }
    }
}
