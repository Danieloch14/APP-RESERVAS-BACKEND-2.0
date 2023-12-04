package netlife.devmasters.booking.domain.dto;

import lombok.Data;

import java.util.Date;
@Data
public class SearchResourceDto {
    private Date date;
    private int hours;
    private int minutes;
    private int capacity;
    private int idRegion;
}
