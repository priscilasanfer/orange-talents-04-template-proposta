package br.com.zupacademy.priscila.proposta.viagem;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate validoAte;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String userAgent;

    @CreationTimestamp
    private OffsetDateTime criadoEm;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public AvisoViagem() {}

    public AvisoViagem(
            String destino,
            LocalDate validoAte,
            String ip,
            String userAgent,
            Cartao cartao) {
        this.destino = destino;
        this.validoAte = validoAte;
        this.ip = ip;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
