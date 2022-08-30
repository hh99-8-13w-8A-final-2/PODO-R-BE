package be.podor.theater.controller;

import be.podor.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

}
