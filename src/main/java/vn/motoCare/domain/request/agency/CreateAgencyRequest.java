package vn.motoCare.domain.request.agency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAgencyRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String address;
    @NotNull
    private int phone;
}