package vn.motoCare.domain.request.user;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.StatusEnumUser;

@Getter
@Setter
public class UserSpecificationRequest {
    private String name;
    private String email;
    private StatusEnumUser status;
}
