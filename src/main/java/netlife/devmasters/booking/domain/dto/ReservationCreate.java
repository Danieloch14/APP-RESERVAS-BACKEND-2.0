package netlife.devmasters.booking.domain.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReservationCreate {
    private Integer idReservation;
    private Integer idResource;
    private Integer idUser;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;
}
