package netlife.devmasters.booking.domain.dto;

import lombok.Data;

@Data
public class ResourceCreate {
    private Integer idResource;
    private LocationCreate idLocation;
    private Integer idTypeResource;
    private Integer idDadResource;
    private String description;
    private Integer capacity;
    private String codNumber;
    private String content;
    private Double price;
    private Boolean isParking;
    private String pathImages;
    private String name;
}
