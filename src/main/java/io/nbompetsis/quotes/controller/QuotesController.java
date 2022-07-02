package io.nbompetsis.quotes.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.nbompetsis.quotes.controller.dto.QuoteRequestDTO;
import io.nbompetsis.quotes.controller.dto.QuoteResponseDTO;
import io.nbompetsis.quotes.model.Quote;
import io.nbompetsis.quotes.service.QuoteService;
import io.nbompetsis.quotes.utils.PageResponseUtils;

@Controller	
@RequestMapping(path="/quote") 
public class QuotesController {
	
	private final QuoteService service;
	
	@Autowired
	public QuotesController(QuoteService service) {
		this.service = service;
	}
	
	@PostMapping(path="/create")
	public @ResponseBody QuoteResponseDTO createQuote(@RequestBody QuoteRequestDTO dto) {
		return new QuoteResponseDTO(service.createQuote(dto.getAuthor(), dto.getText()));
	}
	
	@GetMapping(path="/{quoteId}")
	public @ResponseBody QuoteResponseDTO getQuoteById(@PathVariable Long quoteId) {
		return new QuoteResponseDTO(service.getQuote(quoteId));
	}

	@PutMapping(path="/{quoteId}")
	public @ResponseBody QuoteResponseDTO updateQuote(@PathVariable Long quoteId, @RequestBody QuoteRequestDTO dto) {
		Quote quote = new Quote(quoteId, dto.getAuthor(), dto.getText());
		return new QuoteResponseDTO(service.updateQuote(quote));
	}
	
	@DeleteMapping(path="/{quoteId}")
	public void deleteQuote(@PathVariable Long quoteId) {
		service.deleteQuote(quoteId);
	}
	
	@GetMapping(path="/random")
	public @ResponseBody QuoteResponseDTO randomQuote() {
		return new QuoteResponseDTO(service.randomQuote());
	}
	
	@GetMapping(path="/all")
	public ResponseEntity<Map<String, Object>> allQuotes(@RequestParam(defaultValue = "0") int page,
														 @RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Quote> pageQuote = service.allQuotes(pageable);
	    
		List<QuoteResponseDTO> quotesDTO = pageQuote.getContent().stream()
			.flatMap(Stream::of)
			.map(QuoteResponseDTO::new)
			.collect(Collectors.toList());
		
		return new ResponseEntity<>(PageResponseUtils.getResponse(pageQuote, quotesDTO), HttpStatus.OK);
	}
	
	@GetMapping(path="/discover/{text}")
	public ResponseEntity<Map<String, Object>> discoverQuotes(@PathVariable String text,
															  @RequestParam(defaultValue = "0") int page,
															  @RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Quote> pageQuote = service.discoverQuotes(text, pageable);
	    
		List<QuoteResponseDTO> quotesDTO = pageQuote.getContent().stream()
			.flatMap(Stream::of)
			.map(QuoteResponseDTO::new)
			.collect(Collectors.toList());
	
		
		return new ResponseEntity<>(PageResponseUtils.getResponse(pageQuote, quotesDTO), HttpStatus.OK);
	}
}
