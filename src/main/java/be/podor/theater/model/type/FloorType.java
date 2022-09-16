package be.podor.theater.model.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FloorType {
    FIRST("1층", 1),
    SECOND("2층", 2),
    THIRD("3층", 3),
    BALCONY("발코니", 4);

    private final String floor;
    private final int num;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FloorType from(String floor) {
        for (FloorType floorType : FloorType.values()) {
            if (floorType.getFloor().equals(floor)) {
                return floorType;
            }
        }
        return null;
    }
}
