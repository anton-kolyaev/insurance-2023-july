package service

import base.TestableTrait

import org.springframework.security.crypto.password.PasswordEncoder

import pot.insurance.manager.dto.UserAuthDTO
import pot.insurance.manager.entity.UserAuth
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.mapper.UserAuthMapperImpl
import pot.insurance.manager.repository.UserAuthRepository
import pot.insurance.manager.service.UserAuthServiceImpl
import pot.insurance.manager.type.DataValidation
import pot.insurance.manager.type.UserAuthRole
import pot.insurance.manager.type.UserAuthStatus

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class UserAuthServiceSpec extends Specification implements TestableTrait {

    def repository = Mock(UserAuthRepository)

    @Shared
    def mapper = new UserAuthMapperImpl()

    @Shared
    def encoder = Mock(PasswordEncoder)

    def service = new UserAuthServiceImpl(repository, mapper, encoder)

    @Shared
    def usernameLimit = UserAuthServiceImpl.getMaxUsernameLength() + 1

    @Shared
    def passwordLimit = UserAuthServiceImpl.getMaxPasswordLength() + 1

    def setupSpec() {
        encoder.encode(_ as String) >> {String password -> password}
    }

    @Unroll
    def "test for save(UserAuthDTO) method with valid data"() {
        given:
        def dto = new UserAuthDTO(id, username, password, role, status)

        when:
        service.save(dto)

        then:
        notThrown(DataValidationException)

        1 * repository.save(_ as UserAuth)

        where:
        id                                                      | username | password   | role                | status
        UUID.fromString("46767f04-3a4b-4c65-8ff1-b19388a0b933") | "sammy"  | "5ammy-23" | UserAuthRole.ADMIN  | UserAuthStatus.ACTIVE
        null                                                    | "limeyh" | "l1mey@4"  | UserAuthRole.VIEWER | null
        UUID.fromString("588d73e4-0e67-43c6-a422-b3f63d36fcd9") | "boby"   | "b0bby!"   | null                | UserAuthStatus.DEACTIVATED
        UUID.fromString("fe5f3fe1-fdce-47b1-a7d7-900a7f37d8f1") | "timmyh" | "t1mm!-5"  | null                | null

    }

    @Unroll
    def "test for save(UserAuthDTO) method with invalid data"() {
        given:
        def dto = new UserAuthDTO(id, username, password, role, status)

        when:
        service.save(dto)

        then:
        thrown(Exception)

        0 * repository.save(_ as UserAuth)

        where:
        id                                                      | username                  | password                  | role                   | status
        UUID.fromString("46767f04-3a4b-4c65-8ff1-b19388a0b933") | 's'.repeat(usernameLimit) | "5ammy-23"                | UserAuthRole.ADMIN     | UserAuthStatus.ACTIVE
        null                                                    | "limeyh"                  | 'l'.repeat(passwordLimit) | UserAuthRole.VIEWER    | null
        UUID.fromString("588d73e4-0e67-43c6-a422-b3f63d36fcd9") | 'b'.repeat(usernameLimit) | 'b'.repeat(passwordLimit) | null                   | UserAuthStatus.DEACTIVATED
        UUID.fromString("fe5f3fe1-fdce-47b1-a7d7-900a7f37d8f1") | "timmyh"                  | ''                        | null                   | null
        UUID.fromString("ed029240-1715-44c6-be94-b093f6285714") | ''                        | "z1zzy-65!"               | UserAuthRole.MODERATOR | UserAuthStatus.DELETED
        UUID.fromString("516acbd8-7391-4cd8-9e39-578f5bc7545c") | null                      | "lockd-0ut!"              | UserAuthRole.ADMIN     | null
        UUID.fromString("8509fbdd-5b4a-423f-9ed0-71be989f6ae4") | "n00bie"                  | null                      | null                   | UserAuthStatus.DEACTIVATED
        UUID.fromString("e4a4130a-8720-4670-bd07-1619e10ce394") | null                      | null                      | null                   | null
    }


    @Unroll
    def "test for save(UserAuthDTO) method with conflicting data"() {
        given:
        def dto = new UserAuthDTO(id1, username1, password1, role1, status1)

        when:
        repository.existsByUsernameIgnoreCase(_  as String) >> { String username -> username.toLowerCase() == username2.toLowerCase() }
        repository.existsById(_ as UUID) >> { UUID id -> id == id2 }

        service.save(dto)

        then:
        def exception = thrown(DataValidationException)

        0 * repository.save(_ as UserAuth)
        exception.getStatus().getCategory() == DataValidation.Category.PRESENT

        where:
        id1                                                     | username1  | password1  | role1               | status1
        UUID.fromString("46767f04-3a4b-4c65-8ff1-b19388a0b933") | "sammy"    | "5ammy-23" | UserAuthRole.ADMIN  | UserAuthStatus.ACTIVE
        null                                                    | "limeyh"   | "l1mey@4"  | UserAuthRole.VIEWER | null
        UUID.fromString("588d73e4-0e67-43c6-a422-b3f63d36fcd9") | "boby"     | "b0bby!"   | null                | UserAuthStatus.DEACTIVATED
        UUID.fromString("fe5f3fe1-fdce-47b1-a7d7-900a7f37d8f1") | "timmyh"   | "t1mm!-5"  | null                | null
        __

        id2                                                           | username2  | password2  | role2               | status2
        UUID.fromString("46767f04-3a4b-4c65-8ff1-b19388a0b933") | "sammd"    | "5ammy-23" | UserAuthRole.ADMIN  | UserAuthStatus.ACTIVE
        null                                                          | "limeyh"   | "l1mey@4"  | UserAuthRole.VIEWER | null
        UUID.fromString("588d73e4-0e67-43c6-a422-b3f63d36fcdf") | "boby"     | "b0bby!"   | null                | UserAuthStatus.DEACTIVATED
        UUID.fromString("fe5f3fe1-fdce-47b1-a7d7-900a7f37d8f1") | "timmyh"   | "t1mm!-5"  | null                | null

    }


    @Unroll
    def "test for update(UserAuthDTO) method with valid data"() {
        given:
        def dto = new UserAuthDTO(id, username, password, role, status)

        when:
        repository.existsByIdAndStatusNot(_ as UUID, UserAuthStatus.DELETED) >> true

        service.update(dto)

        then:
        notThrown(DataValidationException)

        1 * repository.save(_ as UserAuth)

        where:
        id                                                      | username | password   | role                | status
        UUID.fromString("46767f04-3a4b-4c65-8ff1-b19388a0b933") | "sammy"  | "5ammy-23" | UserAuthRole.ADMIN  | UserAuthStatus.ACTIVE
        UUID.fromString("008b3e5b-16d0-4d6d-a870-30598743ee3a") | "limeyh" | "l1mey@4"  | UserAuthRole.VIEWER | null
        UUID.fromString("588d73e4-0e67-43c6-a422-b3f63d36fcd9") | "boby"   | "b0bby!"   | null                | UserAuthStatus.DEACTIVATED
        UUID.fromString("fe5f3fe1-fdce-47b1-a7d7-900a7f37d8f1") | "timmyh" | "t1mm!-5"  | null                | null
    }

    @Unroll
    def "test for update(UserAuthDTO) method with invalid data"() {
        given:
        def dto = new UserAuthDTO(id, username, password, role, status)

        when:
        repository.existsByIdAndStatusNot(_ as UUID, UserAuthStatus.DELETED) >> true

        service.update(dto)

        then:
        thrown(Exception)

        0 * repository.save(_ as UserAuth)

        where:
        id                                                      | username                  | password                  | role                   | status
        UUID.fromString("46767f04-3a4b-4c65-8ff1-b19388a0b933") | 's'.repeat(usernameLimit) | "5ammy-23"                | UserAuthRole.ADMIN     | UserAuthStatus.ACTIVE
        null                                                    | "limeyh"                  | 'l'.repeat(passwordLimit) | UserAuthRole.VIEWER    | null
        UUID.fromString("588d73e4-0e67-43c6-a422-b3f63d36fcd9") | 'b'.repeat(usernameLimit) | 'b'.repeat(passwordLimit) | null                   | UserAuthStatus.DEACTIVATED
        UUID.fromString("fe5f3fe1-fdce-47b1-a7d7-900a7f37d8f1") | "timmyh"                  | ''                        | null                   | null
        UUID.fromString("ed029240-1715-44c6-be94-b093f6285714") | ''                        | "z1zzy-65!"               | UserAuthRole.MODERATOR | UserAuthStatus.DELETED
        UUID.fromString("516acbd8-7391-4cd8-9e39-578f5bc7545c") | null                      | "lockd-0ut!"              | UserAuthRole.ADMIN     | null
        UUID.fromString("8509fbdd-5b4a-423f-9ed0-71be989f6ae4") | "n00bie"                  | null                      | null                   | UserAuthStatus.DEACTIVATED
        UUID.fromString("e4a4130a-8720-4670-bd07-1619e10ce394") | null                      | null                      | null                   | null
    }
}
