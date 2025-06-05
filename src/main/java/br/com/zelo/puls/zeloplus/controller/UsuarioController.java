package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.dto.CriarUsuarioDTO;
import br.com.zelo.puls.zeloplus.dto.PerfilDTO;
import br.com.zelo.puls.zeloplus.dto.PerfilUpdateDTO;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody CriarUsuarioDTO usuario) {
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }


    @GetMapping("busca/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok().body(usuarioService.buscarPorId(id));

    }

    @PatchMapping ("/{id}/perfil")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Integer id, @RequestBody PerfilUpdateDTO dto) {
        try {
            usuarioService.atualizarPerfil(id, dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("perfil/{id}")
    public PerfilDTO buscarPorId(@PathVariable Integer id) {
        return usuarioService.getPerfil(id);
    }
}
