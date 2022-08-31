package be.podor.theater.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponseDto {
    private String section;
    private List<String> rows;
}
