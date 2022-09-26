package be.podor.member.model;

import be.podor.share.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSearch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSearchId;

    @Column(nullable = false)
    private String search;

    public static MemberSearch empty() {
        return new MemberSearch(null, "");
    }

    public void updateSearch(String search) {
        this.search = search;
    }
}
