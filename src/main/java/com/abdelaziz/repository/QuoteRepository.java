package com.abdelaziz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.entity.Quote;

@Loggable(layer = ApplicationLayer.REPOSITORY_LAYER)
@Repository
public interface QuoteRepository extends PagingAndSortingRepository<Quote, Long> {
	public Page<Quote> findAllByAuthorId(Pageable pageable, Long id);

	public Page<Quote> findAllByTagLabel(Pageable pageable, String label);
}