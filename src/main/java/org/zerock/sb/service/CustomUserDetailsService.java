package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.MemberDTO;
import org.zerock.sb.entity.Member;
import org.zerock.sb.repository.MemberRepository;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("-----------------------------------------");
        log.info("----------------------loadUserByUsername-------------------"+username);


        Optional<Member> optionalMember = memberRepository.getMemberEager(username);
        //LAZY 로딩 시에도 자동적으로 left outer join이 걸린다=>Entity Graph를 썼기 때문!
        //일괄 조인 처리가 되기 때문에 종속된 값 객체가 많은 경우에 유용, 쓰레기 코드를 줄인다 ex) 다이어리

        Member member = optionalMember.orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

        log.info("Member: " + member);

        MemberDTO memberDTO = MemberDTO.builder()
                .mid(member.getMid())
                .mpw(member.getMpw())
                .mname(member.getMname())
                .roles(member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toSet()))
                .build();

        log.info(memberDTO);

        log.info("-------------------------------------------------");
        log.info("-------------------------------------------------");

        return memberDTO;

    }
}
