package be.podor.theater.service;

import be.podor.theater.dto.TheaterResponseDto;
import be.podor.theater.dto.seat.FloorResponseDto;
import be.podor.theater.dto.seat.SectionResponseDto;
import be.podor.theater.model.type.FloorType;
import be.podor.theater.model.Theater;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.repository.TheaterRepository;
import be.podor.theater.repository.TheaterSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterSeatRepository theaterSeatRepository;

    //극장 정보 조회
    @Transactional(readOnly = true)
    public TheaterResponseDto findTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 극장입니다.")
        );

        TheaterResponseDto responseDto = TheaterResponseDto.of(theater);

        return responseDto;
    }

    // Todo refactoring
    //극장별 좌석 정보 조회
    @Transactional(readOnly = true)
    @Cacheable(value = "seats", key = "#theaterId")
    public List<FloorResponseDto> getTheaterSeats(Long theaterId) {

        // 층별 정보 ResponseDto
        List<FloorResponseDto> floorResponseDtos = new ArrayList<>();

        // 층별 정보 조회
        List<FloorType> floors = theaterSeatRepository.findFloorEnumsByTheaterIdGroupByFloor(theaterId).stream()
                .sorted()
                .collect(Collectors.toList());

        // 층간 섹션 정보 조회
        for (FloorType floor : floors) {
            List<String> sections = theaterSeatRepository.findSectionsByTheaterIdAndFloorGroupBySection(theaterId, floor);

            // 섹션별 정보 ResponseDto
            List<SectionResponseDto> sectionResponseDtos = new ArrayList<>();

            for (String section : sections) {
                List<String> rows;
                // 섹션별 열 정보 조회
                if (section == null) {
                    rows = theaterSeatRepository.findRowsByTheaterIdAndFloorAndSectionGroupByRowWithoutSection(theaterId, floor);
                } else {
                    rows = theaterSeatRepository.findRowsByTheaterIdAndFloorAndSectionGroupByRow(theaterId, floor, section);
                }
                sectionResponseDtos.add(new SectionResponseDto(section, rows));
            }

            floorResponseDtos.add(new FloorResponseDto(floor.getFloor(), sectionResponseDtos));
        }

        return floorResponseDtos;
    }
}
