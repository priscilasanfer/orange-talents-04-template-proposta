package br.com.zupacademy.priscila.proposta.events;

import br.com.zupacademy.priscila.proposta.proposta.Proposta;

public class PropostaElegivelEvent {

    private Proposta proposta;

    public PropostaElegivelEvent(Proposta proposta) {
        this.proposta = proposta;
    }

    public Proposta getProposta() {
        return proposta;
    }
}
