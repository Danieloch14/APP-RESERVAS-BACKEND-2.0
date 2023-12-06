package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "register_request")
public class RegisterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_register_request")
    private Integer idRegisterRequest;
    @ManyToOne
    @JoinColumn(name = "id_personal_data")
    private PersonalData personalData;
    @Column(name = "request_date")
    private Date requestDate;
    @Column(name = "status")
    private String status;
}
