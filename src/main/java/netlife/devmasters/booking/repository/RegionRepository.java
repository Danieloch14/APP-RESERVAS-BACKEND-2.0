package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {
}
