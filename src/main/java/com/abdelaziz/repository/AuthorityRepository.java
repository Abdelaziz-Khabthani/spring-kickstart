package com.abdelaziz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.entity.Authority;

@Loggable(layer = ApplicationLayer.REPOSITORY_LAYER)
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
