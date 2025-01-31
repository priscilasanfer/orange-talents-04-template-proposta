package br.com.zupacademy.priscila.proposta.feing.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${client.analise.url}", name = "${client.analise.name}")
public interface AnaliseFinanceiraClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao", consumes = "application/json")
    AnaliseFinanceiraResponse consulta(AnaliseFinanceiraRequest request);
}
