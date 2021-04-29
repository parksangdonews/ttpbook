package works.tripod.ttpbook.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import works.tripod.ttpbook.model.Account;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    // auth basic 추후 빈 설정
    // private final AuthenticationManager authenticationManager;


    @Transactional  // persist status
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .emailVerified(false)
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .build();

        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("회원 가입인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public void login(Account account) {

        List<GrantedAuthority> roleList = new LinkedList<>();
        roleList.add(new SimpleGrantedAuthority("ROLE_USER"));

        // temp process
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                account.getEmail(),
                account.getPassword(),
                roleList);

        /*
        // basic solution
        UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(
                account.getNickname(),
                account.getPassword());
        // authenticationManager 을 주입 받아 사용
        AuthenticationManager authenticationManager = authenticationManager.authenticate(token);
        */

        SecurityContextHolder.getContext().setAuthentication(token);

    }
}
