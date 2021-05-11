package br.com.zupacademy.priscila.proposta.feing.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${client.contas.url}", name = "${client.contas.name}")
public interface NovoCartaoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/cartoes", consumes = "application/json")
    NovoCartaoResponse solicita(@RequestParam("idProposta") Long idProposta);

}
