package com.fisherpaykel.model.experian;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fisherpaykel.generated.experian.ws.Address;
import com.fisherpaykel.generated.experian.ws.AddressLineType;

public class SelectedAddress {

    private String flatUnitNo;
    private String addressLine1;
    private String addressLine2;
    private String postCode;
    private String city;
    private String state;

    @JsonIgnore
    private static Set<String> postCodeLabels = new HashSet<>(Arrays.asList("ZIP/Postal Code", "PAF Postcode"));
    @JsonIgnore
    private static Set<String> cityLabels = new HashSet<>(Arrays.asList("City", "PAF Locality"));
    @JsonIgnore
    private static Set<String> stateLabels = new HashSet<>(Arrays.asList("State/Province", "State code"));
    @JsonIgnore
    BiFunction<String, List<Map<String, String>>, String> restFieldSelector = (s, addressFields) -> {
        Optional<Map<String, String>> any = addressFields.stream().filter(map -> map.containsKey(s)).findAny();
        if (any.isPresent()) {
            return StringUtils.isEmpty(any.get().get(s)) ? null : any.get().get(s);
        }
        return null;
    };
    @JsonIgnore
    BiFunction<Set<String>, List<AddressLineType>, String> soapFieldSelector = (labels, addressLines) -> {
        Optional<AddressLineType> any = addressLines.stream().filter(line -> labels.contains(line.getLabel())).findAny();
        if (any.isPresent()) {
            return StringUtils.isEmpty(any.get().getLine()) ? null : any.get().getLine();
        }
        return null;
    };

    public SelectedAddress(Address address) {
        if (isAddressLine(address, 0)) {
            addressLine1 = address.getQAAddress().getAddressLine().get(0).getLine();
        }
        if (isAddressLine(address, 1)) {
            addressLine2 = address.getQAAddress().getAddressLine().get(1).getLine();
        }

        this.state = soapFieldSelector.apply(stateLabels, address.getQAAddress().getAddressLine());
        this.city = soapFieldSelector.apply(cityLabels, address.getQAAddress().getAddressLine());
        this.postCode = soapFieldSelector.apply(postCodeLabels, address.getQAAddress().getAddressLine());

    }

    private boolean isAddressLine(Address address, int i) {
        return address.getQAAddress().getAddressLine().size() > i &&
                !StringUtils.isEmpty(address.getQAAddress().getAddressLine().get(i).getLine()) &&
                StringUtils.isEmpty(address.getQAAddress().getAddressLine().get(i).getLabel());
    }


    public SelectedAddress(AddressSelectionResponse address, AddressSelectionRequest request) {

        this.addressLine1 = restFieldSelector.apply("addressLine1", address.getAddress());
        this.addressLine2 = restFieldSelector.apply("addressLine2", address.getAddress());
        this.city = restFieldSelector.apply("locality", address.getAddress());
        this.postCode = restFieldSelector.apply("postalCode", address.getAddress());
        this.state = restFieldSelector.apply("province", address.getAddress());
        if ("US".equalsIgnoreCase(request.getCountry())) {
            String[] split = this.postCode.split("-");
            this.postCode = split[0];
        }
    }

    public String getFlatUnitNo() {
        return flatUnitNo;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

}
