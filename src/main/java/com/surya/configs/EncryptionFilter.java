package com.surya.configs;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow.Publisher;

public class EncryptionFilter implements ExchangeFilterFunction {
	
	
	private final List<HttpMessageWriter<?>> messageWriters;

	public EncryptionFilter(List<HttpMessageWriter<?>> messageWriters) {
		super();
		this.messageWriters = messageWriters;
	}

	@Override
	public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
	    if (request.body() != null) {
	    	
	    	ClientRequest newRequest = ClientRequest.from(request)
	    			.headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
	    			.body(new BodyDecorator(request.body(), messageWriters))
	    			.build();
	    	
	    	
            return next.exchange(newRequest);
	    } else {
	        return next.exchange(request);
	    }
	}


	

}

class BodyDecorator implements BodyInserter<Object, ClientHttpRequest> {
	
	
	
	private final BodyInserter<?, ? super ClientHttpRequest> delegate;
	private final List<HttpMessageWriter<?>> messageWriters;

	public BodyDecorator(BodyInserter<?, ? super ClientHttpRequest> delegate,
			List<HttpMessageWriter<?>> messageWriters) {
		super();
		this.delegate = delegate;
		this.messageWriters = messageWriters;
	}

	@Override
	public Mono<Void> insert(ClientHttpRequest outputMessage, Context context) {
		
		ClientHttpRequestDecorator requestDecorator = new ClientHttpRequestDecorator(outputMessage) {

			@Override
			public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
				// TODO Auto-generated method stub

				Mono<DataBuffer> encryptedPayload = Mono.from(body).flatMap(dataBuffer -> {
					String rawReq = dataBuffer.toString();

					return Mono.fromCallable(() -> {
						byte[] encReq = CryptoUtil.encrypt(rawReq);
						DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
						System.out.println("bytesOut:" + new String(encReq));
						return dataBufferFactory.wrap(encReq);
					});

				});

				return super.writeWith(encryptedPayload);
			}
			
			@Override
			public HttpMethod getMethod() {
				return super.getMethod();
			}
			
		};
		
		return delegate.insert(requestDecorator, context);
	}
	
}

