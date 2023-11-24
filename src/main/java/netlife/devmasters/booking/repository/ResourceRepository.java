package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Optional<Resource> findByCodNumberIgnoreCase(String codNumber);
    List<Resource> findByIdLocation_IdRegion_IdRegion(int idRegion);
    List<Resource> findByIdLocation_IdRegion_Name(String name);
}
