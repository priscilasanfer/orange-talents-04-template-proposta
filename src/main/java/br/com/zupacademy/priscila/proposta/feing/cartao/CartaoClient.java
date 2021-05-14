package br.com.zupacademy.priscila.proposta.feing.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${client.contas.url}", name = "${client.contas.name}")
public interface CartaoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/cartoes", consumes = "application/json")
    NovoCartaoResponse solicita(@RequestParam("idProposta") Long idProposta);

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{id}/bloqueios", consumes = "application/json")
    void bloquear(@PathVariable("id") String id, @RequestBody NovoBloqueioRequest request);

}
