package be.podor.musical.validator;

import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;

public class MusicalValidator {

    public static Musical validate(MusicalRepository musicalRepository, Long musicalId) {
        return musicalRepository.findById(musicalId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 뮤지컬입니다.")
        );
    }
}
