package br.com.autenticacaooauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpController {

    @GetMapping("/public")
    public String publicRoute() {
        return "<h1>Rota pública, sinta-s livre para olhar!</h1>";
    }

    @GetMapping("/private")
    public String privateRoute(@AuthenticationPrincipal OidcUser principal) {
        return String.format(
                "<h1>Rota privada, somente pessoas autorizadas<h1>"+
                        "<h3>Principal: %s</h3>" +
                        "<h3>E-mail: %s</h3>" +
                        "<h3>Authorities: %s</h3>" +
                        "<h3>JWT: %s</h3>",
                principal,
                principal.getAttribute("email"),
                principal.getAuthorities(),
                principal.getIdToken().getTokenValue()
        );

    }

    @GetMapping("/jwt")
    public String privateJwt(@AuthenticationPrincipal Jwt jwt) {
        return String.format(
                "<h1>JWT<h1>"+
                        "<h3>Principal: %s<h3>"+
                        "<h3>E-mail: %s<h3>"+
                        "<h3>JWT: %s<h3>",
                jwt.getClaims(),
                jwt.getClaim("email"),
                jwt.getTokenValue());
    }
}
