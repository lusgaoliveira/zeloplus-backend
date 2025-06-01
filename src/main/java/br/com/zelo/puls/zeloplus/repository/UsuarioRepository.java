package br.com.zelo.puls.zeloplus.repository;

import br.com.zelo.puls.zeloplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByEmail(String email);
    Usuario findByNomeUsuario(String nomeUsuario);
}
