package vn.motoCare.util.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.motoCare.domain.response.ResponseSystem;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseSystem<Object>> handleAllException(Exception ex){
        ResponseSystem<Object> res = new ResponseSystem<>();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setData(null);
        res.setMessage("Lỗi hệ thống =>>>>>" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(value = {
            IdInvalidException.class,
            NameExistsException.class,
            PasswordMismatchException.class,
            ChangePasswordException.class,
            EmailAlreadyExistsException.class,
            StatusIsActiveException.class,
            RefreshTokenInvalidException.class
    })
    public ResponseEntity<ResponseSystem<Object>> handleExceptionInvalid(Exception ex){
        ResponseSystem<Object> res = new ResponseSystem<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setData(null);
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseSystem<Object>> handleExceptionValid(MethodArgumentNotValidException ex){
        ResponseSystem<Object> res = new ResponseSystem<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setData(null);
        List<FieldError> errors = ex.getFieldErrors();
        List<String> arrErrors = errors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        res.setMessage(arrErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseSystem<Object>> handleExceptionBadCredential(BadCredentialsException ex){
        ResponseSystem<Object> res = new ResponseSystem<>();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setMessage(ex.getMessage());
        res.setData(null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }
}
