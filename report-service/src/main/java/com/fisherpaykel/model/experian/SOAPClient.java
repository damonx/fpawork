package com.fisherpaykel.model.experian;

import java.net.Authenticator;
import java.net.ProxySelector;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.springframework.stereotype.Component;

import com.fisherpaykel.generated.experian.ws.Address;
import com.fisherpaykel.generated.experian.ws.EngineEnumType;
import com.fisherpaykel.generated.experian.ws.EngineType;
import com.fisherpaykel.generated.experian.ws.QACanSearch;
import com.fisherpaykel.generated.experian.ws.QAGetAddress;
import com.fisherpaykel.generated.experian.ws.QAPortType;
import com.fisherpaykel.generated.experian.ws.QASOnDemandIntermediary;
import com.fisherpaykel.generated.experian.ws.QASearch;
import com.fisherpaykel.generated.experian.ws.QASearchOk;
import com.fisherpaykel.generated.experian.ws.QASearchResult;

@Component
public class SOAPClient {

	private final Map<String, Object> requestHeaders;
	private final EngineType engineType;

	public SOAPClient() {
		if ("true".equals(System.getProperty("use.proxy.for.client.connections"))) {
			ProxySelector.setDefault(new FPProxySelector());
			Authenticator.setDefault(new FPAuthenticator());
		}

		this.requestHeaders = new HashMap<>();
		this.requestHeaders.put("auth-token", Arrays.asList(System.getProperty("experion.auth.token")));

		this.engineType = new EngineType();
		this.engineType.setValue(EngineEnumType.INTUITIVE);
	}

	private QAPortType getQaPortType() {
		final QAPortType qaPortType = new QASOnDemandIntermediary().getQAPortType();
		((BindingProvider) qaPortType).getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, this.requestHeaders);
		return qaPortType;
	}

	public QASearchOk doCanSearch() {
		final QACanSearch canSearch = new QACanSearch();
		canSearch.setCountry("AUS");
		canSearch.setEngine(this.engineType);
		return getQaPortType().doCanSearch(canSearch);
	}

	public QASearchResult doSearch(final String dataSet, final String query) {
		final QASearch body = new QASearch();
		body.setCountry(dataSet);
		body.setEngine(this.engineType);
		body.setSearch(query);
		return getQaPortType().doSearch(body);
	}

	public Address doGetAddress(final String moniker) {
		final QAGetAddress body = new QAGetAddress();
		body.setMoniker(moniker);
		body.setLayout("SalesforceDefault");
		return getQaPortType().doGetAddress(body);
	}


}
