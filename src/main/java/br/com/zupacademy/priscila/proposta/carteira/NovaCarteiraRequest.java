package br.com.zupacademy.priscila.proposta.carteira;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NovaCarteiraRequest {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private TipoCarteira carteira;

    public NovaCarteiraRequest(String email, TipoCarteira carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public Carteira toModel(Cartao cartao) {
        return new Carteira(cartao,this.email, this.carteira);
    }

    public String getEmail() {
        return email;
    }

    public TipoCarteira getCarteira() {
        return carteira;
    }
}
