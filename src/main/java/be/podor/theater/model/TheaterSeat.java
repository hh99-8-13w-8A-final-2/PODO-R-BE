package be.podor.theater.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TheaterSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FloorEnum floor;

    @Column
    private String section;

    @Column
    private String seatRow;

    @Column(nullable = false)
    private Integer seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    public static TheaterSeat of(Theater theater, FloorEnum floor, String section, String row, Integer seat) {
        return TheaterSeat.builder()
                .theater(theater)
                .floor(floor)
                .section(section)
                .seatRow(row)
                .seat(seat)
                .build();
    }
}