package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location")
    private Integer idLocation;
    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region idRegion;
    @Column(name = "name")
    private String name;
    @Column(name = "floor")
    private String floor;
    @Column(name = "address")
    private String address;

}
