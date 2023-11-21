package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Optional<Resource> findByCodNumberIgnoreCase(String codNumber);
}
