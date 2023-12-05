package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.PersonalData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer> {
    Optional<PersonalData> findOneByEmail(String email);
    List<PersonalData> findAllByEmail(String correoPersonal);
    @Query(value = "SELECT d FROM PersonalData d WHERE d.name LIKE %:filtro% OR d.lastname LIKE %:filtro%")
    Page<PersonalData> searchNativo(@Param("filtro") String filtro, Pageable pageable);
}
