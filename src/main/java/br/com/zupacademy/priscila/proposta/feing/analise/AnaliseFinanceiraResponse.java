package br.com.zupacademy.priscila.proposta.feing.analise;

public class AnaliseFinanceiraResponse {

    private String documento;
    private String nome;
    private StatusAnaliseFinanceira resultadoSolicitacao;
    private Long idProposta;

    public AnaliseFinanceiraResponse(String documento,
                                     String nome,
                                     StatusAnaliseFinanceira resultadoSolicitacao,
                                     Long idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public StatusAnaliseFinanceira getEstado() {
        return resultadoSolicitacao;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
