package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value="\tselect\n" +
            "\tr1_0.*\n" +
            "from\n" +
            "\tpublic.reservation r1_0\n" +
            "where\n" +
            "\tr1_0.id_resource =:idResource\n" +
            "\tand :startDate between r1_0.start_date and r1_0.end_date", nativeQuery = true)
    List<Reservation> findByIdResource_IdResourceAndStartDateBetween(Integer idResource, Timestamp startDate);
    @Query(value="select\n" +
            "\tr.*\n" +
            "from\n" +
            "\t reservation r\n" +
            "where\n" +
            "\textract(year\n" +
            "from\n" +
            "\tr.start_date) = :year\n" +
            "\tand extract(month\n" +
            "from\n" +
            "\tr.start_date) = :month\n" +
            "\tand extract(day\n" +
            "from\n" +
            "\tr.start_date)=:day", nativeQuery = true)
    List<Reservation> findByStartDate_Day_Month_Year(Integer day, Integer month, Integer year);
    @Query(value="select\n" +
            "\tr.*\n" +
            "from\n" +
            "\t reservation r\n" +
            "where\n" +
            "\textract(year\n" +
            "from\n" +
            "\tr.start_date) = :year\n" +
            "\tand extract(month\n" +
            "from\n" +
            "\tr.start_date) = :month", nativeQuery = true)
    List<Reservation> findByStartDate_Month_Year(Integer month, Integer year);
    @Query(value="select\n" +
            "\tr.*\n" +
            "from\n" +
            "\t reservation r\n" +
            "where\n" +
            "\textract(year\n" +
            "from\n" +
            "\tr.start_date) = :year", nativeQuery = true)
    List<Reservation> findByStartDate_Year(Integer year);
}
