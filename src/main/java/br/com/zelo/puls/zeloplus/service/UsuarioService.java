package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.dto.*;
import br.com.zelo.puls.zeloplus.model.Cuidador;
import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.model.TipoUsuario;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.repository.CuidadorRepository;
import br.com.zelo.puls.zeloplus.repository.IdosoRepository;
import br.com.zelo.puls.zeloplus.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.Base64;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final IdosoService idosoService;
    private final CuidadorService cuidadorService;
    private final CuidadorRepository cuidadorRepository;
    private final IdosoRepository idosoRepository;

    @Value("${foto.perfil.diretorio:uploads/fotos}")
    private String diretorioFotos;

    public UsuarioService(UsuarioRepository repository, UsuarioRepository usuarioRepository, IdosoService idosoService, CuidadorService cuidadorService, CuidadorRepository cuidadorRepository, IdosoRepository idosoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.idosoService = idosoService;
        this.cuidadorService = cuidadorService;
        this.cuidadorRepository = cuidadorRepository;
        this.idosoRepository = idosoRepository;
    }


    public LoginRespostaDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(dto.nomeUsuario());

        if (!usuario.getSenha().equals(dto.senha())) {
            throw new RuntimeException("Senha inválida");
        }

        String tipoUsuario = usuario.getTipoUsuario().name();
        Integer codigo = null;
        String nome;

        if (usuario.getTipoUsuario() == TipoUsuario.CUIDADOR) {
            Cuidador cuidador = cuidadorRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Cuidador não encontrado para o usuário"));

            nome = cuidador.getNome();

            if (cuidador.getIdoso() != null) {
                codigo = cuidador.getIdoso().getId();
            }
        } else if (usuario.getTipoUsuario() == TipoUsuario.IDOSO) {
            Idoso idoso = idosoRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Idoso não encontrado para o usuário"));

            nome = idoso.getNome();
        } else {
            throw new RuntimeException("Tipo de usuário desconhecido");
        }

        // Converte o byte[] da imagem para Base64 (caso tenha imagem)
        String fotoPerfilBase64 = null;
        if (usuario.getFotoPerfil() != null) {
            fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
        }

        return new LoginRespostaDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                fotoPerfilBase64,
                nome,
                tipoUsuario,
                codigo
        );
    }

    public Usuario salvar(CriarUsuarioDTO dto) {
        byte[] fotoPerfilBytes = null;

        if (dto.fotoPerfil() != null && !dto.fotoPerfil().isEmpty()) {
            fotoPerfilBytes = Base64.getDecoder().decode(dto.fotoPerfil());
        }

        var usuario = new Usuario(
                null,
                dto.nomeUsuario(),
                dto.senha(),
                TipoUsuario.valueOf(dto.tipoUsuario()),
                dto.email(),
                fotoPerfilBytes
        );

        repository.save(usuario);

        if (dto.codigoVinculo() == null || dto.codigoVinculo().isEmpty()) {
            idosoService.salvar(new CriarIdosoDTO(dto.nome(), dto.dataNascimento()), usuario);
        } else {
            Idoso idosoVinculado = idosoService.buscarPorCodigo(dto.codigoVinculo())
                    .orElseThrow(() -> new IllegalArgumentException("Código para vínculo não encontrado"));

            cuidadorService.salvar(new CriarCuidadorDTO(dto.nome(), dto.dataNascimento(), dto.codigoVinculo()), idosoVinculado, usuario);
        }

        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Usuario atualizar(Usuario usuario) {
        return repository.save(usuario);
    }

    public String salvarFotoPerfil(Integer usuarioId, MultipartFile foto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (foto.isEmpty()) {
            throw new IllegalArgumentException("Arquivo inválido");
        }

        try {
            byte[] imagemBytes = foto.getBytes();
            usuario.setFotoPerfil(imagemBytes);
            usuarioRepository.save(usuario);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar imagem", e);
        }

        return "Foto de perfil salva com sucesso.";
    }
}
