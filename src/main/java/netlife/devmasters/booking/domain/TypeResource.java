package netlife.devmasters.booking.domain;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "type_resource")
public class TypeResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_resource")
    private Integer idTypeResource;
    @Column(name = "name")
    private String name;
    /*
    @Column(name = "description")
    private String description;
     */
    @Column(name = "time_anticipation")
    private Long timeAnticipation;
}
