package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="reservation")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_reservation")
    private Integer idReservation;
    @ManyToOne
    @JoinColumn(name="id_resource")
    private Resource idResource;
    @ManyToOne
    @JoinColumn(name="id_user")
    private User idUser;
    @Column(name="start")
    private Date startDate;
    @Column(name="end")
    private Date endDate;
    @Column(name="status")
    private String status;
}
