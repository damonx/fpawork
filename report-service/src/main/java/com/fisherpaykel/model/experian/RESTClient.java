package com.fisherpaykel.model.experian;

import java.net.Authenticator;
import java.net.ProxySelector;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
public class RESTClient {

	private final Client client;

	public RESTClient() {
		if ("true".equalsIgnoreCase(System.getProperty("use.proxy.for.client.connections"))) {
			ProxySelector.setDefault(new FPProxySelector());
			Authenticator.setDefault(new FPAuthenticator());
		}
		this.client = ClientBuilder.newClient();

	}

	public AddressLookupResponse lookupAddress(final String dataSet, final String query) {
		final Invocation.Builder request = this.client.target("https://api.edq.com")
				.path("capture/address/v2/search")
				.queryParam("auth-token", System.getProperty("experion.auth.token"))
				.queryParam("query", query)
				.queryParam("country", dataSet).request(MediaType.APPLICATION_JSON);

		return request.get(AddressLookupResponse.class);
	}

	public AddressSelectionResponse selectAddress(final String formatProvider) {
		final Invocation.Builder request = this.client.target(formatProvider)
				.queryParam("auth-token", System.getProperty("experion.auth.token"))
				.request(MediaType.APPLICATION_JSON);

		return request.get(AddressSelectionResponse.class);
	}
}
