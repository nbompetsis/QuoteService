package io.nbompetsis.quotes.repository;

import io.nbompetsis.quotes.model.Quote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class QuoteRepositoryTest {

    @Autowired
    QuoteRepository quoteRepository;

    @Test
    public void shouldReturnEmpty() {
        Page<Quote> quotes = quoteRepository.findAll(PageRequest.of(0, 5));
        assertThat(quotes).isEmpty();
    }

    @Test
    public void shouldSaveQuote() {
        Quote quote = quoteRepository.save(new Quote("Me", "Life is good"));
        Page<Quote> quotes = quoteRepository.findAll(PageRequest.of(0, 5));
        assertThat(quotes).hasSize(1).contains(quote);
    }

    @Test
    public void shouldRetrieveQuote() {
        Quote quote = quoteRepository.save(new Quote("Me", "Life is good"));
        Optional<Quote> expectedQuote = quoteRepository.findById(quote.getId());
        assertThat(expectedQuote.orElseThrow()).isEqualTo(quote);
    }

    @Test
    public void shouldRetrieveRandomQuote() {
        quoteRepository.save(new Quote("Me", "Life is good"));
        quoteRepository.save(new Quote("You", "Music is my hobby"));
        Quote expectedQuote = quoteRepository.randomQuote();
        assertThat(expectedQuote).isNotNull();
    }

    @Test
    public void shouldFindSpecificQuotes() {
        quoteRepository.save(new Quote("Me", "Life is good"));
        quoteRepository.save(new Quote("You", "Music is my hobby"));
        quoteRepository.save(new Quote("Me", "Music is good"));

        Page<Quote> quotes = quoteRepository.findByTextContaining("Music", PageRequest.of(0, 5));
        assertThat(quotes).hasSize(2);
    }


}