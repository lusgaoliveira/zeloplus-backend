package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.dto.LoginDTO;
import br.com.zelo.puls.zeloplus.dto.LoginRespostaDTO;
import br.com.zelo.puls.zeloplus.service.UsuarioService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("login")
    public LoginRespostaDTO criar(@RequestBody @Validated LoginDTO dto) {
        return usuarioService.login(dto);
    }

}
