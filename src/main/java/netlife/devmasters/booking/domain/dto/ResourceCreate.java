package netlife.devmasters.booking.domain.dto;

public class ResourceCreate {
    private Integer idResource;
    private Integer idLocation;
    private Integer idTypeResource;
    private Integer idDadResource;
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
