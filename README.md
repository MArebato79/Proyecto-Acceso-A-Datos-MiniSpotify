# 🎧 MiniSpotify - Backend API REST

**MiniSpotify** es una API RESTful de alto rendimiento construida con **Java 21** y **Spring Boot 3**, diseñada para gestionar una plataforma de streaming de música. El sistema implementa una arquitectura monolítica modular, seguridad basada en tokens (JWT) y persistencia relacional optimizada.

## 🛠️ Stack Tecnológico

| Componente | Tecnología | Versión | Propósito |
| :--- | :--- | :--- | :--- |
| **Core** | Java | 21 (LTS) | Lenguaje base con Records y Pattern Matching |
| **Framework** | Spring Boot | 3.2.2 | Inyección de dependencias y configuración |
| **Seguridad** | Spring Security | 6.2 | Autenticación y Autorización (RBAC) |
| **Auth** | JJWT | 0.11.5 | Generación y validación de JSON Web Tokens |
| **Persistencia** | Spring Data JPA | - | Abstracción de repositorios |
| **ORM** | Hibernate | 6.4 | Mapeo Objeto-Relacional |
| **Base de Datos** | MySQL | 8.0 | Almacenamiento relacional (InnoDB) |
| **Herramientas** | Lombok | 1.18 | Reducción de boilerplate code |
| **Build** | Maven | 3.x | Gestión de dependencias y ciclo de vida |

## 🏗️ Arquitectura del Sistema

El proyecto sigue una arquitectura de capas clásica, desacoplando la lógica de negocio de la exposición de datos:

1.  **Controller Layer (`/controller`)**: Maneja las peticiones HTTP, valida los DTOs de entrada (`@Valid`) y gestiona las respuestas `ResponseEntity`.
2.  **Service Layer (`/service`)**: Contiene la lógica de negocio, validaciones de reglas de dominio y transaccionalidad (`@Transactional`).
3.  **Repository Layer (`/repositories`)**: Interfaces que extienden de `JpaRepository` para la interacción directa con la base de datos.
4.  **Security Layer (`/security`)**: Filtros y configuraciones para interceptar peticiones y gestionar el contexto de seguridad.

### Modelo de Datos (ER)
* **Usuario ↔ Artista**: Relación 1:1. Un usuario puede "ascender" a artista, compartiendo datos de perfil.
* **Canción ↔ Artista**: Relación N:1 (Autoría) y N:M (Colaboraciones mediante tabla intermedia).
* **Playlist ↔ Canción**: Relación N:M gestionada mediante la entidad explícita `EntradaPlaylist`, que registra la fecha y posición de cada pista.

## 🔌 API Endpoints

Todos los endpoints (excepto Auth y Registro) requieren el header: `Authorization: Bearer <TOKEN>`.

### 🔐 Autenticación & Usuarios
| Método | Ruta | Rol Requerido | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/login` | Público | Autentica credenciales y devuelve el JWT. |
| `POST` | `/usuarios/register` | Público | Registra un nuevo usuario estándar. |
| `PUT` | `/usuarios/update` | AUTH | Actualiza perfil (Username, Foto) y sincroniza con Artista si aplica. |

### 🎵 Canciones (`/canciones`)
| Método | Ruta | Rol Requerido | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/canciones` | AUTH | Lista todas las canciones (Soporta paginación). |
| `GET` | `/canciones/search` | AUTH | Busca por título: `?filtro=texto` (Soporta paginación). |
| `GET` | `/canciones/artista/{id}` | AUTH | Obtiene todas las canciones de un artista específico. |
| `POST` | `/canciones` | **ARTIST** | Crea una nueva canción y la asigna al artista logueado. |
| `PUT` | `/canciones/{id}` | **ARTIST** | Modifica datos de la canción (Solo el autor). |
| `PUT` | `/canciones/{id}/estado` | **ARTIST** | Cambia visibilidad (`publica`: true/false). |
| `DELETE` | `/canciones/{id}` | **ARTIST** | Elimina una canción (Solo el autor). |

### 🤝 Colaboraciones
| Método | Ruta | Rol Requerido | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/canciones/{id}/colaboradores` | **ARTIST** | Añade IDs de artistas colaboradores a una canción. |
| `DELETE` | `/canciones/{id}/colaboradores/{idArt}`| **ARTIST** | Elimina a un colaborador de la canción. |

### 📜 Playlists (`/playlists`)
| Método | Ruta | Rol Requerido | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/playlists` | AUTH | Obtiene las playlists del usuario actual (Paginado). |
| `POST` | `/playlists` | AUTH | Crea una nueva playlist vacía. |
| `PUT` | `/playlists/{id}` | AUTH | Actualiza título, descripción o estado de la playlist. |
| `DELETE` | `/playlists/{id}` | AUTH | Elimina una playlist completa (Solo el dueño). |
| `POST` | `/playlists/{idP}/canciones/{idC}` | AUTH | Añade una canción a la playlist. |
| `DELETE` | `/playlists/{idP}/canciones/{idC}` | AUTH | Elimina una canción de la playlist. |

## ⚙️ Configuración y Ejecución

### 1. Requisitos Previos
* Tener **MySQL** corriendo en el puerto `3306`.
* Crear una base de datos vacía llamada `MiniSpotify`.

### 2. Configuración de Entorno
El archivo `src/main/resources/application.properties` debe contener tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/MiniSpotify?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tu_contraseña_aqui
spring.jpa.hibernate.ddl-auto=update