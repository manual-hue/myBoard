package org.zerock.sb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member m where m.mid = :mid")
    @EntityGraph(attributePaths = "roleSet") // 어떤값을 로딩할지 필요시마다 지정 가능(패치 조인보다 효과적)
        //LAZY 로딩 시에도 자동적으로 left outer join이 걸린다=>Entity Graph를 썼기 때문!
        //그러나 우선적으로 join이 걸린 상태에서 처리가 되기 때문에 limit 없이 처리가 된다.
    Optional<Member> getMemberEager(String mid);

}