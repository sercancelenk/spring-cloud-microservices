package byzas.io.microservice.authservice.domain;

import byzas.io.microservice.authservice.model.PermissionType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "app_permissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppPermission {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String appId;
    @Column
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    @Column
    private String permissionVal;
}
