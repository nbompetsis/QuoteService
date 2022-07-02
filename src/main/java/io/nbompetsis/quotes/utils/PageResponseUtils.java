package io.nbompetsis.quotes.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import io.nbompetsis.quotes.controller.dto.QuoteResponseDTO;
import io.nbompetsis.quotes.model.Quote;

public class PageResponseUtils {
	
	private PageResponseUtils() {
	}
	
	public static Map<String, Object> getResponse (Page<Quote> pageQuote, List<QuoteResponseDTO> quotesDTO){
		
		Map<String, Object> response = new HashMap<>();

		response.put("quotes", quotesDTO);
		response.put("currentPage", pageQuote.getNumber());
		response.put("totalItems", pageQuote.getTotalElements());
		response.put("totalPages", pageQuote.getTotalPages());
		
		return response;
	}

}
