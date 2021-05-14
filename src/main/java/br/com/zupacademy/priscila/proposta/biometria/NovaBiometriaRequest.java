package br.com.zupacademy.priscila.proposta.biometria;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;

import javax.validation.constraints.NotBlank;
import java.util.Base64;

public class NovaBiometriaRequest {

    @NotBlank
    private String biometria;

    public String getBiometria() {
        return biometria;
    }

    public Biometria toModel(Cartao cartao) {
        byte[] biometriaDecode = Base64.getDecoder().decode(this.biometria.getBytes());
        String biometriaString = new String(biometriaDecode);

        return new Biometria(biometriaString, cartao);
    }
}
