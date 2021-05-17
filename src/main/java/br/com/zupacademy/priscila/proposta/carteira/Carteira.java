package br.com.zupacademy.priscila.proposta.carteira;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCarteira carteira;

    @Deprecated
    public Carteira() {}

    public Carteira(Cartao cartao, String email, TipoCarteira carteira) {
        this.cartao = cartao;
        this.email = email;
        this.carteira = carteira;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getEmail() {
        return email;
    }

    public TipoCarteira getCarteira() {
        return carteira;
    }

    public Long getId() {
        return id;
    }
}
