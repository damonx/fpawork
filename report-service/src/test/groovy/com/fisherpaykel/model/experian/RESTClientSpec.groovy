import com.fisherpaykel.model.experian.RESTClient

class RESTClientSpec extends spock.lang.Specification {

    def setupSpec(){
        Properties properties = new Properties()

        def resource = this.getClass().getResource("application.properties")
        if(resource) {
            File propertiesFile = new File(resource.getPath());
            propertiesFile.withInputStream {
                properties.load(it)
            }
            properties.entrySet().each { entry ->
                System.setProperty(entry.key, entry.value)
            }
        }
    }

    def "lookup address"() {
        def client = new RESTClient()
        given:
        def res = client.lookupAddress("USA", "78 Springs");
        expect:
        res.count > 0
    }

    def "lookup address no results"() {
        def client = new RESTClient()
        given:
        def res = client.lookupAddress("GBR", "1 Mickey Mouse Road");
        expect:
        res.count == 0
    }

    def "select us address"() {
        def client = new RESTClient()
        given:
        def res = client.selectAddress("https://api.edq.com/capture/address/v2/format?country=USA&id=31f4eca4-9669-4035-95f9-95ed6c4e5bca_U40_24_0_0_0%3D78")
        expect:
        res.address.get(0).get("addressLine1") == "78 Springs Rd"
        res.address.get(1).get("addressLine2") == ""

    }
}