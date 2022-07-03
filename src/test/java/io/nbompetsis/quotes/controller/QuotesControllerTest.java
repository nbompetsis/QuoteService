package io.nbompetsis.quotes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nbompetsis.quotes.controller.dto.QuoteRequestDTO;
import io.nbompetsis.quotes.exception.QuoteNotFoundException;
import io.nbompetsis.quotes.model.Quote;
import io.nbompetsis.quotes.service.QuoteService;

@WebMvcTest(QuotesController.class)
public class QuotesControllerTest {

	@MockBean
	private QuoteService service;

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper om = new ObjectMapper();

	@Test
	public void createQuote() throws Exception {
		QuoteRequestDTO request = new QuoteRequestDTO("Oscar Wilde", "The truth is rarely pure and never simple.");
		Quote quote = new Quote(1L, request.getAuthor(), request.getText());

		Mockito.when(service.createQuote(request.getAuthor(), request.getText())).thenReturn(quote);

		mockMvc.perform(post("/quote/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsBytes(request)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.author").value("Oscar Wilde"))
				.andExpect(jsonPath("$.text").value("The truth is rarely pure and never simple."));
	}
	
	@Test
	public void updateQuote() throws Exception {
		QuoteRequestDTO request = new QuoteRequestDTO("OSCAR", "UPDATED Quote.");
		Quote quote = new Quote(1L, "OSCAR", "UPDATED Quote.");

		Mockito.when(service.updateQuote(quote)).thenReturn(quote);

		mockMvc.perform(put("/quote/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsBytes(request)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.author").value("OSCAR"))
				.andExpect(jsonPath("$.text").value("UPDATED Quote."));
	}
	
	@Test
	public void shouldNotUpdateQuoteNotFound() throws Exception {
		QuoteRequestDTO request = new QuoteRequestDTO("OSCAR", "UPDATED Quote.");
		Quote quote = new Quote(1L, "OSCAR", "UPDATED Quote.");

		Mockito.when(service.updateQuote(quote)).thenThrow(new QuoteNotFoundException());

		mockMvc.perform(put("/quote/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsBytes(request)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getQuote() throws Exception {
		Quote quote = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");

		Mockito.when(service.getQuote(1L)).thenReturn(quote);

		mockMvc.perform(get("/quote/1")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.author").value("Oscar Wilde"))
				.andExpect(jsonPath("$.text").value("The truth is rarely pure and never simple."));
	}

	@Test
	public void quoteNotFound() throws Exception {
		Mockito.when(service.getQuote(1L)).thenThrow(new QuoteNotFoundException());

		mockMvc.perform(get("/quote/1")).andExpect(status().isNotFound());
	}

	@Test
	public void getARandomQuote() throws Exception {
		Quote quote = new Quote(10L, "Nikos Bompetsis", "Life is good :)");

		Mockito.when(service.randomQuote()).thenReturn(quote);

		mockMvc.perform(get("/quote/random")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.id").value(10))
				.andExpect(jsonPath("$.author").value("Nikos Bompetsis"))
				.andExpect(jsonPath("$.text").value("Life is good :)"));
	}
	
	@Test
	public void deleteQuote() throws Exception {
		mockMvc.perform(delete("/quote/1")).andExpect(status().isOk());
		
		Mockito.verify(service, Mockito.times(1)).deleteQuote(1L);
	}
	
	@Test
	public void getAllQuotes() throws Exception {
		Quote quote1 = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");
		Quote quote2 = new Quote(2L, "Nikos Bompetsis", "Life is good :)");
		Pageable pageable = PageRequest.of(0, 5);
		List<Quote> quotes = List.of(quote1, quote2);

		Mockito.when(service.allQuotes(pageable)).thenReturn(new PageImpl<>(quotes));

		mockMvc.perform(get("/quote/all")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.totalItems").value(2))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.currentPage").value(0))
				
				.andExpect(jsonPath("$.quotes[0].id").value(1))
				.andExpect(jsonPath("$.quotes[0].author").value("Oscar Wilde"))
				.andExpect(jsonPath("$.quotes[0].text").value("The truth is rarely pure and never simple."))
		
				.andExpect(jsonPath("$.quotes[1].id").value(2))
				.andExpect(jsonPath("$.quotes[1].author").value("Nikos Bompetsis"))
				.andExpect(jsonPath("$.quotes[1].text").value("Life is good :)"));
	}
	
	@Test
	public void discoverQuotesContainingSpecificText() throws Exception {
		Quote quote1 = new Quote(1L, "Oscar Wilde", "The truth is rarely pure and never simple.");
		Pageable pageable = PageRequest.of(0, 5);
		List<Quote> quotes = List.of(quote1);

		Mockito.when(service.discoverQuotes("truth", pageable)).thenReturn(new PageImpl<>(quotes));

		mockMvc.perform(get("/quote/discover/truth")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.totalItems").value(1))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.currentPage").value(0))
				
				.andExpect(jsonPath("$.quotes[0].id").value(1))
				.andExpect(jsonPath("$.quotes[0].author").value("Oscar Wilde"))
				.andExpect(jsonPath("$.quotes[0].text").value("The truth is rarely pure and never simple."));
	}
}
