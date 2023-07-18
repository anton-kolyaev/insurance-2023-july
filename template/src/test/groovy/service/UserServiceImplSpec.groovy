package service

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import org.springframework.dao.DataIntegrityViolationException

import pot.insurance.manager.dto.UserDTO
import pot.insurance.manager.mapper.UserMapperImpl
import pot.insurance.manager.service.UserAuthService
import pot.insurance.manager.type.DataValidation
import pot.insurance.manager.entity.User
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.mapper.UserMapper
import pot.insurance.manager.repository.UserRepository
import pot.insurance.manager.service.UserServiceImpl

import java.sql.Date

class UserServiceImplSpec extends Specification {

    
    UserRepository repository = Mock()

    UserMapper mapper = new UserMapperImpl()

    @Subject
    UserServiceImpl service = new UserServiceImpl(repository, mapper, Mock(UserAuthService))

    @Unroll("test should save user success")
    def "test should save user successfully and return userDTO"() {
        given: "as input dto converted to user"
        def dto = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "the_greatest@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )
        
        when: "save user and return userDTO by passing required parameters"
        repository.existsById(dto.getId()) >> false
        repository.existsBySsn(dto.getSsn()) >> false
        repository.save(_ as User) >> mapper.toEntity(dto)
        def result = service.save(dto)
        
        then: "result is not null and equals userDTO, DataValidationException is not thrown"
        result != null
        result == dto
        notThrown(DataValidationException)
        
        and:
        1 * repository.existsById(dto.getId()) >> Optional.empty()
        1 * repository.existsBySsn(dto.getSsn()) >> Optional.empty()
        1 * repository.save(_ as User) >> mapper.toEntity(dto)
    }

    @Unroll
    def "test should throw DataValidationException for conflicting user id"() {
        given:
        def dto = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "the_greatest@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        when:
        repository.existsById(dto.id) >> Optional.of(mapper.toEntity(dto))
        repository.existsBySsn(dto.ssn) >> Optional.empty()
        repository.save(_ as User) >> mapper.toEntity(dto)
        def result = service.save(dto)

        then:
        result == null
        DataValidationException exception = thrown(DataValidationException)
        exception.getStatus() == DataValidation.Status.USER_ID_EXISTS

        and:
        1 * repository.existsById(dto.id) >> Optional.of(mapper.toEntity(dto))
        0 * repository.existsBySsn(dto.ssn) >> Optional.empty()
        0 * repository.save(_ as User) >> mapper.toEntity(dto)
    }

    @Unroll
    def "test should throw DataValidationException for conflicting user ssn"() {
        given:
        def dto = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "the_greatest@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        when:
        repository.existsById(dto.id) >> Optional.empty()
        repository.existsBySsn(dto.ssn) >> Optional.of(mapper.toEntity(dto))
        repository.save(_ as User) >> mapper.toEntity(dto)
        def result = service.save(dto)

        then:
        result == null
        DataValidationException exception = thrown(DataValidationException)
        exception.getStatus() == DataValidation.Status.USER_SSN_EXISTS

        and:
        1 * repository.existsById(dto.id) >> Optional.empty()
        1 * repository.existsBySsn(dto.ssn) >> Optional.of(mapper.toEntity(dto))
        0 * repository.save(_ as User) >> mapper.toEntity(dto)
    }

    @Unroll
    def "test should throw DataValidationException for malformed data"() {
        given:
        def dto = new UserDTO(
            id: UUID.randomUUID(),
            firstName: "Vitalii",
            lastName: "Tsal",
            birthday: Date.valueOf("1990-02-12"),
            email: "the_greatest@gmail.com",
            ssn: "123-456-7890",
            deletionStatus: false
        )

        when:
        repository.existsById(dto.id) >> Optional.empty()
        repository.existsBySsn(dto.ssn) >> Optional.empty()
        repository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
        def result = service.save(dto)

        then:
        result == null
        DataValidationException exception = thrown(DataValidationException)
        exception.getStatus() == DataValidation.Status.MALFORMED_DATA

        and:
        1 * repository.existsById(dto.id) >> Optional.empty()
        1 * repository.existsBySsn(dto.ssn) >> Optional.empty()
        1 * repository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
    }

    @Unroll
    def "test findAll should return all users"() {
        given:
        List<User> entities = [
            new User(id: UUID.randomUUID(), deletionStatus: false),
            new User(id: UUID.randomUUID(), deletionStatus: false)
        ]

        when:
        repository.findAllByDeletionStatus(false) >> entities.stream()
            .filter(user -> !user.isDeletionStatus())
            .toList()

        List<UserDTO> result = service.findAll()

        then:
        result.collect { it.id } == entities.collect { it.id }
        result.collect { it.deletionStatus } == entities.collect { it.deletionStatus }
        result.size() == entities.size()

        and:
        1 * repository.findAllByDeletionStatus(false) >> entities.stream()
            .filter(user -> !user.isDeletionStatus())
            .toList()
    }

    @Unroll
    def "test findAll should return only undeleted users"() {
        given:
        List<User> entities = [
            new User(id: UUID.randomUUID(), deletionStatus: false),
            new User(id: UUID.randomUUID(), deletionStatus: true)
        ]

        when:
        repository.findAllByDeletionStatus(false) >> entities.stream()
            .filter(user -> !user.isDeletionStatus())
            .toList()
        List<UserDTO> result = service.findAll()

        then:
        result.collect { it.id } == entities.findAll { !it.deletionStatus }.collect { it.id }
        result.collect { it.deletionStatus } == entities.findAll { !it.deletionStatus }.collect { it.deletionStatus }
        result.size() == entities.findAll { !it.deletionStatus }.size()

        and:
        1 * repository.findAllByDeletionStatus(false) >> entities.stream()
            .filter(user -> !user.isDeletionStatus())
            .toList()
    }

    @Unroll
    def "test findById should return found user"() {
        given:
        UUID id = UUID.randomUUID()
        User entity = new User(id: id, deletionStatus: false)

        when:
        repository.findByIdAndDeletionStatus(id, false) >> Optional.of(entity)
        UserDTO result = service.findById(id)

        then:
        result.id == entity.id
        result.deletionStatus == entity.deletionStatus
        notThrown(DataValidationException)

        and:
        1 * repository.findByIdAndDeletionStatus(id, false) >> Optional.of(entity)
    }

    @Unroll
    def "test findById should throw DataValidationException if user not found"() {
        given:
        UUID id = UUID.randomUUID()

        when:
        repository.findByIdAndDeletionStatus(id, false) >> Optional.empty()
        def result = service.findById(id)

        then:
        result == null
        thrown(DataValidationException)

        and:
        1 * repository.findByIdAndDeletionStatus(id, false) >> Optional.empty()
    }

    @Unroll
    def "test update should update a found user"() {
        given:
        def dto = new UserDTO(id: UUID.randomUUID(), deletionStatus: false)

        when:
        repository.findByIdAndDeletionStatus(dto.id, false) >> Optional.of(new User())
        repository.save(_ as User) >> mapper.toEntity(dto)
        UserDTO result = service.update(dto)

        then:
        result.id == dto.id
        result.deletionStatus == dto.deletionStatus
        notThrown(DataValidationException)

        and:
        1 * repository.findByIdAndDeletionStatus(dto.id, false) >> Optional.of(new User())
        1 * repository.save(_ as User) >> mapper.toEntity(dto)
    }

    @Unroll
    def "test update should throw DataValidationException if user not found"() {
        given:
        def dto = new UserDTO(id: UUID.randomUUID(), deletionStatus: false)

        when:
        repository.findByIdAndDeletionStatus(dto.id, false) >> Optional.empty()
        def result = service.update(dto)

        then:
        result == null
        thrown(DataValidationException)

        and:
        1 * repository.findByIdAndDeletionStatus(dto.id, false) >> Optional.empty()
        0 * repository.save(_ as User) >> mapper.toEntity(dto)
    }

    @Unroll
    def "test update should throw DataValidationException if data is malformed"() {
        given:
        UserDTO dto = new UserDTO(id: UUID.randomUUID(), deletionStatus: false)

        when:
        repository.findByIdAndDeletionStatus(dto.id, false) >> Optional.of(new User())
        repository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
        def result = service.update(dto)

        then:
        result == null
        DataValidationException exception = thrown(DataValidationException)
        exception.getStatus() == DataValidation.Status.MALFORMED_DATA

        and:
        1 * repository.findByIdAndDeletionStatus(dto.id, false) >> Optional.of(new User())
        1 * repository.save(_ as User) >> { throw new DataIntegrityViolationException("") }
    }

    @Unroll
    def "test softDeleteById should soft delete found user"() {
        given:
        User entity = new User(id: UUID.randomUUID(), deletionStatus: false)

        when:
        repository.findByIdAndDeletionStatus(entity.id, false) >> Optional.of(entity)
        repository.save(_ as User) >> entity
        UserDTO result = service.softDeleteById(entity.id)

        then:
        result.id == entity.id
        result.deletionStatus
        notThrown(DataValidationException)

        and:
        1 * repository.findByIdAndDeletionStatus(entity.id, false) >> Optional.of(entity)
        1 * repository.save(_ as User) >> entity
    }

    @Unroll
    def "test softDeleteById should throw DataValidationException if user not found"() {
        given:
        UUID id = UUID.randomUUID()

        when:
        repository.findByIdAndDeletionStatus(id, false) >> Optional.empty()
        def result = service.softDeleteById(id)

        then:
        result == null
        DataValidationException exception = thrown(DataValidationException)
        exception.getStatus() == DataValidation.Status.USER_NOT_FOUND

        and:
        1 * repository.findByIdAndDeletionStatus(id, false) >> Optional.empty()
        0 * repository.save(_ as User)
    }
}