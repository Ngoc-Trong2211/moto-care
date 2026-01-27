package vn.motoCare.domain.response.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumMethodPermission;

import java.util.List;

@Getter
@Setter
public class GetRoleResponse {
    private DataPage dataPage;
    private List<Role> roles;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataPage{
        private int number;
        private int size;
        private int numberOfElements;
        private int totalPages;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Role{
        private Long id;
        private String name;
        private boolean active;
        private List<Permission> permissions;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Permission{
            private String path;
            private EnumMethodPermission method;
            private String entity;
        }
    }
}
