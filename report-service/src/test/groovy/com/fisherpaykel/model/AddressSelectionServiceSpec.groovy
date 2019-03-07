package com.fisherpaykel.model

import com.fisherpaykel.generated.experian.ws.Address
import com.fisherpaykel.generated.experian.ws.AddressLineType
import com.fisherpaykel.generated.experian.ws.QAAddressType
import com.fisherpaykel.model.experian.AddressSelectionResponse
import com.fisherpaykel.model.experian.RESTClient
import com.fisherpaykel.model.experian.SOAPClient
import spock.lang.Specification

class AddressSelectionServiceSpec extends Specification {

    private AddressSelectionService selectionService;
    private SOAPClient soapClient
    private RESTClient restClient
    private Address soapResult =  new Address()
    private AddressSelectionResponse restResult = new AddressSelectionResponse()

    public void setup(){
        selectionService = new AddressSelectionService()
        soapClient = Mock(SOAPClient.class)
        restClient = Mock(RESTClient.class)
        def lineType = new AddressLineType()

        selectionService.setSoapClient(soapClient)
        selectionService.setRestClient(restClient)
    }

    def "test select nz address no unit" (){
        given:
        def type = new QAAddressType()
        type.addressLine.add(new AddressLineType( line: "54b Tawa road"))
        type.addressLine.add(new AddressLineType())
        type.addressLine.add(new AddressLineType( label: "City", line: "One Tree Hill"))
        type.addressLine.add(new AddressLineType( label: "State/Province", line: "Auckland"))
        type.addressLine.add(new AddressLineType( label: "ZIP/Postal Code", line: "1061"))
        soapResult.setQAAddress(type)

        1 * soapClient.doGetAddress(*_) >> soapResult
        0 * restClient.doGetAddress(*_)
        when:
        def res = selectionService.selectAddress(new AddressSelectionRequest( country: "NZ", source:AddressSource.SOAP))

        then:
        res.flatUnitNo == null
        res.addressLine1 == "54b Tawa road"
        res.addressLine2 == null
        res.postCode == "1061"
        res.city == "One Tree Hill"
        res.state == "Auckland"
    }

    def "test select nz address with unit" (){
        given:
        def type = new QAAddressType()
        type.addressLine.add(new AddressLineType( line: "7"))
        type.addressLine.add(new AddressLineType( line: "9 Fanshawe Street"))
        type.addressLine.add(new AddressLineType( label: "City", line: "Auckland Central"))
        type.addressLine.add(new AddressLineType( label: "State/Province", line: "Auckland"))
        type.addressLine.add(new AddressLineType( label: "ZIP/Postal Code", line: "1010"))
        soapResult.setQAAddress(type)

        1 * soapClient.doGetAddress(*_) >> soapResult
        0 * restClient.doGetAddress(*_)
        when:
        def res = selectionService.selectAddress(new AddressSelectionRequest( country: "NZ", source:AddressSource.SOAP))

        then:
        res.flatUnitNo == null
        res.addressLine1 == "7"
        res.addressLine2 == "9 Fanshawe Street"
        res.postCode == "1010"
        res.city == "Auckland Central"
        res.state == "Auckland"
    }

    def "test select australian address" () {
        given:
        def type = new QAAddressType()
        type.addressLine.add(new AddressLineType( line: "U 5  2 Tupia St"))
        type.addressLine.add(new AddressLineType())
        type.addressLine.add(new AddressLineType( label: "PAF Locality", line: "BOTANY"))
        type.addressLine.add(new AddressLineType( label: "State code", line: "NSW"))
        type.addressLine.add(new AddressLineType( label: "PAF Postcode", line: "2019"))
        type.addressLine.add(new AddressLineType( label: "Country", line: "AUSTRALIA"))
        soapResult.setQAAddress(type)

        1 * soapClient.doGetAddress(*_) >> soapResult
        0 * restClient.doGetAddress(*_)
        when:
        def res = selectionService.selectAddress(new AddressSelectionRequest( country: "AU", source:AddressSource.SOAP))

        then:
        res.flatUnitNo == null
        res.addressLine1 == "U 5  2 Tupia St"
        res.addressLine2 == null
        res.city == "BOTANY"
        res.postCode == "2019"
        res.state == "NSW"
    }

    def "test select UK address" (){
        given:
        def type = new QAAddressType()
        restResult.getAddress().add(['addressLine1':"110 Spring Street"])
        restResult.getAddress().add(['addressLine2':""])
        restResult.getAddress().add(['addressLine3':""])
        restResult.getAddress().add(['locality':"BURY"])
        restResult.getAddress().add(['province':""])
        restResult.getAddress().add(['postalCode':"BL9 0RW"])
        restResult.getAddress().add(['country':"UNITED KINGDOM"])

        0 * soapClient.doGetAddress(*_)
        1 * restClient.selectAddress(*_) >> restResult
        when:
        def res = selectionService.selectAddress(new AddressSelectionRequest( country: "UK", source:AddressSource.REST))

        then:
        res.flatUnitNo == null
        res.addressLine1 == "110 Spring Street"
        res.addressLine2 == null
        res.city == "BURY"
        res.postCode == "BL9 0RW"
        res.state == null
    }

    def "test select US address" (){
        given:
        def type = new QAAddressType()
        restResult.getAddress().add(['addressLine1':"110 Spring St"])
        restResult.getAddress().add(['addressLine2':""])
        restResult.getAddress().add(['addressLine3':""])
        restResult.getAddress().add(['locality':"Aberdeen"])
        restResult.getAddress().add(['province':"NC"])
        restResult.getAddress().add(['postalCode':"28315-3730"])
        restResult.getAddress().add(['country':"UNITED STATES OF AMERICA"])

        0 * soapClient.doGetAddress(*_)
        1 * restClient.selectAddress(*_) >> restResult
        when:
        def res = selectionService.selectAddress(new AddressSelectionRequest( country: "US", source:AddressSource.REST))

        then:
        res.flatUnitNo == null
        res.addressLine1 == "110 Spring St"
        res.addressLine2 == null
        res.city == "Aberdeen"
        res.postCode == "28315"
        res.state == "NC"
    }



}
