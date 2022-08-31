package be.podor.theater.repository;

import be.podor.theater.model.FloorEnum;
import be.podor.theater.model.TheaterSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

    // 층별 정보 조회
    @Query(value = "SELECT ts FROM TheaterSeat ts WHERE ts.theater.theaterId = :theaterId GROUP BY ts.floor")
    List<TheaterSeat> findByTheaterIdGroupByFloor(@Param("theaterId") Long theaterId);

    // 층별 섹션 정보 조회
    @Query(value =
            "SELECT ts FROM TheaterSeat ts " +
                    "WHERE ts.theater.theaterId = :theaterId " +
                    "AND ts.floor = :floor " +
                    "GROUP BY ts.section"
    )
    List<TheaterSeat> findByTheaterIdAndFloorGroupBySection(
            @Param("theaterId") Long theaterId,
            @Param("floor") FloorEnum floor
    );

    // 층간 섹션별 열 정보 조회
    @Query(value =
            "SELECT ts FROM TheaterSeat ts " +
                    "WHERE ts.theater.theaterId = :theaterId " +
                    "AND ts.floor = :floor " +
                    "AND ts.section = :section " +
                    "GROUP BY ts.seatRow"
    )
    List<TheaterSeat> findByTheaterIdAndFloorAndSectionGroupByRow(
            @Param("theaterId") Long theaterId,
            @Param("floor") FloorEnum floor,
            @Param("section") String section
    );

    // 층간 섹션별 열 정보 조회 섹션이 존재하지 않음
    @Query(value =
            "SELECT ts FROM TheaterSeat ts " +
                    "WHERE ts.theater.theaterId = :theaterId " +
                    "AND ts.floor = :floor " +
                    "AND ts.section IS NULL " +
                    "GROUP BY ts.seatRow"
    )
    List<TheaterSeat> findByTheaterIdAndFloorAndSectionGroupByRowWithoutSection(
            @Param("theaterId") Long theaterId,
            @Param("floor") FloorEnum floor
    );

    // 층, 섹션, 열, 좌석 정보로 조회
    Optional<TheaterSeat> findByFloorAndSectionAndSeatRowAndSeat(FloorEnum floor, String section, String seatRow, Integer Seat);
}
