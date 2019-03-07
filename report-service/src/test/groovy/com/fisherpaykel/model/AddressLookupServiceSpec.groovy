package com.fisherpaykel.model

import com.fisherpaykel.generated.experian.ws.PicklistEntryType
import com.fisherpaykel.generated.experian.ws.QAPicklistType
import com.fisherpaykel.generated.experian.ws.QASearchResult
import com.fisherpaykel.model.experian.AddressLookupResponse
import com.fisherpaykel.model.experian.AddressSuggestion
import com.fisherpaykel.model.experian.RESTClient
import com.fisherpaykel.model.experian.SOAPClient
import spock.lang.Specification

class AddressLookupServiceSpec extends Specification {

    private AddressLookupService service
    private SOAPClient soapClient
    private RESTClient restClient
    private QASearchResult soapResult =  new QASearchResult()
    private AddressLookupResponse restResult = new AddressLookupResponse()

    public void setup(){
        service = new AddressLookupService()
        soapClient = Mock(SOAPClient.class)
        restClient = Mock(RESTClient.class)


        def searchResoults = new ArrayList<PicklistEntryType>()
        searchResoults.add(new PicklistEntryType())
        def type = new QAPicklistType(
                picklistEntry: searchResoults,
                total: 1
        )
        soapResult.setQAPicklist(type)


        def suggestions = new ArrayList<AddressSuggestion>()
        suggestions.add(new AddressSuggestion())
        restResult.setResults(suggestions)

        service.setSoapClient(soapClient)
        service.setRestClient(restClient)
    }

    def "test search for NZ address"() {
        given:
        1 * soapClient.doSearch(*_) >> { arguments ->
            assert arguments[0] == "NZD"
            return soapResult
        }
        0 * restClient.lookupAddress(*_)
        when:
        def res = service.lookupAddress("NZ", "54b%20Tawa")

        then:
        res.size() == 1
        res.get(0).source == AddressSource.SOAP
    }

    def "test search for AU address"() {
        given:
        1 * soapClient.doSearch(*_) >> { arguments ->
            assert arguments[0] == "AUE"
            return soapResult
        }
        0 * restClient.lookupAddress(*_)
        when:
        def res = service.lookupAddress("AU", "54b%20Tawa")

        then:
        res.size() == 1
        res.get(0).source == AddressSource.SOAP
    }

    def "test search for US address"() {
        given:
        1 * restClient.lookupAddress(*_) >> { arguments ->
            assert arguments[0] == "USA"
            return restResult
        }
        0 * soapClient.doSearch(*_)
        when:
        def res = service.lookupAddress("US", "54b%20Tawa")

        then:
        res.size() == 1
        res.get(0).source == AddressSource.REST
    }

    def "test search for GB address"() {
        given:
        1 * restClient.lookupAddress(*_) >> { arguments ->
            assert arguments[0] == "GBR"
            return restResult
        }
        0 * soapClient.doSearch(*_)
        when:
        def res = service.lookupAddress("UK", "54b%20Tawa")

        then:
        res.size() == 1
        res.get(0).source == AddressSource.REST
    }

    def "test search for unknown country"() {
        0 * restClient.lookupAddress(*_)
        0 * soapClient.doSearch(*_)
        when:
        def res = service.lookupAddress("XX", "54b%20Tawa")

        then:
        res.size() == 0
    }

    def "test search for null or empty queries"() {
        0 * restClient.lookupAddress(*_)
        0 * soapClient.doSearch(*_)
        when:
        def res1 = service.lookupAddress("US", "")
        def res2 = service.lookupAddress("US", null)
        then:
        res1.size() == 0
        res2.size() == 0
    }

    def "test rest search returns no results"(){
        given:
        1 * restClient.lookupAddress(*_) >> { arguments ->
            assert arguments[0] == "GBR"
            return new AddressLookupResponse()
        }
        0 * soapClient.doSearch(*_)
        when:
        def res = service.lookupAddress("UK", "54b%20Tawa")

        then:
        res.size() == 0
    }

    def "test soap search returns no results"() {
        given:
        1 * soapClient.doSearch(*_) >> { arguments ->
            assert arguments[0] == "NZD"
            return new QASearchResult(
                    qaPicklist: new QAPicklistType(
                            picklistEntry: Arrays.asList(new PicklistEntryType(
                                    picklist: "No matches"
                            )),
                            total: 0
                    )
            )
        }
        0 * restClient.lookupAddress(*_)
        when:
        def res = service.lookupAddress("NZ", "54b%20Tawa")

        then:
        res.size() == 0
    }

}
