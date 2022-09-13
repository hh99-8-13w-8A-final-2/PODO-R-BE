package be.podor.musical.model;

import be.podor.review.model.Review;
import be.podor.theater.model.Theater;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "musical", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
