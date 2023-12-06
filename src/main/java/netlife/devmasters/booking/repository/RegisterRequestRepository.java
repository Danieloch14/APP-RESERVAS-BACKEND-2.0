package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRequestRepository extends JpaRepository<RegisterRequest, Integer> {
}
