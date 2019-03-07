package com.fisherpaykel.model.experian;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressSelectionResponse {

	List<Map<String, String>> address = new ArrayList<>();
	List<Map<String, String>> components = new ArrayList<>();

	public List<Map<String, String>> getAddress() {
		return this.address;
	}

	public void setAddress(final List<Map<String, String>> address) {
		this.address = address;
	}

	public List<Map<String, String>> getComponents() {
		return this.components;
	}

	public void setComponents(final List<Map<String, String>> components) {
		this.components = components;
	}

}
