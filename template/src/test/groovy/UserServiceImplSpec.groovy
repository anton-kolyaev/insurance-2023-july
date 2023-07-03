package pot.insurance.manager

import spock.lang.Specification
import spock.lang.Subject

import java.util.UUID
import java.sql.Date

import pot.insurance.manager.dao.UserRepository
import pot.insurance.manager.dto.UserDTO
import pot.insurance.manager.entity.User
import pot.insurance.manager.exception.exeptions.UserWrongCredentialsInput
import pot.insurance.manager.mapper.UserMapper
import pot.insurance.manager.service.UserServiceImpl

// !!!!!!!!!!!!!!!
// GROOVY TESTS NOT WORKING!!!
// !!!!!!!!!!!!!!!
class UserServiceImplSpec extends Specification{
    
    @Subject
    UserServiceImpl userService

    // Mocks
    def userRepository = Mock(UserRepository)
    def userMapper = Mock(UserMapper)

    def setup() {
        userService = new UserServiceImpl(userRepository)
        userMapper = UserMapper.INSTANCE

    }


    def "Save user should return the saved user"() {
        given:
        UUID userId = UUID.randomUUID()
        UserDTO userDTO = new UserDTO(
            userId: userId,
            firstName: "Sammy",
            lastName: "Sam",
            ssn: "123456789",
            birthday: Date.valueOf("1990-01-01"),
            email: "test@test.test",
            username: "test_sam"
        )
        User user = new User(
            userId: userId,
            firstName: "Sammy",
            lastName: "Sam",
            ssn: "123456789",
            birthday: Date.valueOf("1990-01-01"),
            email: "test@test.test",
            username: "test_sam"
        )
        
        userRepository.save(_ as User) >> user
        
        when:
        UserDTO savedUserDTO = userService.save(userDTO)

        then:
        1 * userRepository.save(user)
    }
}