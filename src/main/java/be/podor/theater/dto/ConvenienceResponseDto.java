package be.podor.theater.dto;

import be.podor.theater.model.type.ConvenienceCode;
import be.podor.theater.model.type.ConvenienceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConvenienceResponseDto {
    private String value;
    private ConvenienceType type;

    public static ConvenienceResponseDto of(ConvenienceCode code) {
        return new ConvenienceResponseDto(code.getValue(), code.getType());
    }
}
