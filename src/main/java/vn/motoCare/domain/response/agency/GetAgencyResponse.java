package vn.motoCare.domain.response.agency;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GetAgencyResponse {
    private DataPage page;
    private List<Agency> agencies;

    @Getter
    @Setter
    public static class DataPage {
        private int totalPages;
        private int size;
        private int number;
        private long numberOfElements;
        private long totalElements;
    }

    @Getter
    @Setter
    public static class Agency {
        private Long id;
        private String name;
        private String email;
        private String address;
        private int phone;
        private boolean active;
        private Instant createdAt;
        private String createdBy;
    }
}
