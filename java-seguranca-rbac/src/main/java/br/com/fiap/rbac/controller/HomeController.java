package br.com.fiap.rbac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/admin")
    public String adminPage() {
        return "<h1>ADMIN, você está acessando a área administrativa do app!</h1>";
    }

    @GetMapping("/user")
    public String userPage() {
        return "<h1>USER, você está acessando a área geral do app!</h1>";
    }

    @GetMapping("/public")
    public String publicPage() {
        return "<h1>DESCONHECIDO, você está acessando a área geral do app!</h1>";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
