package base

trait TestableTrait {
    void assertResponseStatus(def response, int expectedResponseCode) {
        assert response?.status == expectedResponseCode
    }
}