package com.abdelaziz.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abdelaziz.dto.QuoteDto;

public interface QuoteService {

	public Optional<QuoteDto> findQuoteById(Long id);

	public Page<QuoteDto> getAllQuotes(Pageable pageable);

	public Page<QuoteDto> getAllQuotesByAuthorId(Pageable pageable, Long id);

	public Page<QuoteDto> getAllQuotesByTagLabel(Pageable pageable, String label);

	public Optional<QuoteDto> createQuote(QuoteDto quoteDto, Long authorId);

	public Boolean deleteQuoteById(Long id);

	public Optional<QuoteDto> updateaQuote(QuoteDto quoteDto);

	public Boolean replaceAuthorQuote(Long authorId, Long quoteId);

}
