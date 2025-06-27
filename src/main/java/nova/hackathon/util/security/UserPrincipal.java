package nova.hackathon.util.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nova.hackathon.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final UUID uuid;
    private final String email;
    private final String password;
    private final String fullName;
    private final String role;
    private final Boolean introStatus;

    public UserPrincipal(String email, UUID uuid, String password, String fullName, String role, Boolean introStatus) {
        this.email = email;
        this.uuid = uuid;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.introStatus = introStatus;
    }

    public UserPrincipal(String email, String role, Boolean introStatus) {
        this.email = email;
        this.role = role;
        this.uuid = null;
        this.password = null;
        this.fullName = null;
        this.introStatus = introStatus;
    }

    public static UserPrincipal fromMember(Member member){
        return new UserPrincipal(
                member.getEmail(),
                member.getUuid(),
                member.getPassword(),
                member.getName(),
                String.valueOf(member.getRole()),
                member.getIntroStatus()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
