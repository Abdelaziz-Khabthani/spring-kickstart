package com.abdelaziz.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.entity.User;

@Loggable(layer = ApplicationLayer.REPOSITORY_LAYER)
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	Optional<User> findOneByActivationKey(String activationKey);
	
	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);
	
	Optional<User> findOneByResetKey(String resetKey);

	Optional<User> findOneByEmailIgnoreCase(String email);

	Optional<User> findOneByLogin(String login);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesById(Long id);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByLogin(String login);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmail(String email);

	Page<User> findAllByLoginNot(Pageable pageable, String login);
}
