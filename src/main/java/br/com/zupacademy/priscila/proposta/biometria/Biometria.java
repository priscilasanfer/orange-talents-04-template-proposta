package br.com.zupacademy.priscila.proposta.biometria;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String biometria;

    @ManyToOne
    private Cartao cartao;

    @CreationTimestamp
    private OffsetDateTime dataDeCriação;

    @Deprecated
    public Biometria() {}

    public Biometria(String biometria, Cartao cartao) {
        this.biometria = biometria;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public String getBiometria() {
        return biometria;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
