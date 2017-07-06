package convention.persistent;

import convention.rest.api.tos.TemaTo;
import convention.rest.api.tos.UserTo;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by fede on 04/07/17.
 */
public class Minuta {
    private String fecha;
    private List<UserTo> listaDeUsuarios;
    private List<TemaTo> listaDeTemas;

    public Minuta(String fecha,List<UserTo> listaDeUsuarios, List<TemaTo> listaDeTemas){

        this.fecha = fecha;
        this.listaDeUsuarios = listaDeUsuarios;
        this.listaDeTemas = listaDeTemas;
    }
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
}
