package com.fisherpaykel.model.experian;

public class AddressSelectionRequest {

    String country;
    String identifier;
    AddressSource source;

    public String getCountry() {
        return country;
    }

    public String getIdentifier() {
        return identifier;
    }

    public AddressSource getSource() {
        return source;
    }

}
