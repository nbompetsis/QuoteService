package io.nbompetsis.quotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.nbompetsis.quotes.exception.QuoteNotFoundException;
import io.nbompetsis.quotes.model.Quote;
import io.nbompetsis.quotes.repository.QuoteRepository;

@Component
public class QuoteServiceImpl implements QuoteService {
	
	private final QuoteRepository repository;

	@Autowired
	public QuoteServiceImpl(QuoteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Quote createQuote(String author, String text) {
		Quote quote = new Quote(author, text);
		return repository.save(quote);
	}

	@Override
	public Quote getQuote(Long id) {
		return repository.findById(id)
			.orElseThrow(() -> new QuoteNotFoundException(String.format("Quote with id %d not found", id)));
	}

	@Override
	public Quote updateQuote(Quote quote) {
		Quote storedQuote = repository.findById(quote.getId())
			.orElseThrow(() -> new QuoteNotFoundException(String.format("Quote with id %d not found", quote.getId())));
		storedQuote.update(quote);
		return repository.save(storedQuote);
	}

	@Override
	public void deleteQuote(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new QuoteNotFoundException(String.format("Quote with id %d not found", id));
		}
	}

	@Override
	public Quote randomQuote() {
		return repository.randomQuote();
	}

	@Override
	public Page<Quote> allQuotes(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	@Override
	public Page<Quote> discoverQuotes(String text, Pageable pageable) {
		return repository.findByTextContaining(text, pageable);
	}
}
