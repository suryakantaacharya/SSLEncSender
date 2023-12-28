package com.surya.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.surya.configs.EncryptionFilter;


@RestController
public class SenderController {

	
	@RequestMapping(value = "/send")
	public void sendEnc() {

	WebClient webClient = WebClient.builder()
	        .baseUrl("http://localhost:8080")
	        .filter(new EncryptionFilter())
	        .build();
	
	EncSender request = new EncSender();

	// Use the configured WebClient to make requests
	String response = webClient.post()
	        .uri("/decrypt")
	        .bodyValue(request)
	        .retrieve()
	        .bodyToMono(String.class)
	        .block();
	}
}


class EncSender{
	public String username = "Suryakanta Acharya";
}


