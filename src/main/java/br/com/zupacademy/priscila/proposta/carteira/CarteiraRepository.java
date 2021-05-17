package br.com.zupacademy.priscila.proposta.carteira;

import br.com.zupacademy.priscila.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    boolean existsByCarteiraAndCartao(TipoCarteira tipoCarteira, Cartao cartao);
}

