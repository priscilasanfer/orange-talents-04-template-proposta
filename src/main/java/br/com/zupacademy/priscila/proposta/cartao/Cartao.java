package br.com.zupacademy.priscila.proposta.cartao;

import br.com.zupacademy.priscila.proposta.proposta.Proposta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    @OneToOne(mappedBy = "cartao")
    private Proposta proposta;

    @Deprecated
    public Cartao() {}

    public Cartao(String numero) {
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public Proposta getProposta() {
        return proposta;
    }
}
