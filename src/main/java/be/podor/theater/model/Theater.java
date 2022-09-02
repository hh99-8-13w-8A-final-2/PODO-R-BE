package be.podor.theater.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    @Column(nullable = false)
    private String theaterName;

    @Column
    private String theaterTel;

    @Column
    private String theaterUrl;

    @Column(nullable = false)
    private String theaterAddr;

    // 위도
    @Column(nullable = false)
    private Double la;

    // 경도
    @Column(nullable = false)
    private Double lo;

    // 편의시설
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TheaterConvenience> theaterConveniences;
}
