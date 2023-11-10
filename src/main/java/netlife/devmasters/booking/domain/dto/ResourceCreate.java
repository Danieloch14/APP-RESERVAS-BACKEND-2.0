package netlife.devmasters.booking.domain.dto;

import jakarta.persistence.*;
import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.TypeResource;

public class ResourceCreate {
    private Integer idResource;
    private Location idLocation;
    private TypeResource idTypeResource;
    private String name;
    /*
    @Column(name = "description")
    private String description;
     */
    private Integer capacity;
    private Integer codNumber;
    private String content;
    private Double price;
    private Boolean isParking;
    private String pathImages;
}
