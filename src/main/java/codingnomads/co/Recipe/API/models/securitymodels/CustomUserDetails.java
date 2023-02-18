package codingnomads.co.Recipe.API.models.securitymodels;

import codingnomads.co.Recipe.API.models.UserMeta;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class CustomUserDetails implements UserDetails {

    @Column(nullable = false, unique = true)
    public String username;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    //make sure that the password is never sent out but can be read when creating a new user
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private boolean isAccountNonExpired = true;

    @Column(nullable = false)
    private boolean isAccountNonLocked = true;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired = true;

    @Column(nullable = false)
    private boolean isEnabled = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private Collection<Role> authorities = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST, optional = false)
    private UserMeta userMeta;

    public CustomUserDetails(String username, String password, Collection<Role> authorities, UserMeta userMeta) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.userMeta = userMeta;
    }
}