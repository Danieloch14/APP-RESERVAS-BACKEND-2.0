package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;


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
    @Column(name="start_date")
    private Timestamp startDate;
    @Column(name="end_date")
    private Timestamp endDate;
    @Column(name="status")
    private String status;
    @Column(name="description")
    private String description;
    @Column(name="hours")
    private Integer hours;
    @Column(name="minutes")
    private Integer minutes;
}
