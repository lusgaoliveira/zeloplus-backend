package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.dto.*;
import br.com.zelo.puls.zeloplus.model.Cuidador;
import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.model.TipoUsuario;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.repository.CuidadorRepository;
import br.com.zelo.puls.zeloplus.repository.IdosoRepository;
import br.com.zelo.puls.zeloplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Base64;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final IdosoService idosoService;
    private final CuidadorService cuidadorService;
    private final CuidadorRepository cuidadorRepository;
    private final IdosoRepository idosoRepository;
    private EmailService emailService;
    @Value("${foto.perfil.diretorio:uploads/fotos}")
    private String diretorioFotos;

    public UsuarioService(UsuarioRepository repository, UsuarioRepository usuarioRepository, IdosoService idosoService,
                          CuidadorService cuidadorService, CuidadorRepository cuidadorRepository, IdosoRepository idosoRepository,
                          EmailService emailService) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.idosoService = idosoService;
        this.cuidadorService = cuidadorService;
        this.cuidadorRepository = cuidadorRepository;
        this.idosoRepository = idosoRepository;
        this.emailService = emailService;
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

    @Transactional
    public void atualizarPerfil(Integer usuarioId, PerfilUpdateDTO dto) {
        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza email e foto do usuário
        if (dto.email() != null) {
            usuario.setEmail(dto.email());
        }
        if (dto.fotoPerfil() != null) {
            byte[] fotoBytes = Base64.getDecoder().decode(dto.fotoPerfil());
            usuario.setFotoPerfil(fotoBytes);
        }
        repository.save(usuario);

        // Atualiza dados do idoso ou cuidador
        if (usuario.getTipoUsuario() == TipoUsuario.IDOSO) {
            Idoso idoso = idosoRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Idoso não encontrado"));
            if (dto.nome() != null) {
                idoso.setNome(dto.nome());
            }
            if (dto.dataNascimento() != null) {
                idoso.setDataNascimento(dto.dataNascimento());
            }
            idosoRepository.save(idoso);
        } else {
            Cuidador cuidador = cuidadorRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Cuidador não encontrado"));
            if (dto.nome() != null) {
                cuidador.setNome(dto.nome());
            }
            if (dto.dataNascimento() != null) {
                cuidador.setDataNascimento(dto.dataNascimento());
            }
            cuidadorRepository.save(cuidador);
        }
    }


    public PerfilDTO getPerfil(Integer usuarioId) {
        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String nome = null;
        LocalDate dataNascimento = null;
        Integer idUsuario = null;
        String codigoVinculo = null;

        Optional<Usuario> usuarioOpt = repository.findById(usuarioId);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getTipoUsuario() == TipoUsuario.IDOSO) {
            Optional<Idoso> idosoOpt = idosoRepository.findByUsuario(usuarioOpt.get());
            var idoso = idosoOpt.get();
            nome = idoso.getNome();
            dataNascimento = idoso.getDataNascimento();
            idUsuario = idoso.getId();
            codigoVinculo = idoso.getCodigoVinculo();
        } else {
            // assume que é cuidador
            Cuidador cuidador = cuidadorRepository.findByUsuario(usuarioOpt.get())
                    .orElseThrow(() -> new RuntimeException("Dados do cuidador não encontrados"));

            nome = cuidador.getNome();
            dataNascimento = cuidador.getDataNascimento();
            idUsuario = cuidador.getId();
            codigoVinculo = cuidador.getCodigoVinculo();
        }

        String fotoBase64 = "";
        if (usuario.getFotoPerfil() != null) {
            fotoBase64 = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
        }

        return new PerfilDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                fotoBase64,
                usuario.getEmail(),
                nome,
                dataNascimento,
                usuario.getTipoUsuario().toString(),
                idUsuario,
                codigoVinculo
        );
    }

    public void atualizarSenha(Integer idUsuario, String senha) {
        Usuario usuario = repository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setSenha(senha);
        repository.save(usuario);
    }


    public void recuperarSenha(String email) {
        Usuario usuario = repository.findByEmail(email);

        if (usuario != null) {
            var senha = gerarSenhaAleatoria();
            usuario.setSenha(senha);
            repository.save(usuario);

            String assunto = "Nova senha solicitada na recuperação de senha";
            String mensagem = "Olá, você solicitou uma nova senha no nosso aplicativo Zelo+. " +
                    "Uma senha aleatória foi gerada e está disponível abaixo. " +
                    "Lembre-se de alterá-la no aplicativo:\n\n" + senha;

            emailService.enviarEmail(email, assunto, mensagem);
            return;
        }

        throw new IllegalArgumentException("Email não encontrado!");
    }


    private String gerarSenhaAleatoria() {
        SecureRandom random = new SecureRandom();
        int numero = random.nextInt(100_000_000);
        return String.format("%08d", numero);
    }

    public void gerarVinculo(Integer id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado!")
        );

        if (usuario.getTipoUsuario() != TipoUsuario.IDOSO) {
            throw new IllegalArgumentException("Usuário não é do tipo idoso");
        }
        var idoso = idosoRepository.findByUsuario(usuario).orElseThrow(
                () -> new IllegalArgumentException("Idoso não encontrado!")
        );

        idoso.setCodigoVinculo(idosoService.gerarCodigoVinculo(idoso.getId()));
        idosoRepository.save(idoso);
    }
}
