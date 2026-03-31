package com.appbarber.api.model;

import com.appbarber.api.model.enums.TipoConta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "usuario_nome", nullable = false)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O formato do email é inválido")
    @Column(name = "usuario_email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Column(name = "usuario_senha", nullable = false)
    private String senha;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "usuario_telefone", nullable = false)
    private String telefone;

    @NotNull(message = "O tipo de conta é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // aqui vai ver se a pessoa é DONO ou CLIENTE
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.tipoConta.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                '}';
    }
}
