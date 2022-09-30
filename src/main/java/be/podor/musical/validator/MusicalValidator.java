package be.podor.musical.validator;

import be.podor.exception.podor.PodoalException;
import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;

public class MusicalValidator {

    public static Musical validate(MusicalRepository musicalRepository, Long musicalId) {
        return musicalRepository.findById(musicalId)
                .orElseThrow(() -> PodoalException.NO_MUSICAL_EXCEPTION);
    }
}
