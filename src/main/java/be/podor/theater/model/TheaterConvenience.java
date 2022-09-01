package be.podor.theater.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TheaterConvenience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long convenienceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConvenienceCode convenience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
}
