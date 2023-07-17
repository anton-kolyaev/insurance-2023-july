package controller

import pot.insurance.manager.dto.UserDTO
import pot.insurance.manager.service.UserService
import pot.insurance.manager.controller.UserRestController

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.sql.Date

class UserRestControllerSpec extends Specification {
    
    UserService userService = Mock()

    @Subject
    UserRestController userRestController = new UserRestController(userService)

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
            UserDTO result = userRestController.saveUser(userDTO)
        
        then: "result is not null and equals userDTO"
            result != null
            result == userDTO
        
        and: "userService.save(userDTO) is called once"
            1 * userService.save(userDTO) >> userDTO
    }
        
    @Unroll("test saveUser metod for unsuccessful save with #userDTO")
    def "test saveUser metod for unsuccessful save should return null"() {
        given: "UserDTO object with invalid data"
            def userDTO = null
        
        when: "is called method saveUser with userDTO"
            UserDTO response = userRestController.saveUser(userDTO)
        
        then: "response is null"
            response == null
        
        and: "userService.save(userDTO) is called once and return null"
            1 * userService.save(userDTO) >> null
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
            UserDTO result = userRestController.findUserById(userDTO.getId())
        
        then:
            result != null
            result == userDTO
        
        and:
            1 * userService.findById(userDTO.getId()) >> userDTO
    }

    @Unroll("test findUserById method for unsuccessful find with #userDTO")
    def "test findUserById method for unsuccessful find return null"() {
        given: "UserDTO object with invalid data"
            def userDTO = null
        
        when: "is called method findUserById with userDTO"
            UserDTO response = userRestController.findUserById(userDTO?.getId())
        
        then: "response is null"
            response == null
        
        and: "userService.findById(userDTO?.getId()) is called once and return null"
            1 * userService.findById(userDTO?.getId()) >> null
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
            
            def List<UserDTO> users = [userDTO1, userDTO2]
        
        when: "is called method findAll"
            List<UserDTO> result = userRestController.findAllUsers()
        
        then: "result is not null and equals users"
            result != null
            result == users
        
        and: "userService.findAll() is called once and return users"
            1 * userService.findAll() >> users
    }

    @Unroll("test retrive all users by calling findAll method with empty list")
    def "test retrive all users by calling findAll method return empty list"() {
        given: "empty list"
            def List<UserDTO> users = []
        
        when: "is called method findAll"
            List<UserDTO> result = userRestController.findAllUsers()
        
        then: "result is not null and equals users"
            result != null
            result == users
        
        and: "userService.findAll() is called once and return users"
            1 * userService.findAll() >> users
    }

    @Unroll("test update user by calling updateUser method with #userId and #userDTO")
    def "test update user by calling updateUser method return updated user values"() {
        given: "UserDTO object with valid data and userId to update concrete user"
            def userId = UUID.randomUUID()
            def userDTO = new UserDTO(
                id: userId,
                firstName: "Vitalii",
                lastName: "Tsal",
                birthday: Date.valueOf("1990-02-12"),
                email: "the_greatest@gmail.com",
                ssn: "123-456-7890",
                deletionStatus: false
            )
        when: "is called method updateUser"
            UserDTO result = userRestController.updateUser(userId, userDTO)
        
        then: "result is not null and equals userDTO"
            result != null
            result == userDTO
        
        and: "the update method is called once and return userDTO"
            1 * userService.update(userId, userDTO) >> userDTO
    }

    @Unroll("test update user by calling updateUser method with #userId and #userDTO")
    def "test update user by calling updateUser method return null"() {
        given: "UserDTO object with invalid data and userId to update concrete user"
            def userId = UUID.randomUUID()
            def userDTO = null
        when: "is called method updateUser"
            UserDTO result = userRestController.updateUser(userId, userDTO)
        
        then: "result is null"
            result == null
        
        and: "the update method is called once and return null"
            1 * userService.update(userId, userDTO) >> null
    }

    @Unroll("test delete user by calling deleteUser method with #userId")
    def "test delete user by calling deleteUserById method return userDtO with true value for deletionStatus"() {
        given: "userId to delete concrete user"
            def userId = UUID.randomUUID()
            def userDTO = new UserDTO(
                id: userId,
                firstName: "Vitalii",
                lastName: "Tsal",
                birthday: Date.valueOf("1990-02-12"),
                email: "papich@gmail.com",
                ssn: "123-456-7890",
                deletionStatus: true
            )
        
        when: "is called method deleteUserById"
            UserDTO result = userRestController.deleteUserById(userId)
        
        then: "result is true"
            result == userDTO
        
        and: "the delete method is called once and return userDTO"
            1 * userService.softDeleteById(userId) >> userDTO
    }

    @Unroll("test delete user by calling deleteUser method with #userId")
    def "test delete user by calling deleteUserById method return null"() {
        given: "userId to delete concrete user"
            def userId = UUID.randomUUID()
            def userDTO = null
        
        when: "is called method deleteUserById"
            UserDTO result = userRestController.deleteUserById(userId)
        
        then: "result is null"
            result == null
        
        and: "the delete method is called once and return null"
            1 * userService.softDeleteById(userId) >> null
    }
}