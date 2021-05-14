package br.com.zupacademy.priscila.proposta.feing.cartao;

import javax.validation.constraints.NotBlank;

public class NovoBloqueioRequest {

    @NotBlank
    private String sistemaResponsavel;

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

    public void setSistemaResponsavel(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

}
