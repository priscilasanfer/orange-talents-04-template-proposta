package br.com.zupacademy.priscila.proposta.feing.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${client.contas.url}", name = "${client.contas.name}")
public interface NovoCartaoClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes", consumes = "application/json")
    NovoCartaoResponse solicita(NovoCartaoRequest request);

}
