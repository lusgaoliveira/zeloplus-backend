package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.dto.CriarUsuarioDTO;
import br.com.zelo.puls.zeloplus.dto.PerfilDTO;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.service.UsuarioService;
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

    @PostMapping(value = "/{id}/foto", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFoto(@PathVariable Integer id,
                                             @RequestParam("foto") MultipartFile foto) {
        String url = usuarioService.salvarFotoPerfil(id, foto);
        return ResponseEntity.ok(url);
    }

    @GetMapping("busca/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok().body(usuarioService.buscarPorId(id));

    }

    @PostMapping("atualiza/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok().body(usuarioService.atualizar(usuario));
    }

    @GetMapping("perfil/{id}")
    public PerfilDTO buscarPorId(@PathVariable Integer id) {
        return usuarioService.getPerfil(id);
    }
}
