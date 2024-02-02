package netlife.devmasters.booking.domain.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReservationCreate {
    private Integer idReservation;
    private Integer idResource;
    private Integer idUser;
    private Timestamp startDate;
    private Timestamp endDate= new Timestamp(0);
    private String status;
    private String description;
    private Integer hours;
    private Integer minutes;
}
