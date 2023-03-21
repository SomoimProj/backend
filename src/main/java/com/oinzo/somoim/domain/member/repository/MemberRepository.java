package com.oinzo.somoim.domain.member.repository;

import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.member.entity.Member;
import com.oinzo.somoim.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Boolean existsByUserAndClub(User user, Club club);

}
