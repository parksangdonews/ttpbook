package works.tripod.ttpbook.account;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import works.tripod.ttpbook.model.Account;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;


    @InitBinder("signUpForm") // 해당 객체를 받을 때 바인딩(끼워넣기) 해주는 역할
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        // model.addAttribute("signUpForm", new SignUpForm());  // same with next line ( using cameCase name )
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors) {  // @ModelAttibute 생략가능
        // signUpFormValidator.validate(signUpForm, errors); // initBinder 로 대체
        if (errors.hasErrors()) {
            log.debug(errors.getAllErrors().toString());
            return "account/sign-up";
        }

        // 가입처리
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);


        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {

        Account account = accountRepository.findByEmail(email);

        String view = "account/checked-email";

        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if (account.isEmailVerified()) {
            model.addAttribute("verified", "wrong.emailVerified");
            return view;
        }

        // sign-up complete.
        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        accountRepository.save(account);

        // login
        accountService.login(account);

        return view;
    }

    @GetMapping("/login")
    public String login() {
        log.debug("login");
        return "login";
    }



    /*


    @PostMapping("/login")
    public String loginProcess(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                                Model model) {

        String errorMessage = "";
        errorMessage = error != null ? "error" : "ok";
        errorMessage = logout != null ? "logged out" : "";

        model.addAttribute("errorMessage", errorMessage);

        return "login";


    }
*/



    /*
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
    */

}
