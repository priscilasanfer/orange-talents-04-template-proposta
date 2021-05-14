package br.com.zupacademy.priscila.proposta.bloqueio;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime bloqueadoEm;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String userAgente;

    @OneToOne(mappedBy = "bloqueio")
    private Cartao cartao;

    @Deprecated
    public Bloqueio() {}

    public Bloqueio(String ip, String userAgente, Cartao cartao) {
        this.ip = ip;
        this.userAgente = userAgente;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
