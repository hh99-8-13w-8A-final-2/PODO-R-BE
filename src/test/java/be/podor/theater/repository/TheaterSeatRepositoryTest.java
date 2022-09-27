package be.podor.theater.repository;

import be.podor.theater.model.type.FloorType;
import be.podor.theater.model.Theater;
import be.podor.theater.model.TheaterSeat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ComponentScan(basePackages = "be.podor.theater.repository")
@ActiveProfiles("h2")
@DataJpaTest
class TheaterSeatRepositoryTest {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private TheaterSeatRepository theaterSeatRepository;

    private Theater theater;

    @BeforeEach
    void setUp() {
        theater = theaterRepository.save(Theater.builder()
                .theaterName("테스트극장")
                .theaterAddr("서울특별시 강남구 역삼1동 논현로 425")
                .la(37.4979954)
                .lo(127.0379322)
                .theaterSeatImage("")
                .build()
        );

        List<TheaterSeat> theaterSeats = new ArrayList<>();

        // 1층, 2층 3층 각각 [A B C D] 섹션 / [1, 2] row / 2개 좌석
        for (FloorType floor : FloorType.values()) {
            for (char c = 'A'; c < 'A' + 4; c++) {
                for (int i = 1; i < 1 + 2; i++) {
                    theaterSeats.add(
                            TheaterSeat.of(
                                    theater,
                                    floor,
                                    String.valueOf(c),
                                    String.valueOf(i),
                                    (c - 'A') * 3 + i
                            )
                    );
                }
            }
        }

        theaterSeatRepository.saveAll(theaterSeats);
    }

    @Test
    @DisplayName("극장 아이디로 상영관 전체 층 조회")
    void findFloorEnumsByTheaterIdGroupByFloor() {
        //given
        Long theaterId = theater.getTheaterId();

        //when
        List<FloorType> floors = theaterSeatRepository.findFloorEnumsByTheaterIdGroupByFloor(theaterId);

        //then
        Assertions.assertThat(floors.size()).isEqualTo(FloorType.values().length);
    }

    @Test
    @DisplayName("극장 아이디와 층으로 층별 섹션 조회")
    void findSectionsByTheaterIdAndFloorGroupBySection() {
        //given
        Long theaterId = theater.getTheaterId();
        FloorType floor = FloorType.FIRST;

        //when
        List<String> sections = theaterSeatRepository.findSectionsByTheaterIdAndFloorGroupBySection(theaterId, floor);

        //then
        Assertions.assertThat(sections.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("극장 아이디, 층, 섹션으로 층, 섹션별 열 조회")
    void findRowsByTheaterIdAndFloorAndSectionGroupByRow() {
        //given
        Long theaterId = theater.getTheaterId();
        FloorType floor = FloorType.FIRST;
        String section = "A";

        //when
        List<String> sections = theaterSeatRepository.findRowsByTheaterIdAndFloorAndSectionGroupByRow(theaterId, floor, section);

        //then
        Assertions.assertThat(sections.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상영관, 층, 섹션, 열, 좌석 정보로 해당 극장에 해당 좌석이 존재하는지 조회")
    void findByFloorAndSectionAndSeatRowAndSeatAndTheater_TheaterId() {
        Long theaterId = theater.getTheaterId();
        FloorType floor = FloorType.FIRST;
        String section = "A";
        String row = "1";
        Integer seat = 1;

        //when
        Optional<TheaterSeat> theaterSeat = theaterSeatRepository.findByFloorAndSectionAndSeatRowAndSeatAndTheater_TheaterId(floor, section, row, seat, theaterId);

        //then
        Assertions.assertThat(theaterSeat).isPresent();
    }

    @AfterEach
    void tearDown() {
        System.out.println("afterEach");
        theaterSeatRepository.deleteAll();
        theaterRepository.deleteAll();
    }
}