package netlife.devmasters.booking.domain.dto;

import lombok.Data;

@Data
public class LocationCreate {
    private Integer idLocation;
    private Integer idRegion;
    private String place;
    private Integer floor;
    private String address;

}
