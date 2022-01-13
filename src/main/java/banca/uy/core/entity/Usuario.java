package banca.uy.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.Hashing;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "Usuario")
public class Usuario extends Entidad {

    private String nombre;

    private String apellido;

    private String email;

    private String usuario;

    private String contrasena;

    private Boolean reseteoContrasena;

    private Boolean validado;

    private Boolean validadoParaEmpresa;

    public Boolean getValidadoParaEmpresa() {
        return this.validadoParaEmpresa;
    }

    public void setValidadoParaEmpresa(Boolean validado) {
        this.validadoParaEmpresa = validado;
    }

    private Boolean esAdministradorSistema;

    private Boolean activo;

    @JsonProperty
    @DBRef(lazy = true)
    private Set<Rol> roles = new HashSet<Rol>();

    private Set<String> sroles = new HashSet<String>();

    private Set<String> susuariosEmpresas = new HashSet<String>(0);

    private Set<String> sempresas = new HashSet<String>();

    public Usuario() {
        super();
    }

    /**
     *
     * Método utilizado para establecer los valores predeterminados básicos cuando
     * se crea un nuevo {@link "Usuario"}
     *
     */
    public void setParametersForRegister() {
        this.activo = true;
        this.esAdministradorSistema = false;
        this.validado = false;
        this.reseteoContrasena = false;
        this.fechaCreacion = new DateTime();
        this.fechaEdicion = new DateTime();
        for (Rol rol: this.roles) {
            this.sroles.add(rol.getId());
        }
        if (this.contrasena != null) {
            String sha256hex = Hashing.sha256().hashString(this.contrasena, StandardCharsets.UTF_8).toString();
            this.contrasena = sha256hex;
        }

    }

    public Usuario(String nombre, String email, String usuario, String contrasena, Boolean reseteoContrasena,
                    Boolean validado, Boolean activo, DateTime fechaCreacion, DateTime fechaEdicion) {
        super();
        this.nombre = nombre;
        this.email = email;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.reseteoContrasena = reseteoContrasena;
        this.validado = validado;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaEdicion = fechaEdicion;
    }

    public Usuario(Usuario usuario) {
        super();
        this.oldId = usuario.getOldId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.usuario = usuario.getUsername();
        this.contrasena = usuario.getContrasena();
        this.reseteoContrasena = usuario.getReseteoContrasena();
        this.validado = usuario.getValidado();
        this.activo = usuario.getActivo();
        this.fechaCreacion = usuario.getFechaCreacion();
        this.fechaEdicion = usuario.getFechaEdicion();
        this.eliminado = usuario.getEliminado();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getUsername() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @JsonIgnore
    public String getContrasena() {
        return contrasena;
    }

    @JsonIgnore
    public String getPassword() {
        return "{SHA-256}" + contrasena;
    }

    @JsonProperty
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * TODO verificar si es conveniente este método aquí...
     *
     * @param contrasena
     */
    public void encryptAndSetContrasena(String contrasena) {
        String sha256hex = Hashing.sha256().hashString(contrasena, StandardCharsets.UTF_8).toString();
        this.contrasena = sha256hex;
    }

    @JsonIgnore
    public Boolean getReseteoContrasena() {
        return reseteoContrasena;
    }

    public void setReseteoContrasena(Boolean reseteoContrasena) {
        this.reseteoContrasena = reseteoContrasena;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public Boolean esAdministradorSistema() {
        return esAdministradorSistema;
    }

    public void setEsAdministradorSistema(Boolean admin) {
        this.esAdministradorSistema = admin;
    }

    public DateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public DateTime getFechaEdicion() {
        return fechaEdicion;
    }

    public Boolean getEsAdministradorSistema() {
        return esAdministradorSistema;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public Set<String> getSroles() {
        return sroles;
    }

    public void setSroles(Set<String> sroles) {
        this.sroles = sroles;
    }

    @JsonIgnore()
    public Set<String> getSusuariosEmpresas() {
        return susuariosEmpresas;
    }

    public void setSusuariosEmpresas(Set<String> susuariosEmpresas) {
        this.susuariosEmpresas = susuariosEmpresas;
    }

    @JsonIgnore()
    public Set<String> getSempresas() {
        return sempresas;
    }

    public void setSempresas(Set<String> sempresas) {
        this.sempresas = sempresas;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activo == null) ? 0 : activo.hashCode());
        result = prime * result + ((contrasena == null) ? 0 : contrasena.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
        result = prime * result + ((fechaEdicion == null) ? 0 : fechaEdicion.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((reseteoContrasena == null) ? 0 : reseteoContrasena.hashCode());
        result = prime * result + ((validado == null) ? 0 : validado.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Usuario other = (Usuario) obj;
        if (activo == null) {
            if (other.activo != null) {
                return false;
            }
        } else if (!activo.equals(other.activo)) {
            return false;
        }
        if (contrasena == null) {
            if (other.contrasena != null) {
                return false;
            }
        } else if (!contrasena.equals(other.contrasena)) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (fechaCreacion == null) {
            if (other.fechaCreacion != null) {
                return false;
            }
        } else if (!fechaCreacion.equals(other.fechaCreacion)) {
            return false;
        }
        if (fechaEdicion == null) {
            if (other.fechaEdicion != null) {
                return false;
            }
        } else if (!fechaEdicion.equals(other.fechaEdicion)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (nombre == null) {
            if (other.nombre != null) {
                return false;
            }
        } else if (!nombre.equals(other.nombre)) {
            return false;
        }
        if (reseteoContrasena == null) {
            if (other.reseteoContrasena != null) {
                return false;
            }
        } else if (!reseteoContrasena.equals(other.reseteoContrasena)) {
            return false;
        }
        if (validado == null) {
            if (other.validado != null) {
                return false;
            }
        } else if (!validado.equals(other.validado)) {
            return false;
        }
        return true;
    }

}
