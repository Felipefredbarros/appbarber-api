package com.appbarber.api.infra;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

//se der erro cai aq
@RestControllerAdvice
public class Errors {
    //email ou senha errados
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroLogin() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos!");
    }

    //campos vazios
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErroValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    //email ja esxiste no banco
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity tratarErrosDeNegocio(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    //erros de enums
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity tratarErroDeLeitura(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Erro ao ler os dados. Verifique se as opções (como Forma de Pagamento, Dia da Semana ou Tipo de Contato) estão escritas exatamente como o sistema espera.");
    }
}
