package pot.insurence.manager.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    private UUID id;
    @Column(name = "company_name")
    private String company_name;
    @Column(name = "country_code")
    private String country_code;
    @Column(name = "email")
    private String email;
    @Column(name = "site")
    private String site;
}
