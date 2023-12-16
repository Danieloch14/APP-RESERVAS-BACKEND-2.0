package netlife.devmasters.booking.domain.dto;

import lombok.Data;

@Data
public class ResourceCreate {
    private Integer idResource;
    private Integer idTypeResource;
    private LocationCreate idLocation;
    private Integer idParentResource;
    private String description;
    private String codNumber;
    private Integer capacity;
    private Double price;
    private Boolean isParking;
    private String pathImages;
    private String name;
    private String content;

}
