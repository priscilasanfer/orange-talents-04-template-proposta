package br.com.zupacademy.priscila.proposta.util.validation;

import br.com.zupacademy.priscila.proposta.proposta.NovaPropostaRequest;
import br.com.zupacademy.priscila.proposta.proposta.PropostaRepository;
import br.com.zupacademy.priscila.proposta.util.exception.PropostaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProibeNovaPropostaParaMesmoDocumentoValidator implements Validator {

    @Autowired
    private PropostaRepository repository;

    @Override
    public boolean supports(Class<?> aClass) {
        return NovaPropostaRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return ;

        NovaPropostaRequest request = (NovaPropostaRequest) target;

        Boolean isValid = repository.existsByDocumento(request.getDocumento());

        if (isValid) {
            throw new PropostaException("Já existe uma proposta para esse CPF / CNPJ");
        }
    }
}