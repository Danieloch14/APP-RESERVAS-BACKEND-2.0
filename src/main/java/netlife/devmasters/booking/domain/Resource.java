package netlife.devmasters.booking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resource")
    private Integer idResource;
    @ManyToOne
    @JoinColumn(name = "id_type_resource")
    private TypeResource idTypeResource;
    @ManyToOne
    @JoinColumn(name = "id_location")
    private Location idLocation;
    @ManyToOne
    @JoinColumn(name = "res_id_resource")
    @JsonIgnore
    private Resource parentResource;

    @Column(name = "description")
    private String description;

    @Column(name = "cod_number")
    private String codNumber;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "price")
    private Double price;
    @Column(name = "is_parking")
    private Boolean isParking;
    @Column(name = "path_images")
    private String pathImages;
    @Column(name = "name")
    private String name;
}
