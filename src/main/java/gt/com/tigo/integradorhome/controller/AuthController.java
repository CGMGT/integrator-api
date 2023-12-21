package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @GetMapping("")
    public RedirectView auth() {
        return new RedirectView(this.authService.getAuthorizationUrl());
    }

    @GetMapping("/getRedirectUrl")
    public String getRedirectUrl() {
        return this.authService.getAuthorizationUrl();
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code) {
        try {
            return this.authService.getToken(code);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return "ERROR";
        }
    }

}
