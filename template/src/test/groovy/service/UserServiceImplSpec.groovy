package service

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import org.springframework.dao.DataIntegrityViolationException

import pot.insurance.manager.dto.UserDTO
import pot.insurance.manager.entity.User
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.mapper.UserMapper
import pot.insurance.manager.repository.UserRepository
import pot.insurance.manager.service.UserServiceImpl

import java.util.Optional
import java.util.UUID
import java.sql.Date

class UserServiceImplSpec extends Specification {

    
    UserRepository userRepository = Mock()
    UserMapper userMapper = UserMapper.INSTANCE

    @Subject
    UserServiceImpl userService

    def setup() {
        userService = new UserServiceImpl(userRepository)
    }

    @Unroll("test should save user success")
    def "test should save user successfully and return userDTO"() {
        given: "as input userDTO converted to user"
            def userDTO = new UserDTO(
                id: UUID.randomUUID(),
                firstName: "Vitalii",
                lastName: "Tsal",
                birthday: Date.valueOf("1990-02-12"),
                email: "the_greatest@gmail.com",
                ssn: "123-456-7890",
                deletionStatus: false
            )
        
        when: "save user and return userDTO by passing required parameters"
            userRepository.findById(userDTO.getId()) >> Optional.empty()
            userRepository.findBySsn(userDTO.getSsn()) >> Optional.empty()
            userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
            def result = userService.save(userDTO)
        
        then: "result is not null and equals userDTO, DataValidationException is not thrown"
            result != null
            result == userDTO
            notThrown(DataValidationException)
        
        and:
            1 * userRepository.findById(userDTO.getId()) >> Optional.empty()
            1 * userRepository.findBySsn(userDTO.getSsn()) >> Optional.empty()
            1 * userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
    }

    @Unroll
    def "test should throw DataValidationException for conflicting user id"() {
        given:
            def userDTO = new UserDTO(
                id: UUID.randomUUID(),
                firstName: "Vitalii",
                lastName: "Tsal",
                birthday: Date.valueOf("1990-02-12"),
                email: "the_greatest@gmail.com",
                ssn: "123-456-7890",
                deletionStatus: false
            )
        
        when:
            userRepository.findById(userDTO.id) >> Optional.of(userMapper.userDTOToUser(userDTO))
            userRepository.findBySsn(userDTO.ssn) >> Optional.empty()
            userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
            def result = userService.save(userDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "user id is already exists"
        
        and:
            1 * userRepository.findById(userDTO.id) >> Optional.of(userMapper.userDTOToUser(userDTO))
            0 * userRepository.findBySsn(userDTO.ssn) >> Optional.empty()
            0 * userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
    }

    @Unroll
    def "test should throw DataValidationException for conflicting user ssn"() {
        given:
            def userDTO = new UserDTO(
                id: UUID.randomUUID(),
                firstName: "Vitalii",
                lastName: "Tsal",
                birthday: Date.valueOf("1990-02-12"),
                email: "the_greatest@gmail.com",
                ssn: "123-456-7890",
                deletionStatus: false
            )
        
        when:
            userRepository.findById(userDTO.id) >> Optional.empty()
            userRepository.findBySsn(userDTO.ssn) >> Optional.of(userMapper.userDTOToUser(userDTO))
            userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
            def result = userService.save(userDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "user ssn is already used"
        
        and:
            1 * userRepository.findById(userDTO.id) >> Optional.empty()
            1 * userRepository.findBySsn(userDTO.ssn) >> Optional.of(userMapper.userDTOToUser(userDTO))
            0 * userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
    }

    @Unroll
    def "test should throw DataValidationException for malformed data"() {
        given:
            def userDTO = new UserDTO(
                id: UUID.randomUUID(),
                firstName: "Vitalii",
                lastName: "Tsal",
                birthday: Date.valueOf("1990-02-12"),
                email: "the_greatest@gmail.com",
                ssn: "123-456-7890",
                deletionStatus: false
            )
        
        when:
            userRepository.findById(userDTO.id) >> Optional.empty()
            userRepository.findBySsn(userDTO.ssn) >> Optional.empty()
            userRepository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
            def result = userService.save(userDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "data is malformed"
        
        and:
            1 * userRepository.findById(userDTO.id) >> Optional.empty()
            1 * userRepository.findBySsn(userDTO.ssn) >> Optional.empty()
            1 * userRepository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
    }
    
    @Unroll
    def "test findAll should return all users"() {
        given:
            List<User> users = [
                new User(id: UUID.randomUUID(), deletionStatus: false),
                new User(id: UUID.randomUUID(), deletionStatus: false)
            ]
        
        when:
            userRepository.findAll() >> users
            List<UserDTO> result = userService.findAll()
        
        then:
            result.collect { it.id } == users.collect { it.id }
            result.collect { it.deletionStatus } == users.collect { it.deletionStatus }
            result.size() == users.size()
        
        and:
            1 * userRepository.findAll() >> users
    }

    @Unroll
    def "test findAll should return only undeleted users"() {
        given:
            List<User> users = [
                new User(id: UUID.randomUUID(), deletionStatus: false),
                new User(id: UUID.randomUUID(), deletionStatus: true)
            ]
        
        when:
            userRepository.findAll() >> users
            List<UserDTO> result = userService.findAll()
        
        then:
            result.collect { it.id } == users.findAll { !it.deletionStatus }.collect { it.id }
            result.collect { it.deletionStatus } == users.findAll { !it.deletionStatus }.collect { it.deletionStatus }
            result.size() == users.findAll { !it.deletionStatus }.size()
        
        and:
        1 * userRepository.findAll() >> users
    }

    @Unroll
    def "test findById should return found user"() {
        given:
            UUID userId = UUID.randomUUID()
            User user = new User(id: userId, deletionStatus: false)
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(user)
            UserDTO result = userService.findById(userId)
        
        then:
            result.id == user.id
            result.deletionStatus == user.deletionStatus
            notThrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(user)
    }

    @Unroll
    def "test findById should throw DataValidationException if user not found"() {
        given:
            UUID userId = UUID.randomUUID()
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            def result = userService.findById(userId)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
    }

    @Unroll
    def "test update should update a found user"() {
        given:
            UUID userId = UUID.randomUUID()
            UserDTO userDTO = new UserDTO(id: userId, deletionStatus: false)
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
            UserDTO result = userService.update(userId, userDTO)
        
        then:
            result.id == userId
            result.deletionStatus == userDTO.deletionStatus
            notThrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            1 * userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
    }

    @Unroll
    def "test update should throw DataValidationException if user not found"() {
        given:
            UUID userId = UUID.randomUUID()
            UserDTO userDTO = new UserDTO(id: userId, deletionStatus: false)
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            def result = userService.update(userId, userDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            0 * userRepository.save(_ as User) >> userMapper.userDTOToUser(userDTO)
    }

    @Unroll
    def "test update should throw DataValidationException if data is malformed"() {
        given:
            UUID userId = UUID.randomUUID()
            UserDTO userDTO = new UserDTO(id: userId, deletionStatus: false)
            
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            userRepository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
            def result = userService.update(userId, userDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "data is malformed"
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            1 * userRepository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
    }

    @Unroll
    def "test softDeleteById should soft delete found user"() {
        given:
            UUID userId = UUID.randomUUID()
            User user = new User(id: userId, deletionStatus: false)
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(user)
            userRepository.save(_ as User) >> user
            UserDTO result = userService.softDeleteById(userId)
        
        then:
            result.id == userId
            result.deletionStatus == true
            notThrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(user)
            1 * userRepository.save(_ as User) >> user
    }

    @Unroll
    def "test softDeleteById should throw DataValidationException if user not found"() {
        given:
            UUID userId = UUID.randomUUID()
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            def result = userService.softDeleteById(userId)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "user not found"
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            0 * userRepository.save(_ as User)
    }
}