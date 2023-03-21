package com.oinzo.somoim.domain.user.repository;

import com.oinzo.somoim.common.type.SocialType;
import com.oinzo.somoim.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String providerId);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
