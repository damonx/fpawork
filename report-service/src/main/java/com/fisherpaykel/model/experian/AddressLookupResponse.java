package com.fisherpaykel.model.experian;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressLookupResponse {

	int count;
	List<AddressSuggestion> results = new ArrayList<>();

	public int getCount() {
		return this.count;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public void setResults(final List<AddressSuggestion> results) {
		this.results = results;
	}

	public List<AddressSuggestion> getResults() {
		return this.results;
	}

}
