package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member {

    @Id
    //소셜로그인의 등장->한 사람이 여러개 이메일을 가짐, mid가 이메일일 가능성을 둔다.
    private String mid;

    private String mpw;

    private String mname;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet;

    public void changePassword(String password){
        this.mpw = password; // 패스워드로 변경하는 기능 메서드
    }

}

