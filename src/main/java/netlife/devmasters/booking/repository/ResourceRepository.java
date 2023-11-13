package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}
