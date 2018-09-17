package com.abdelaziz.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.dto.QuoteDto;
import com.abdelaziz.exception.QuoteNotFoundException;
import com.abdelaziz.service.QuoteService;
import com.abdelaziz.util.PaginationUtil;

@Loggable(layer = ApplicationLayer.RESOURCE_LAYER)
@RestController
public class QuoteResource extends ApiRootPath {

	@Autowired
	private QuoteService quoteService;
	
	@Autowired
	private PaginationUtil paginationUtil;
	
	@GetMapping("/quotes")
	public ResponseEntity<List<QuoteDto>> getAllQuotes(Pageable pageable) {
		Page<QuoteDto> allQuotes = this.quoteService.getAllQuotes(pageable);
		HttpHeaders headers = this.paginationUtil.generatePaginationHttpHeaders(allQuotes, "/api/quotes");
		return new ResponseEntity<List<QuoteDto>>(allQuotes.getContent(), headers, HttpStatus.OK);
	}
	
	@GetMapping("/quotes/{id}")
	public QuoteDto getQuote(@PathVariable long quoteId) {
		Optional<QuoteDto> quote = this.quoteService.findQuoteById(quoteId);
		if (!quote.isPresent()) {
			throw new QuoteNotFoundException();
		}
		return quote.get();
	}
	
	@PutMapping("/quotes")
	public ResponseEntity<Void> updateQuote(@Valid @RequestBody QuoteDto quoteDto) {
		if (quoteDto.getId() == null) {
			throw new QuoteNotFoundException();
		}
		Optional<QuoteDto> quote = this.quoteService.updateaQuote(quoteDto);
		
		if (!quote.isPresent()) {
			throw new QuoteNotFoundException();
		}
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/quotes/{id}")
	public ResponseEntity<Void> deleteQuote(@PathVariable long quoteId) {
		if (quoteService.deleteQuoteById(quoteId)) {
			return ResponseEntity.noContent().build();
		} else {
			throw new QuoteNotFoundException();
		}
	}
	
	// You get the point.
	// I prefer the quote style less work in the resource layer, heay work in the service layer
}
