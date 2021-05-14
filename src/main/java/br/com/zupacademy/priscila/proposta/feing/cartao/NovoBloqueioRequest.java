package br.com.zupacademy.priscila.proposta.feing.cartao;

import javax.validation.constraints.NotBlank;

public class NovoBloqueioRequest {

    @NotBlank
    private String sistemaResponsavel;

    @Deprecated
    public NovoBloqueioRequest() {
    }

    public NovoBloqueioRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

}
