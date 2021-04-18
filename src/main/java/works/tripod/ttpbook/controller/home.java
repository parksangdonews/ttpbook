package works.tripod.ttpbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class home {

    @GetMapping(path = {"/", "/index"})
    public String home() {
        log.info("index");
        return "index";
    }

    @GetMapping(path = {"/link/{simpleLink}"})
    public String home(@PathVariable String simpleLink) {
        log.info(simpleLink);
        return simpleLink;
    }

}
