package be.podor.reviewheart.service;

import be.podor.reviewheart.repository.ReviewHeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewHeartService {

    private final ReviewHeartRepository reviewHeartRepository;

}
