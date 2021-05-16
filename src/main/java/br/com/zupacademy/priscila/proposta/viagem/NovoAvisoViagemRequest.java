package br.com.zupacademy.priscila.proposta.viagem;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class NovoAvisoViagemRequest {

    @NotBlank
    private String destino;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate validoAte;

    @Deprecated
    public NovoAvisoViagemRequest() {
    }

    public NovoAvisoViagemRequest(@NotBlank String destino,
                                  @Future LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public AvisoViagem toModel(Cartao cartao, String ip, String userAgent) {
        return new AvisoViagem(this.destino,
                this.validoAte,
                ip,
                userAgent,
                cartao);
    }
}
