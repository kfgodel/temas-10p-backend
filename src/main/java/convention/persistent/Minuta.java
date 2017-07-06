package convention.persistent;

import convention.rest.api.tos.TemaTo;
import convention.rest.api.tos.UserTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fede on 04/07/17.
 */
public class Minuta {
    private String fecha;
    private List<UserTo> listaDeUsuarios;
    private List<TemaTo> listaDeTemas;
    private List<Usuario> asistentes;

    public Minuta(String fecha,List<UserTo> listaDeUsuarios, List<TemaTo> listaDeTemas){

        this.fecha = fecha;
        this.listaDeUsuarios = listaDeUsuarios;
        this.listaDeTemas = listaDeTemas;
    }

    public Minuta() {}

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<UserTo> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    public void setListaDeUsuarios(List<UserTo> listaDeUsuarios) {
        this.listaDeUsuarios = listaDeUsuarios;
    }

    public List<TemaTo> getListaDeTemas() {
        return listaDeTemas;
    }

    public void setListaDeTemas(List<TemaTo> listaDeTemas) {
        this.listaDeTemas = listaDeTemas;
    }

    public static Minuta create() {
        Minuta nuevaMinuta = new Minuta();
        nuevaMinuta.asistentes = new ArrayList<>();
        return nuevaMinuta;
    }

    public List<Usuario> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<Usuario> asistentes) {
        this.asistentes = asistentes;
    }

    public void agregarAsistente(Usuario unUsuario) {
        asistentes.add(unUsuario);
    }

    public void quitarAsistente(Usuario unUsuario) {
        asistentes.remove(unUsuario);
    }
}
