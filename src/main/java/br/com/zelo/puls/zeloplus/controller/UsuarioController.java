package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.dto.CriarUsuarioDTO;
import br.com.zelo.puls.zeloplus.dto.PerfilDTO;
import br.com.zelo.puls.zeloplus.dto.PerfilUpdateDTO;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody CriarUsuarioDTO usuario) {
        System.out.println("criar: " + usuario);
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

    @PatchMapping ("/atualizar-senha/{id}")
    public ResponseEntity<?> atualizarSenha(@PathVariable Integer id, @RequestBody String senha) {
        try {
            usuarioService.atualizarSenha(id, senha);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/recuperar-senha")
    public ResponseEntity<?> recuperarSenha(@RequestBody String email) {
        try {
            usuarioService.recuperarSenha(email);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/gerar-vinculo/{id}")
    public ResponseEntity<?> gerarVinculo(@PathVariable Integer id) {
        try {
            usuarioService.gerarVinculo(id);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
