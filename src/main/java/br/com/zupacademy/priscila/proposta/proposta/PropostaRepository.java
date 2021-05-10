package br.com.zupacademy.priscila.proposta.proposta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Boolean existsByDocumento(String documento);
    List<Proposta> findPropostaByStatusAndCartaoNumero(Status status, String numero);
}
