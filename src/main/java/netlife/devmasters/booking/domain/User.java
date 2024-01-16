package netlife.devmasters.booking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 9203940391795653856L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer idUser;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_personal_data")
    private PersonalData personalData;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name = "date_entry")
    private Date dateEntry;
    @Column(name = "date_last_login")
    private Date dateLastLogin;
    private boolean isActive;
    private boolean isNotLocked;
    @Column(name = "time_lock")
    private Timestamp timeLock;

    public User() {
    }
}
