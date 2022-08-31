package be.podor.review.model;

import be.podor.musical.model.Musical;
import be.podor.review.model.reviewInfo.BriefTag;
import be.podor.theater.model.TheaterSeat;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String content;

    @Column
    private String imgUrl;

    @Embedded
    @Column(nullable = false)
    private BriefTag briefTag;

    @Column
    private Boolean operaGlass;

    @Column
    private Boolean block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musical_id")
    private Musical musical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private TheaterSeat theaterSeat;
}
