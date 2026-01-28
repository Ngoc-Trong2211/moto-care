package vn.motoCare.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumColor;
import vn.motoCare.util.enumEntity.EnumProductType;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_prd_vehicle")
public class VehicleProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    private String name;

    @Enumerated(EnumType.STRING)
    private List<EnumColor> colors;

    @Enumerated(EnumType.STRING)
    private EnumStatusProduct status;

    @Enumerated(EnumType.STRING)
    private EnumProductType type;

    private long price;
    private int quantity;
}
