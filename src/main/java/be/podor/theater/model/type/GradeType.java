package be.podor.theater.model.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GradeType {
    VIP, OP, R, S, A;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GradeType from(String grade) {
        for (GradeType gradeType : GradeType.values()) {
            if (gradeType.name().equals(grade)) {
                return gradeType;
            }
        }
        return null;
    }
}
