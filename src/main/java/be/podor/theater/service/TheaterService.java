package be.podor.theater.service;

import be.podor.theater.dto.TheaterResponseDto;
import be.podor.theater.dto.seat.FloorResponseDto;
import be.podor.theater.dto.seat.SectionResponseDto;
import be.podor.theater.model.FloorEnum;
import be.podor.theater.model.Theater;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.repository.TheaterRepository;
import be.podor.theater.repository.TheaterSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterSeatRepository theaterSeatRepository;

    //극장 정보 조회
    public TheaterResponseDto findTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 극장입니다.")
        );

        TheaterResponseDto responseDto = TheaterResponseDto.of(theater);

        return responseDto;
    }

    // Todo refactoring
    //극장별 좌석 정보 조회
    public List<FloorResponseDto> getTheaterSeats(Long theaterId) {

        // 층별 정보 ResponseDto
        List<FloorResponseDto> floorResponseDtos = new ArrayList<>();

        // 층별 정보 조회
        List<TheaterSeat> theaterFloors = theaterSeatRepository.findByTheaterIdGroupByFloor(theaterId);
        List<FloorEnum> floors = theaterFloors.stream()
                .map(TheaterSeat::getFloor)
                .collect(Collectors.toList());

        // 층간 섹션 정보 조회
        for (FloorEnum floor : floors) {
            List<TheaterSeat> theaterSections = theaterSeatRepository.findByTheaterIdAndFloorGroupBySection(theaterId, floor);
            List<String> sections = theaterSections.stream()
                    .map(TheaterSeat::getSection)
                    .collect(Collectors.toList());

            // 섹션별 정보 ResponseDto
            List<SectionResponseDto> sectionResponseDtos = new ArrayList<>();

            for (String section : sections) {
                List<TheaterSeat> theaterRows;
                // 섹션별 열 정보 조회
                if (section == null) {
                    theaterRows = theaterSeatRepository.findByTheaterIdAndFloorAndSectionGroupByRowWithoutSection(theaterId, floor);
                } else {
                    theaterRows = theaterSeatRepository.findByTheaterIdAndFloorAndSectionGroupByRow(theaterId, floor, section);
                }
                List<String> rows = theaterRows.stream()
                        .map(TheaterSeat::getSeatRow)
                        .collect(Collectors.toList());

                sectionResponseDtos.add(new SectionResponseDto(section, rows));
            }

            floorResponseDtos.add(new FloorResponseDto(floor.getFloor(), sectionResponseDtos));
        }

        return floorResponseDtos;
    }
}
