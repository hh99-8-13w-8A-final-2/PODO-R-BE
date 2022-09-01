package be.podor.theater.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConvenienceType {
    CONVENIENCE("편의시설"),
    DISABLED("장애인시설"),
    PARK("주차시설");

    private final String value;

    // Todo Enum에 전부 달린 이 코드 하나로 모으기
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ConvenienceType from(String value) {
        for (ConvenienceType convenienceType : ConvenienceType.values()) {
            if (convenienceType.getValue().equals(value)) {
                return convenienceType;
            }
        }
        return null;
    }
}
