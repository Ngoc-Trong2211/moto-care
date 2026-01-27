package vn.motoCare.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.motoCare.domain.response.ResponseSystem;

@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(value = {
            IdInvalidException.class,
            NameExistsException.class,
            PasswordMismatchException.class,
            ChangePasswordException.class,
            EmailAlreadyExistsException.class,
            StatusIsActiveException.class
    })
    public ResponseEntity<ResponseSystem<Object>> handleExceptionInvalid(Exception ex){
        ResponseSystem<Object> res = new ResponseSystem<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setData(null);
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
