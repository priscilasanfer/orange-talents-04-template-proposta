package br.com.zupacademy.priscila.proposta.proposta.cartao;

import br.com.zupacademy.priscila.proposta.proposta.Proposta;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Cartao {

    @Id
    private String numero;

    @OneToOne(mappedBy = "cartao")
    private Proposta proposta;

    @Deprecated
    public Cartao() {}

    public Cartao(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public Proposta getProposta() {
        return proposta;
    }
}
