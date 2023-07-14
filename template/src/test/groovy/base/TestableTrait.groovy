package base

trait TestableTrait {
    void assertResponseStatus(def response, int expectedResponseCode) {
        assert response?.status == expectedResponseCode
    }

    void assertReceivedDataAreAsExpected(def actualResponse, def expected) {
        assert actualResponse == expected
    }

    void assertReceivedDataAreNotEqual(def actualResponse, def expected) {
        assert actualResponse != expected
    }
}