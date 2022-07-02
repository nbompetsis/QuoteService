package io.nbompetsis.quotes.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteRequestDTO {
	
	@Size(max = 50, message = "Author must be 50 characters long") 
	private String author;
	
	@NotEmpty(message = "Text may not be empty")
	@Size(min = 2, max = 255, message = "Text must be between 1 and 255 characters long") 
    private String text;

	public QuoteRequestDTO() {}
	
	public QuoteRequestDTO(String author, String text) {
		this.author = author;
		this.text = text;
	}	
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
