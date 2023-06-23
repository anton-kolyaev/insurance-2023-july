package pot.insurance.manager.dao

import pot.insurance.manager.dto.UserDTO
import pot.insurance.manager.service.UserService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import spock.lang.AutoCleanup

import jakarta.persistence.EntityManager
import java.util.UUID
import java.util.Date



class UserDAOJpaImplSpec extends Specification {

    @Shared
    @AutoCleanup
    EntityManager entityManagerMock = Mock()

    UserDAOJpaImpl userDAO

    def setup() {
        userDAO = new UserDAOJpaImpl(entityManagerMock)
    }


    @Unroll("should return #result when user is found")
    def "saveUser should merge and return the saved user"() {
        given: 'a user to save'
            def mergedUser = new UserDTO(
                id: UUID.randomUUID(),
                first_name: "James",
                last_name: "Jonas",
                birthday: new Date(1990, 1, 1),
                user_name: "jjon",
                email: "jjon@test.com",
                ssn: "123456789"
            )
            
            entityManagerMock.merge(_ as UserDTO) >> mergedUser
    
        when: 'saveUser is called'
            def savedUser = userDAO.saveUser(mergedUser)
    
        then: 'merge is called and the saved user is returned'
            1 * entityManagerMock.merge(_ as UserDTO)
            savedUser == mergedUser
}

}


