package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"diary", "member"})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오토 인크리먼트
    public Long fno;

    // 엘리먼트컬렉션, 원투매니, 매니투원 등등의 관계를 걸어주지 않으면 에러가 난다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int score;

    @CreationTimestamp
    private LocalDateTime regDate; // 언제 기록되었는지에 대한 정보

}
