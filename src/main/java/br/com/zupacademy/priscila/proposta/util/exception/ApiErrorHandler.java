package br.com.zupacademy.priscila.proposta.util.exception;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiErrorHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){
        List<ApiErrorResponse> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = exception.getBindingResult().getGlobalErrors();

        fieldErrors.forEach(fieldError -> {
            errors.add(new ApiErrorResponse(fieldError.getField(),
                    messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())));
        });

        objectErrors.forEach(objectError -> {
            errors.add(new ApiErrorResponse(objectError.getObjectName(),
                    messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));
        });

        return errors;
    }
}
