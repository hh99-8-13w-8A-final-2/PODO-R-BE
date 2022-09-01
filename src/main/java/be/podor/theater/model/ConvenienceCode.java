package be.podor.theater.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConvenienceCode {
    CAFE("카페", ConvenienceType.CONVENIENCE),
    RESTAURANT("레스토랑", ConvenienceType.CONVENIENCE),

    DIS_PARK("주차장", ConvenienceType.DISABLED),
    DIS_BATH("화장실", ConvenienceType.DISABLED),
    DIS_SLOPE("경사로", ConvenienceType.DISABLED),
    DIS_ELEVATOR("전용엘리베이터", ConvenienceType.DISABLED),

    PARK("주차장", ConvenienceType.PARK);

    private final String value;
    private final ConvenienceType type;

    // Todo Enum에 전부 달린 이 코드 하나로 모으기
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ConvenienceCode from(String value) {
        for (ConvenienceCode convenienceCode : ConvenienceCode.values()) {
            if (convenienceCode.getValue().equals(value)) {
                return convenienceCode;
            }
        }
        return null;
    }
}