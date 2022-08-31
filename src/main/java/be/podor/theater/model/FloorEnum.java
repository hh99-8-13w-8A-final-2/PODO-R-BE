package be.podor.theater.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FloorEnum {
    FIRST("1F", 1),
    SECOND("2F", 2),
    THIRD("3F", 3);

    private final String floor;
    private final int num;
}
