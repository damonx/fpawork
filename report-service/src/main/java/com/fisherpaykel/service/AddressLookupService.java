/*
 * Copyright (c) Fisher and Paykel Appliances.
 *
 * This document is copyright. Except for the purpose of fair reviewing, no part
 * of this publication may be reproduced or transmitted in any form or by any
 * means, electronic or mechanical, including photocopying, recording, or any
 * information storage and retrieval system, without permission in writing from
 * the publisher. Infringers of copyright render themselves liable for
 * prosecution.
 */
package com.fisherpaykel.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fisherpaykel.common.util.FPAConstants;
import com.fisherpaykel.generated.experian.ws.QASearchResult;
import com.fisherpaykel.model.experian.AddressLookupResponse;
import com.fisherpaykel.model.experian.AddressResult;
import com.fisherpaykel.model.experian.AddressSource;
import com.fisherpaykel.model.experian.RESTClient;
import com.fisherpaykel.model.experian.SOAPClient;

@Component
public class AddressLookupService {

	@Autowired
	private SOAPClient soapClient;
	@Autowired
	private RESTClient restClient;

	private static final Map<String, String> countryMapping = new HashMap<>();
	private static final Set<String> countriesUsingRest = new HashSet<>();
	private static final Set<String> countriesUsingSoap = new HashSet<>();
	private static final String UK_POSTCODE_RESPONSE_PART_TWO = "...";
	
	static {
		countryMapping.put("NZ", "NZD");
		countryMapping.put("AU", "AUE");
		countryMapping.put("US", "USA");
		countryMapping.put("UK", "GBR");

	    countriesUsingRest.addAll(Arrays.asList("US", "UK"));
		countriesUsingSoap.addAll(Arrays.asList("NZ", "AU"));
	}

	public AddressLookupService() {
	}

	/**
	 * Looks up the address via REST/SOAP web service provided by EXPERIAN.
	 * @param country - country code	
	 * @param q - address query string
	 * @param postcode - optional parameter used as a filter on the result
	 * @return
	 */
	public List<AddressResult> lookupAddress(final String country, final String q, final String postcode) {
		final List<AddressResult> emptyResults = Collections.emptyList();
		if (StringUtils.isEmpty(q)) {
			return emptyResults;
		}
		String countryCode = countryMapping.get(country);
		if (countriesUsingRest.contains(country)) {
			final AddressLookupResponse addressLookupResponse = this.restClient.lookupAddress(countryCode, q);
			if (addressLookupResponse != null) {
				final Stream<AddressResult> addressStream = addressLookupResponse.getResults().stream()
						.map(e -> new AddressResult(e.getFormat(), e.getSuggestion(), AddressSource.REST));
				return fitlerPostcode(postcode, addressStream, countryCode);

			}
		} else if (countriesUsingSoap.contains(country)) {
			final QASearchResult qaSearchResult = this.soapClient.doSearch(countryCode, q);
			if (qaSearchResult != null &&
					qaSearchResult.getQAPicklist() != null && qaSearchResult.getQAPicklist() != null &&
					qaSearchResult.getQAPicklist().getPicklistEntry() != null &&
					BigInteger.ZERO.compareTo(qaSearchResult.getQAPicklist().getTotal()) == -1) {
				final Stream<AddressResult> addressStream = qaSearchResult.getQAPicklist().getPicklistEntry().stream()
						.map(e -> new AddressResult(e.getMoniker(), e.getPartialAddress(), AddressSource.SOAP));
				return fitlerPostcode(postcode, addressStream, countryCode);
			}
		}
		return emptyResults;
	}

	private List<AddressResult> fitlerPostcode(final String postcode, final Stream<AddressResult> addressStream, final String countryCode) {
		if (!StringUtils.isEmpty(postcode)) {
			return addressStream.filter(address -> {
				String postcodeToBeFiltered = postcode;
				//Added this ugly fix for the ugly data returned from EXPERIAN for UK postcode.
				if("GBR".equalsIgnoreCase(countryCode)) {
					final String postcodePartOne = postcode.split(FPAConstants.EMPTY_STRING, 2)[0];
					postcodeToBeFiltered = String.join(FPAConstants.EMPTY_STRING, postcodePartOne, UK_POSTCODE_RESPONSE_PART_TWO);
				}
				return StringUtils.endsWithIgnoreCase(address.getPartialAddress(), postcodeToBeFiltered);}).collect(Collectors.toList());
		} else {
			return addressStream.collect(Collectors.toList());
		}
	}

	public void setSoapClient(final SOAPClient soapClient) {
		this.soapClient = soapClient;
	}

	public void setRestClient(final RESTClient restClient) {
		this.restClient = restClient;
	}

}
