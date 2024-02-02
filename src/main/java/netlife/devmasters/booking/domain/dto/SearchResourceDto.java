package netlife.devmasters.booking.domain.dto;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class SearchResourceDto {
    private Timestamp date;
    private int hours;
    private int minutes;
    private int capacity;
    private int idRegion;
}
