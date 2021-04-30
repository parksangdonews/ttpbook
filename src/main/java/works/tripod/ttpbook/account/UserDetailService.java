package works.tripod.ttpbook.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import works.tripod.ttpbook.model.Account;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("loadUserByUsername");
        log.debug(username);

        Account account = accountRepository.findByEmail(username);

        if(account == null) {
            throw new UsernameNotFoundException("Not Found..");
        }

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                GrantedAuthority grantedAuthority = () -> "ROLE_USER"; // 또는 DB 에서 가져옴.
                return Arrays.asList(grantedAuthority);
            }
            @Override
            public String getPassword() {
                return account.getPassword();
            }
            @Override
            public String getUsername() {
                return account.getEmail();
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
        };

        return userDetails;
    }
}
