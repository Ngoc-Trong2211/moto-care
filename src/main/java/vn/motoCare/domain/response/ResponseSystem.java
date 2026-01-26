package vn.motoCare.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSystem<T>{
    private int status;
    private Object message;
    private T data;
}
