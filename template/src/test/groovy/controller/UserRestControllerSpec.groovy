package controller

import pot.insurance.manager.dto.UserDTO
import pot.insurance.manager.service.UserService
import pot.insurance.manager.controller.UserRestController

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.sql.Date

class UserRestControllerSpec extends Specification {
    
    UserService service = Mock()

    @Subject
    UserRestController controller = new UserRestController(service)

    @Unroll("test saveUser metod for successful save with #userDTO")
    def "test saveUser metod for successful save"() {
        given: "UserDTO object with valid data"
        def userDTO = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Harry",
            lastName: "Truman",
            birthday: Date.valueOf("1990-02-12"),
            email: "trueharry@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )
        
        when: "is called method saveUser with userDTO"
        UserDTO result = controller.saveUser(userDTO)
        
        then: "result is not null and equals userDTO"
        result != null
        result == userDTO
        
        and: "userService.save(userDTO) is called once"
        1 * service.save(userDTO) >> userDTO
    }
        
    @Unroll("test saveUser metod for unsuccessful save with #userDTO")
    def "test saveUser metod for unsuccessful save should return null"() {
        given: "UserDTO object with invalid data"
        def userDTO = null
        
        when: "is called method saveUser with userDTO"
        UserDTO response = controller.saveUser(userDTO)
        
        then: "response is null"
        response == null
        
        and: "userService.save(userDTO) is called once and return null"
        1 * service.save(userDTO) >> null
    }
    
    @Unroll("test findUserById method for successful find with #userDTO")
    def "test findUserById method for successful find"() {
        given: "UserDTO object with valid data"
        def userDTO = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Harry",
            lastName: "Truman",
            birthday: Date.valueOf("1990-02-12"),
            email: "trueharry@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        when: "is called method findUserById with userDTO"
        def result = controller.findUserById(userDTO.getId())
        
        then:
        result != null
        result == userDTO

        and:
        1 * service.findById(userDTO.getId()) >> userDTO
    }

    @Unroll("test findUserById method for unsuccessful find with #userDTO")
    def "test findUserById method for unsuccessful find return null"() {
        given: "UserDTO object with invalid data"
        def userDTO = null
        
        when: "is called method findUserById with userDTO"
        def response = controller.findUserById(userDTO?.getId())
        
        then: "response is null"
        response == null
        
        and: "userService.findById(userDTO?.getId()) is called once and return null"
        1 * service.findById(userDTO?.getId()) >> null
    }

    @Unroll("test retrive all users by calling findAll method with #userDTO1 and #userDTO2 in list")
    def "test retrive all users by calling findAll method return list of users"() {
        given: "UserDTO objects with valid data"
        def userDTO1 = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Harry",
            lastName: "Truman",
            birthday: Date.valueOf("1990-02-12"),
            email: "trueharry@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        def userDTO2 = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "arthas@mail.ua",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        def users = [userDTO1, userDTO2]
        
        when: "is called method findAll"
        def result = controller.findAllUsers()
        
        then: "result is not null and equals users"
        result != null
        result == users
        
        and: "userService.findAll() is called once and return users"
        1 * service.findAll() >> users
    }

    @Unroll("test retrieve all users by calling findAll method with empty list")
    def "test retrieve all users by calling findAll method return empty list"() {
        given: "empty list"
        def users = []
        
        when: "is called method findAll"
        def result = controller.findAllUsers()
        
        then: "result is not null and equals users"
        result != null
        result == users
        
        and: "userService.findAll() is called once and return users"
        1 * service.findAll() >> users
    }

    @Unroll("test update user by calling updateUser method with #userId and #userDTO")
    def "test update user by calling updateUser method return updated user values"() {
        given: "UserDTO object with valid data and userId to update concrete user"
        def dto = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "the_greatest@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        when: "is called method updateUser"
        def result = controller.updateUser(dto.id, dto)
        
        then: "result is not null and equals userDTO"
        result != null
        result == dto
        
        and: "the update method is called once and return userDTO"
        1 * service.update(dto) >> dto
    }

    @Unroll("test update user by calling updateUser method with #userId and #userDTO")
    def "test update user by calling updateUser method return null"() {
        given: "UserDTO object with invalid data and userId to update concrete user"
        def id = UUID.randomUUID()
        def dto = null

        when: "is called method updateUser"
        controller.updateUser(id, dto)
        
        then: "it throws exception"
        thrown(Exception)
    }

    @Unroll("test delete user by calling deleteUser method with #userId")
    def "test delete user by calling deleteUserById method return userDtO with true value for deletionStatus"() {
        given: "userId to delete concrete user"
        def id = UUID.randomUUID()
        def dto = new UserDTO(
            id: id,
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "papich@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: true
        )
        
        when: "is called method deleteUserById"
        def result = controller.deleteUserById(id)
        
        then: "result is true"
        result == dto
        
        and: "the delete method is called once and return userDTO"
        1 * service.softDeleteById(id) >> dto
    }

    @Unroll("test delete user by calling deleteUser method with #userId")
    def "test delete user by calling deleteUserById method return null"() {
        given: "userId to delete concrete user"
        def id = UUID.randomUUID()

        when: "is called method deleteUserById"
        def result = controller.deleteUserById(id)
        
        then: "result is null"
        result == null
        
        and: "the delete method is called once and return null"
        1 * service.softDeleteById(id) >> null
    }
}