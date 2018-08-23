package com.abdelaziz.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.entity.Tag;

@Loggable(layer = ApplicationLayer.REPOSITORY_LAYER)
@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

}