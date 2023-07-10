package pot.insurance.manager.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String email;
    private String ssn;
    private boolean deletionStatus;

}

