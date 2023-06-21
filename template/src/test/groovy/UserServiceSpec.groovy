import spock.lang.Specification
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class UserServiceSpec extends Specification {
    
    def "Create user should return success status"() {
        
        given:
        def baseUrl = "http://localhost:8080/v1/users"
        def httpClient = new URL(baseUrl).openConnection()
        httpClient.setRequestMethod("POST")
        httpClient.setRequestProperty("Content-Type", "application/json")
        
        def userDto = [
            id: UUID.randomUUID(),
            first_name: "Mary",
            last_name: "Simpson",
            birthday: '2000-01-01',
            user_name: "marysss",
            email: "marys@test.com",
            snn: "1234567899"
        ]
        def requestBody = JsonOutput.toJson(userDto).getBytes("UTF-8")
        
        when:
        httpClient.setDoOutput(true)
        httpClient.getOutputStream().with { outputStream ->
            outputStream.write(requestBody)
            outputStream.flush()
        }
        def responseCode = httpClient.getResponseCode()

        then:
        responseCode == 201
    }
}
