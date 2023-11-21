package netlife.devmasters.booking.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationCreate {
    private Integer idReservation;
    private Integer idResource;
    private Integer idUser;
    private Date startDate;
    private Date endDate;
    private String status;
}
