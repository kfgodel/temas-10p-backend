package convention.persistent;

public enum DuracionDeTema{
    CORTO("Corta", 30), LARGO("Larga", 120), MEDIO("Media", 60);

    private String larga;
    private int cantidadDeMinutos;

    DuracionDeTema(String larga, int cantidadDeMinutos) {
        this.larga = larga;
        this.cantidadDeMinutos = cantidadDeMinutos;
    }
}
