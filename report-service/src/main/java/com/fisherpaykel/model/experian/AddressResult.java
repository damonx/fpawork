package com.fisherpaykel.model.experian;

public class AddressResult {

    private String identifier;
    private String partialAddress;
    private AddressSource source;

    public AddressResult(String identifier, String partialAddress, AddressSource source) {
        this.identifier = identifier;
        this.partialAddress = partialAddress;
        this.source = source;
    }


    public AddressSource getSource() { return source; }

    public String getIdentifier() {
        return identifier;
    }

    public String getPartialAddress() {
        return partialAddress;
    }
}
