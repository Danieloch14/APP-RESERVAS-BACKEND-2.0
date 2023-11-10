package netlife.devmasters.booking.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import netlife.devmasters.booking.domain.Region;

public class LocationCreate {
    private Integer idLocation;
    private Region idRegion;
    private String name;
    private String floor;
    private String address;

}
