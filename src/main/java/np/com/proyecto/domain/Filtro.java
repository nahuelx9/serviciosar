
package np.com.proyecto.domain;


public class Filtro {
    String nombre = "";
    String provincia = "";
    String departamento= "";
    String horario ="No especificar";
    String precio ="0";

    public Filtro() {
    }
        
    public Filtro(String provincia, String departamento, String horario, String precio) {
        this.provincia = provincia;
        this.departamento = departamento;
        this.horario = horario;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Filtro{" + "nombre=" + nombre + ", provincia=" + provincia + ", departamento=" + departamento + ", horario=" + horario + ", precio=" + precio + '}';
    }

    
        
}
