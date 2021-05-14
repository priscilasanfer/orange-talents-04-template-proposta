package br.com.zupacademy.priscila.proposta.cartao;

import br.com.zupacademy.priscila.proposta.bloqueio.Bloqueio;
import br.com.zupacademy.priscila.proposta.proposta.Proposta;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    @OneToOne(mappedBy = "cartao")
    private Proposta proposta;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "bloqueio_id", referencedColumnName = "id")
    private Bloqueio bloqueio;

    @Enumerated(EnumType.STRING)
    private StatusCartao status = StatusCartao.ATIVO;

    @Deprecated
    public Cartao() {}

    public Cartao(String numero) {
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public void setBloqueio(Bloqueio bloqueio) {
        this.bloqueio = bloqueio;
        this.status = StatusCartao.BLOQUEADO;
    }

    public boolean bloqueado(){
        return this.status.equals(StatusCartao.BLOQUEADO);
    }

    public String getNumero() {
        return numero;
    }
}
