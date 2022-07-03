package io.nbompetsis.quotes.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.nbompetsis.quotes.exception.QuoteNotFoundException;
import io.nbompetsis.quotes.model.Quote;
import io.nbompetsis.quotes.repository.QuoteRepository;

@ExtendWith(SpringExtension.class)
class QuoteServiceTest {

	private QuoteService service;

	@MockBean
	private QuoteRepository repository;

	@BeforeEach
	public void setUp() {
		service = new QuoteServiceImpl(repository);
	}

	@Test
	public void createQuote() {
		Quote quote = new Quote("Oscar Wilde", "The truth is rarely pure and never simple.");

		Mockito.when(repository.save(quote)).thenReturn(quote);

		service.createQuote("Oscar Wilde", "The truth is rarely pure and never simple.");
	}

	@Test
	public void updateQuote() {
		Quote oldQuote = new Quote(1L, "OSCAR", "Old Quote.");
		Quote quote = new Quote(1L, "OSCAR", "Updated Quote.");

		Mockito.when(repository.findById(quote.getId())).thenReturn(Optional.of(oldQuote));
		Mockito.when(repository.save(quote)).thenReturn(quote);

		service.updateQuote(quote);
	}

	@Test
	public void throwsOnUpdateQuote() {
		Quote quote = new Quote(1L, "OSCAR", "Updated Quote.");

		Mockito.when(repository.findById(quote.getId()))
				.thenThrow(new QuoteNotFoundException(String.format("Quote with id %d not found", quote.getId())));

		Assertions.assertThrows(QuoteNotFoundException.class, () -> service.updateQuote(quote));

		Mockito.verify(repository, Mockito.times(0)).save(quote);

	}

	@Test
	public void getQuote() {
		Quote quote = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");

		Mockito.when(repository.findById(quote.getId())).thenReturn(Optional.of(quote));

		service.getQuote(quote.getId());
	}

	@Test
	public void throwsOnGetQuote() {
		Quote quote = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");

		Mockito.when(repository.findById(quote.getId()))
				.thenThrow(new QuoteNotFoundException(String.format("Quote with id %d not found", quote.getId())));

		Assertions.assertThrows(QuoteNotFoundException.class, () -> service.getQuote(quote.getId()));
	}

	@Test
	public void getARandomQuote() {
		Quote quote = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");

		Mockito.when(repository.randomQuote()).thenReturn(quote);

		service.randomQuote();
	}

	@Test
	public void deleteQuote() {
		service.deleteQuote(1L);

		Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
	}

	@Test
	public void throwsOnDeleteQuote() {

		Mockito.doThrow(new EmptyResultDataAccessException(0)).when(repository).deleteById(1L);

		Assertions.assertThrows(QuoteNotFoundException.class, () -> service.deleteQuote(1L));

		Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
	}

	@Test
	public void getAllQuotes() {
		Quote quote1 = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");
		Quote quote2 = new Quote(2L, "Nikos Bompetsis", "Life is good :)");
		Pageable pageable = PageRequest.of(0, 5);
		List<Quote> quotes = List.of(quote1, quote2);
		

		Mockito.when(repository.findAll(pageable)).thenReturn(new PageImpl<>(quotes));
		
		Page<Quote> returnedQuotes = service.allQuotes(pageable);
		
		assertThat(returnedQuotes.getTotalElements(), is(2L));
		assertThat(returnedQuotes.getContent().size(), is(2));
		assertThat(returnedQuotes.getContent().get(0).getId(), is(1L));
		assertThat(returnedQuotes.getContent().get(0).getAuthor(), is("Oscar Wilde"));
		assertThat(returnedQuotes.getContent().get(0).getText(), is("The truth is rarely pure and never simple."));
		
		assertThat(returnedQuotes.getContent().get(1).getId(), is(2L));
		assertThat(returnedQuotes.getContent().get(1).getAuthor(), is("Nikos Bompetsis"));
		assertThat(returnedQuotes.getContent().get(1).getText(), is("Life is good :)"));
	}
	
	@Test
	public void discoverQuotesContainingSpecificText() {
		Quote quote1 = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");
		Pageable pageable = PageRequest.of(0, 5);
		List<Quote> quotes = List.of(quote1);
		
		Mockito.when(repository.findByTextContaining("truth", pageable)).thenReturn(new PageImpl<>(quotes));
		
		Page<Quote> returnedQuotes = service.discoverQuotes("truth", pageable);
		
		assertThat(returnedQuotes.getTotalElements(), is(1L));
		assertThat(returnedQuotes.getContent().size(), is(1));
		assertThat(returnedQuotes.getContent().get(0).getId(), is(1L));
		assertThat(returnedQuotes.getContent().get(0).getAuthor(), is("Oscar Wilde"));
		assertThat(returnedQuotes.getContent().get(0).getText(), is("The truth is rarely pure and never simple."));
		
	}

}
