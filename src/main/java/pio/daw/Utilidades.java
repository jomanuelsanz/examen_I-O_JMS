package pio.daw;

import java.util.HashMap;

public class Utilidades {

    // Energía liberada por desintegración en Julios (1 MeV = 1.602e-13 J)
    public static final HashMap<String, Double> energias = new HashMap<>();

    // Semivida de cada isótopo en segundos
    public static final HashMap<String, Double> semividas = new HashMap<>();

    static {
        // U235: alfa ~4.679 MeV
        energias.put("U235", 4.679e6 * 1.602e-19);
        // Pu239: alfa ~5.245 MeV
        energias.put("Pu239", 5.245e6 * 1.602e-19);
        // Cs137: beta ~0.512 MeV
        energias.put("Cs137", 0.512e6 * 1.602e-19);

        // U235: 703,800,000 años → segundos
        semividas.put("U235", 703_800_000.0 * 365.25 * 24 * 3600);
        // Pu239: 24,110 años → segundos
        semividas.put("Pu239", 24_110.0 * 365.25 * 24 * 3600);
        // Cs137: 30.17 años → segundos
        semividas.put("Cs137", 30.17 * 365.25 * 24 * 3600);
    }

    /**
     * Integración numérica por la regla de Simpson compuesta.
     * Calcula el área bajo la curva f entre limInf y limSup.
     */
    public static double integrar(FuncionUnivariable f, double limInf, double limSup) {
        int n = 1000; // número de subintervalos (debe ser par)
        if (n % 2 != 0) n++;
        double h = (limSup - limInf) / n;

        double suma = f.evaluar(limInf) + f.evaluar(limSup);
        for (int i = 1; i < n; i++) {
            double x = limInf + i * h;
            suma += (i % 2 == 0 ? 2 : 4) * f.evaluar(x);
        }
        return suma * h / 3.0;
    }

    /**
     * Método de bisección: encuentra x en [limInf, limSup] tal que f(x) = y.
     * Funciona tanto para funciones crecientes como decrecientes.
     */
    public static double byeccion(FuncionUnivariable f, double y, double limInf, double limSup) {
        double tolerancia = 1e-6;
        double a = limInf;
        double b = limSup;

        // g(x) = f(x) - y; buscamos la raíz de g
        double ga = f.evaluar(a) - y;

        for (int i = 0; i < 200; i++) {
            double mid = (a + b) / 2.0;
            double gMid = f.evaluar(mid) - y;

            if (Math.abs(gMid) < tolerancia || (b - a) / 2.0 < tolerancia) {
                return mid;
            }

            if (ga * gMid < 0) {
                b = mid;
            } else {
                a = mid;
                ga = gMid;
            }
        }
        return (a + b) / 2.0;
    }
}
