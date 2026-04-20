package pio.daw;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Radionuclido {

    // ── Tarea 1: declara aquí los atributos privados ──────────────────────────
    private String id;
    private String isotopo;
    private double actividadEspecificaInicial;
    private double masa;
    private LocalDateTime fechaEntrega;




    // ── Tarea 1: constructor ──────────────────────────────────────────────────
    // Añado todos los atributos privados

    public Radionuclido(String id, String isotopo, Double actividadEspecificaInicial,
                        Double masa, LocalDateTime fechaEntrega) {
        this.id = id;
        this.isotopo = isotopo;
        this.actividadEspecificaInicial = actividadEspecificaInicial;
        this.masa = masa;
        this.fechaEntrega = fechaEntrega;
    }




    public Radionuclido(String id2, String isotopo2, double actividadEspecificaInicial2, double masa2) {
        //TODO Auto-generated constructor stub
    }




    // ── Tarea 1: getters ──────────────────────────────────────────────────────
    public String getId() {
        return id; // Devuelve ID
    }

    public String getIsotopo() {
        return isotopo; // Devuelve isotopo
    }

    public Double getMasa() {
        return masa; // Devuelve masa
    }

    public Double getActividadEspecificaInicial() {
        return actividadEspecificaInicial; // Devuelve ActividadEspecificaInicial
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega; // Devuelve fechaEntrega
    }

    public double getActividadInicial() {
        double actividadInicial = actividadEspecificaInicial * masa;
        return actividadInicial; // Devuelve actividadInicial (que es el producto de actividadEspecificaInicial * masa)
    }




    // ── Tarea 2: actividad en una fecha concreta ──────────────────────────────
    
    public double actividad(LocalDateTime fecha) {

        // Fórmula: A(t) = A0 * e^(-lambda * t)

        // Primero necesito calcular una serie de cosas:

        double semivida = Utilidades.semividas.get(this.isotopo);  // Calculo "semivida"
        double lambda = Math.log(2) / semivida;                    // Calculo "lambda"

        double t = ChronoUnit.SECONDS.between(this.fechaEntrega, fecha);   // Calculo "t"

        double actividad = this.getActividadInicial() * Math.exp(-lambda * t);  // Fórmula
            return actividad;   // Devuelve actividad
    }




    // ── Tarea 3: fracción de actividad restante (0..1) ────────────────────────

    public double porcentajeActividad(LocalDateTime fecha) {
        double porcentajeActividad = actividad(fecha) / getActividadInicial();  // Fórmula
        return porcentajeActividad; // Devuelve porcentajeActividad
    }



    // ── Tarea 4: fecha en que la actividad baja al 10% ───────────────────────

    public LocalDateTime getFechaSegura() {

        double semivida = Utilidades.semividas.get(isotopo);

        // Paso 1:
        double tMax = semivida;
        while (porcentajeActividad(fechaEntrega.plusSeconds((long) tMax)) >= 0.1){
            tMax *= 2;
        }
       
        
        // Paso 2:
        double tSeg = Utilidades.biseccion(
            t -> porcentajeActividad(fechaEntrega.plusSeconds((long) t)),
            0.1, // Valor objetivo
            0,  // Limite inferior
            tMax  // Limite superior
            
        );


        // Paso 3:
        return fechaEntrega.plusSeconds((long) tSeg);
    }




    // ── Tarea 5: coste de refrigeración en euros ──────────────────────────────

    public double getCosteRefrigeracion() {
        LocalDateTime fechaSegura = getFechaSegura();
        double tSeg = ChronoUnit.SECONDS.between(fechaEntrega, fechaSegura);
        double eDesintegracion = Utilidades.energias.get(isotopo);

        FuncionUnivariable pEle = t -> 
            actividad(fechaEntrega.plusSeconds((long) t)) + eDesintegracion / 4.0;

        double eGastada = Utilidades.integrar(pEle, 0, tSeg);


        return eGastada / 3_600_000.0;
    }





    // ── Tarea 6: bloque de factura en texto ───────────────────────────────────

    public String toFactura() {
        return "===== FACTURA DE REFRIGERACIÓN - RESIDUO #1 =====" + 
        "Isótopo: " + isotopo + 
        "Masa: " + masa +
        "Fecha de entrega: " + fechaEntrega + 
        "Fecha segura: " + getFechaSegura() + 
        "Coste total: " + getCosteRefrigeracion() + 
        "--------------------";
    }
}
