package io.nbompetsis.quotes.controller.dto;

import io.nbompetsis.quotes.model.Quote;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteResponseDTO {
	private Long id;
	private String author;
	private String text;
	
	public QuoteResponseDTO(Quote q) {
		this.id = q.getId();
		this.author = q.getAuthor();
		this.text = q.getText();
	}
	
	public String getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}
	
	public Long getId() {
		return id;
	}
	
}
