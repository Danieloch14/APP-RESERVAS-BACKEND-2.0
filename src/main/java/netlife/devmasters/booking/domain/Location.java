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
    @Column(name = "place")
    private String place;
    @Column(name = "floor")
    private Integer floor;
    @Column(name = "address")
    private String address;

}
