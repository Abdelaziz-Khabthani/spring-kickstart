package com.abdelaziz.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.dto.QuoteDto;
import com.abdelaziz.entity.Author;
import com.abdelaziz.entity.Quote;
import com.abdelaziz.entity.Tag;
import com.abdelaziz.repository.AuthorRepository;
import com.abdelaziz.repository.QuoteRepository;
import com.abdelaziz.service.QuoteService;

@Loggable(layer = ApplicationLayer.SERVICE_LAYER)
@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {

	@Autowired
	private QuoteRepository quoteRepository;
	
	@Autowired
	private AuthorRepository authorRepository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<QuoteDto> findQuoteById(Long id) {
		return quoteRepository.findById(id).map(quote -> modelMapper.map(quote, QuoteDto.class));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<QuoteDto> getAllQuotes(Pageable pageable) {
		return quoteRepository.findAll(pageable).map(quote -> modelMapper.map(quote, QuoteDto.class));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<QuoteDto> getAllQuotesByAuthorId(Pageable pageable, Long id) {
		return quoteRepository.findAllByAuthorId(pageable, id).map(quote -> modelMapper.map(quote, QuoteDto.class));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<QuoteDto> getAllQuotesByTagLabel(Pageable pageable, String label) {
		return quoteRepository.findAllByTagLabel(pageable, label).map(quote -> modelMapper.map(quote, QuoteDto.class));
	}

	@Override
	public Optional<QuoteDto> createQuote(QuoteDto quoteDto, Long authorId) {
		return authorRepository.findById(authorId).map(author -> {
			Quote quote = modelMapper.map(quoteDto, Quote.class);
			quote.setAuthor(author);
			Quote newQuote = quoteRepository.save(quote);
			return modelMapper.map(newQuote, QuoteDto.class);
		});
	}

	@Override
	public Boolean deleteQuoteById(Long id) {
		Optional<Quote> quoteToDelete = quoteRepository.findById(id);
		if (quoteToDelete.isPresent()) {
			quoteRepository.delete(quoteToDelete.get());
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Optional<QuoteDto> updateaQuote(QuoteDto quoteDto) {
		return Optional.of(quoteRepository.findById(quoteDto.getId())).filter(Optional::isPresent).map(Optional::get).map(quote -> {
			quote.setContent(quoteDto.getContent());
			Set<Tag> tags = quoteDto.getTags().stream().map(tagDto -> {
				return new Tag(tagDto);
			}).collect(Collectors.toSet());
			quote.setTags(tags);
			return modelMapper.map(quote, QuoteDto.class);
		});
	}

	@Override
	public Boolean replaceAuthorQuote(Long authorId, Long quoteId) {
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		Optional<Author> author = authorRepository.findById(authorId);
		if(quote.isPresent() && author.isPresent()) {
			quote.get().setAuthor(author.get());
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}