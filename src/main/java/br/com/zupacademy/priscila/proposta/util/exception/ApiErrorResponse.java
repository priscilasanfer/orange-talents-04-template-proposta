package br.com.zupacademy.priscila.proposta.util.exception;

public class ApiErrorResponse { private String campo;

    private String mensagem;

    public ApiErrorResponse(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensagem() {
        return mensagem;
    }
}
