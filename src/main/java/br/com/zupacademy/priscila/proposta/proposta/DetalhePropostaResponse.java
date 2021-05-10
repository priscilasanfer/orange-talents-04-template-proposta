package br.com.zupacademy.priscila.proposta.proposta;

public class DetalhePropostaResponse {

    private String codigo;
    private String email;
    private String nome;
    private Status status;

    public DetalhePropostaResponse(Proposta proposta) {
        this.codigo = proposta.getCodigo();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.status = proposta.getStatus();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public Status getStatus() {
        return status;
    }

}
