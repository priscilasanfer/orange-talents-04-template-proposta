package br.com.zupacademy.priscila.proposta.carteira;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import br.com.zupacademy.priscila.proposta.util.validation.ValueOfEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NovaCarteiraRequest {

    @Email
    @NotBlank
    private String email;

    @NotNull
    @ValueOfEnum(enumClass = TipoCarteira.class)
    private String carteira;

    public NovaCarteiraRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public Carteira toModel(Cartao cartao) {
        return new Carteira(cartao,this.email, TipoCarteira.valueOf(carteira));
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
