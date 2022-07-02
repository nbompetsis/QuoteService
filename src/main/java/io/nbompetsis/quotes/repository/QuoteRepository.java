package io.nbompetsis.quotes.repository;

import io.nbompetsis.quotes.model.Quote;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<Quote, Long> {
	
	Optional<Quote> findById(Long id);
	
	// For a more efficient solution please read the following article
	// http://jan.kneschke.de/projects/mysql/order-by-rand/
	@Query(value = "SELECT id, author, text FROM QUOTE ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Quote randomQuote();
	
	Page<Quote> findAll(Pageable pageable);
	
	Page<Quote> findByTextContaining(String text, Pageable pageable);
}
