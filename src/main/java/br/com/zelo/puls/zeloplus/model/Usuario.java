package br.com.zelo.puls.zeloplus.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Column(unique = true, nullable = false)
    private String nomeUsuario;

    @NotBlank(message = "Senha é obrigatória")
    @Length(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "foto_perfil")
    private byte[] fotoPerfil;;

    @Column(name = "token_expo", nullable = false)
    private String tokenExpo;
}