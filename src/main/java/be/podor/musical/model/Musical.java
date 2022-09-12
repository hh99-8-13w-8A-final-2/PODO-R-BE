package be.podor.musical.model;

import be.podor.theater.model.Theater;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Musical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicalId;

    @Column(nullable = false)
    private String musicalName;

    @Column(nullable = false)
    private String musicalPoster;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private LocalDate closeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_Id")
    private Theater theater;
}
