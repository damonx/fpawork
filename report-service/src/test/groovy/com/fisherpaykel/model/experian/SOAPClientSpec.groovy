
import com.fisherpaykel.model.experian.SOAPClient


class SOAPClientSpec extends spock.lang.Specification {

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

    def "test can search"() {
        def client = new SOAPClient()
        given:
        def res = client.doCanSearch();
        expect:
        res.isOk == true
    }

    def "lookup address"() {
        def client = new SOAPClient()
        given:
        def res = client.doSearch("NZD", "78 Springs");
        expect:
        res.getQAPicklist().total > 0
    }

    def "select address"() {
        def client = new SOAPClient()
        given:
        def res = client.doGetAddress("NZD|8f6525f3-6535-4587-bb2e-2d4caa70b9bd|0rONZDHgnhBwAAAAAIAwEAAAB.yH0AAgAAAAAAAAAAAP..ZAAAAAD.....AAAAAAAAAAAAAAAAADU0YiB0YXdhAA--");
        expect:
        res.QAAddress.addressLine.get(0).line == "54B Tawa Road"
    }
}