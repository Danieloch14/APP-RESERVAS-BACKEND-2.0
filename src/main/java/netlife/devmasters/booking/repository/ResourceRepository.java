package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    List<Resource> findByCodNumberIgnoreCase(String codNumber);
    List<Resource> findByIdLocation_IdRegion_IdRegion(int idRegion);
    List<Resource> findByIdLocation_IdRegion_Name(String name);
    @Query(value="SELECT\n" +
            "  r.*\n" +
            "FROM\n" +
            " resource r\n" +
            " left join location l on l.id_location = r.id_location \n" +
            " left join region rg on rg.id_region = l.id_region \n" +
            "WHERE\n" +
            "  NOT EXISTS (\n" +
            "    SELECT\n" +
            "      *\n" +
            "    FROM\n" +
            "      reservation re\n" +
            "    WHERE\n" +
            "      re.id_resource = r.id_resource \n" +
            "      AND re.start_date BETWEEN :startDate and :endDate \n" +
            "  )\n" +
            "  and rg.id_region =:idRegion\n" +
            "  and r.capacity >=:capicity", nativeQuery = true)
    List<Resource> findByIdLocation_IdRegion(int idRegion, int capicity, Timestamp startDate, Timestamp endDate);
}
