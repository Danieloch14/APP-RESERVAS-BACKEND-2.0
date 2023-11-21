package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.TypeResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeResourceRepository extends JpaRepository<TypeResource, Integer> {
    Optional<TypeResource> findByNameIgnoreCase(String nameTypeResource);
}
