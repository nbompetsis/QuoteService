package io.nbompetsis.quotes.service;

import io.nbompetsis.quotes.model.Quote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuoteService {
	Quote getQuote(Long id);

	Quote createQuote(String author, String text);
	
	Quote updateQuote(Quote quote);
	
	void deleteQuote(Long id);

	Quote randomQuote();
	
	Page<Quote> allQuotes(Pageable pageable);
	
	Page<Quote> discoverQuotes(String text, Pageable pageable);	
}
