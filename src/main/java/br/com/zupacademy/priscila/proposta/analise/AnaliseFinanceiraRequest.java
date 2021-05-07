package br.com.zupacademy.priscila.proposta.analise;

import br.com.zupacademy.priscila.proposta.proposta.Proposta;

public class AnaliseFinanceiraRequest {

    private String documento;
    private String nome;
    private String idProposta;

    public AnaliseFinanceiraRequest(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId().toString();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
