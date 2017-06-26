package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.DuracionDeTema;
import convention.persistent.Reunion;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

import java.util.List;

/**
 * Created by fede on 23/06/17.
 */
public class DuracionDeTemaTo {

    @CopyFromAndTo(DuracionDeTema.descripcion_FIELD)
    private String descripcion;

    @CopyFrom(DuracionDeTema.cantidadDeMinutos_FIELD)
    private int cantidadDeMinutos;

    private DuracionDeTema duracion;

    public int getCantidadDeMinutos() {
        return cantidadDeMinutos;
    }

    public void setCantidadDeMinutos(int cantidadDeMinutos) {
        this.cantidadDeMinutos = cantidadDeMinutos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static DuracionDeTemaTo create(DuracionDeTema duracionDeTema){
        DuracionDeTemaTo nuevoTo=new DuracionDeTemaTo();
        nuevoTo.setDescripcion(duracionDeTema.getDescripcion());
        nuevoTo.setCantidadDeMinutos(duracionDeTema.getCantidadDeMinutos());
        nuevoTo.setDuracion(duracionDeTema);
        return nuevoTo;
    }

    public DuracionDeTema getDuracion() {
        return duracion;
    }

    public void setDuracion(DuracionDeTema duracion) {
        this.duracion = duracion;
    }
}
