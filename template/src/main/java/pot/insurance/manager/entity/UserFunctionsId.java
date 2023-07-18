package pot.insurance.manager.entity;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class UserFunctionsId implements Serializable {

    private UUID userId;
    private UUID companyId;

}
