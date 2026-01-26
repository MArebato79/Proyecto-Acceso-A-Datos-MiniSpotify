package com.rebatosoft.minispotify.entities;

import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.entities.componentes.Follow;
import com.rebatosoft.minispotify.entities.componentes.Historial;
import com.rebatosoft.minispotify.entities.componentes.Playlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String correo;
    private String contrasena;

    private String fotoUrl;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuario",fetch = FetchType.EAGER)
    private List<Playlist> playlists;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuario",fetch = FetchType.LAZY)
    private List<Historial> cancionesEscuchadas;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuarioSeguidor",fetch = FetchType.LAZY)
    private List<Follow> follows;

    @OneToOne(mappedBy = "usuarioPropietario", cascade = CascadeType.ALL)
    private Artista datosArtista;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Si tiene perfil de artista, le damos el rol de ARTISTA
        if (this.datosArtista != null) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ARTISTA"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        // Si no, solo es un usuario normal
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
