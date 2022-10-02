package be.podor.theater.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponseDto implements Serializable {
    private String section;
    private List<String> rows;
}
