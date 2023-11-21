package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByNameIgnoreCase(String name);
}
