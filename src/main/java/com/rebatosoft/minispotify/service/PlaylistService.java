package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.EntradaPlaylistDto;
import com.rebatosoft.minispotify.dto.PlaylistDto;
import com.rebatosoft.minispotify.dto.UsuarioDto;
import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.PlaylistBasicDto;
import com.rebatosoft.minispotify.dto.requests.PlaylistRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.entities.componentes.EntradaPlaylist;
import com.rebatosoft.minispotify.entities.componentes.Playlist;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.PlaylistRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.EntradaPlaylistRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final CancionRepository cancionRepository;
    private final EntradaPlaylistRepository entradaPlaylistRepository;
    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PlaylistDto crearPlaylist(PlaylistRequest playlist) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Playlist list = new Playlist();
        list.setTitulo(playlist.nombre());
        list.setPublica(playlist.publica() != null ? playlist.publica() : true);
        list.setUsuario(usuario);
        list.setFoto(playlist.imagenUrl());
        list.setDescripcion(playlist.descripcion());

        return convertirADto(playlistRepository.save(list));

    }

    @Transactional
    public PlaylistDto updatePlaylist(PlaylistRequest playlistRequest,Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();
        Playlist playlist = playlistRepository.findById(Integer.parseInt(String.valueOf(id)))
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        playlist.setTitulo(playlistRequest.nombre());
        playlist.setDescripcion(playlistRequest.descripcion());
        if (playlistRequest.publica() != null) playlist.setPublica(playlist.getPublica());
        playlist.setFoto(playlistRequest.imagenUrl());

        return convertirADto(playlistRepository.save(playlist));

    }

    @Transactional
    public void deletePlaylist(Long idPlaylist) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Playlist playlist = playlistRepository.findById(idPlaylist.intValue())
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        if (!playlist.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta playlist");
        }

        if (usuario.getPlaylists() != null) {
            usuario.getPlaylists().removeIf(p -> p.getId().equals(playlist.getId()));
        }

        playlistRepository.delete(playlist);
    }

    @Transactional
    public void añadirCancionPlaylist(Long idCancion , Long idPlaylist){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Playlist playlist = playlistRepository.findById(Integer.parseInt(String.valueOf(idPlaylist)))
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        if (!playlist.getUsuario().getCorreo().equals(email)) {
            throw new RuntimeException("No tienes permiso para editar esta playlist");
        }

        Cancion cancion = cancionRepository.findById(idCancion)
                .orElseThrow(() -> new RuntimeException("cancion no encontrada"));

        EntradaPlaylist entrada = new EntradaPlaylist();
        entrada.setPlaylist(playlist);
        entrada.setCancion(cancion);
        entrada.setFechaIntroduccion(LocalDate.now());


        int nuevaPosicion = playlist.getCancionesEntradas().size() + 1;
        entrada.setPosicionCancion(nuevaPosicion);

        entradaPlaylistRepository.save(entrada);
    }

    public Page<PlaylistDto> getMyPlaylist(Pageable pageable){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        return playlistRepository.findByUsuario(usuario, pageable)
                .map(this::convertirADto);
    }

    public PlaylistDto getPlaylistById(Long id){
        PlaylistDto playlistDto=convertirADto(playlistRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada")));
        return playlistDto;
    }

    public List<PlaylistDto> getAllPublicPlaylists() {
        List<Playlist> playlists = playlistRepository.findByPublicaTrue();

        return playlists.stream()
                .map(this::convertirADto) // Reutilizamos tu método convertirADto
                .collect(Collectors.toList());
    }


    // En PlaylistService.java

    @Transactional
    public void eliminarCancionPlaylist(Long idCancion, Long idPlaylist) {

        Playlist playlist = playlistRepository.findById(idPlaylist.intValue())
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        boolean eliminado = playlist.getCancionesEntradas().removeIf(entrada ->
                entrada.getCancion().getId() == idCancion.longValue()
        );

        if (!eliminado) {
            throw new RuntimeException("La canción no estaba en la playlist");
        }
        playlistRepository.save(playlist);
    }

    private PlaylistBasicDto convertirBasicDto(PlaylistDto playlistDto){
        PlaylistBasicDto basicDto = new PlaylistBasicDto();
            basicDto.setId(playlistDto.getId());
            basicDto.setNombre(playlistDto.getNombre());
            basicDto.setImagenUrl(playlistDto.getImagenUrl());

            return  basicDto;
    }


    private PlaylistDto convertirADto(Playlist playlist) {
        PlaylistDto dto = new PlaylistDto();
        dto.setId(String.valueOf(playlist.getId()));
        dto.setNombre(playlist.getTitulo());
        dto.setImagenUrl(playlist.getFoto());
        dto.setPublica(playlist.getPublica());

        String nombreCreador = "Desconocido";
        if (playlist.getUsuario() != null) {
            if (playlist.getUsuario().getRealUsername() != null && !playlist.getUsuario().getRealUsername().isEmpty()) {
                nombreCreador = playlist.getUsuario().getRealUsername();
            } else {
                nombreCreador = playlist.getUsuario().getCorreo();
            }
        }
        dto.setCreador(nombreCreador);

        List<EntradaPlaylistDto> entradasDto = new ArrayList<>();
        if (playlist.getCancionesEntradas() != null) {
            entradasDto = playlist.getCancionesEntradas().stream().map(entrada -> {
                EntradaPlaylistDto entradaDto = new EntradaPlaylistDto();
                entradaDto.setId(String.valueOf(entrada.getId()));
                entradaDto.setFechaAgregado(String.valueOf(entrada.getFechaIntroduccion()));

                Cancion c = entrada.getCancion();
                if (c != null) {
                    // Fallback imagen
                    String img = c.getFoto();
                    if ((img == null || img.isEmpty()) && c.getAlbum() != null) {
                        img = c.getAlbum().getFoto();
                    }

                    // Colaboradores
                    List<ArtistaBasicDto> colabs = new ArrayList<>();
                    if (c.getColaboraciones() != null) {
                        colabs = c.getColaboraciones().stream().map(col -> new ArtistaBasicDto(
                                String.valueOf(col.getArtistaColaborador().getId()),
                                col.getArtistaColaborador().getNombre(),
                                col.getArtistaColaborador().getFoto()
                        )).collect(Collectors.toList());
                    }

                    CancionBasicDto cancionBasic = new CancionBasicDto();
                    cancionBasic.setId(String.valueOf(c.getId()));
                    cancionBasic.setTitulo(c.getTitulo());
                    cancionBasic.setImagenUrl(img);
                    cancionBasic.setGenero(c.getGenero() != null ? c.getGenero().name() : "POP");
                    cancionBasic.setDuracion(0);
                    cancionBasic.setArtistaNombre(c.getAutor() != null ? c.getAutor().getNombre() : "Desconocido");
                    cancionBasic.setColaboradores(colabs);

                    entradaDto.setCancion(cancionBasic);
                }
                return entradaDto;
            }).collect(Collectors.toList());
        }
        dto.setCancionesEntradas(entradasDto);
        return dto;
    }
}
