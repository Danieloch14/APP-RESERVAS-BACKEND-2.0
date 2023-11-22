package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    //Optional<Reservation> findByIdResource_IdResourceAndStartDateIsLessThanOrEqualToAndEndDateIsGreaterThanOrEqualTo(Integer idResource, Timestamp startDate, Timestamp endDate);
    Optional<Reservation> findByIdResource_IdResourceAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(Integer idResource, Timestamp startDate, Timestamp endDate);
}
