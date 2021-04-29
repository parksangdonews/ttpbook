package works.tripod.ttpbook.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import works.tripod.ttpbook.account.AccountRepository;
import works.tripod.ttpbook.model.Account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @MockBean
    JavaMailSender javaMailSender;


    @DisplayName("회원가입 화면 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));

    }

    @DisplayName("회원가입 시 입력값 오류")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .with(csrf())  // implementation 'org.springframework.security:spring-security-test' 추가 필요 , https://spring.io/guides/gs/securing-web/
                .param("nickname", "sangdo")
                .param("email", "email..")
                .param("password", "abcde"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());

    }


    @DisplayName("회원가입 시 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .with(csrf())  // implementation 'org.springframework.security:spring-security-test' 추가 필요 , https://spring.io/guides/gs/securing-web/
                .param("nickname", "SANGDO")
                .param("email", "nsu@daum.net")
                .param("password", "12345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        Account account = accountRepository.findByEmail("nsu@daum.net");
        assertNotNull(account);
        assertNotNull(account.getEmailCheckToken()); // token
        assertNotEquals(account.getPassword(), "12345678"); //cause by encoding.

        assertTrue(accountRepository.existsByEmail("nsu@daum.net"));
        then(javaMailSender).should().send(any(SimpleMailMessage.class));

    }

    @DisplayName("인증메일 확인, 입력값 오류")
    @Test
    void checkEmailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token", "blahblah")
                .param("email", "nsu@daum.net"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated());
    }


    @DisplayName("인증메일 확인, 입력값 정상")
    @Test
    void checkEmailToken() throws Exception {

        // 조회해서 없으면 만들어서 ... 테스트
        Account account = accountRepository.findByEmail("nsu@daum.net");
        if(account == null) {
           account = Account.builder()
                   .email("nsu@daum.net")
                   .password(passwordEncoder.encode("12345678"))
                   .nickname("SANGDO")
                   .build();
           account.generateEmailCheckToken();
           accountRepository.save(account);
        }

        mockMvc.perform(get("/check-email-token")
                .param("token", account.getEmailCheckToken())
                .param("email", "nsu@daum.net"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername("SANGDO"));

    }

}
