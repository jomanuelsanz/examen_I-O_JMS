package pio.daw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        
        
        // DEMOSTRACIÓN DE QUE PUEDO LEER CSV Y GENERAR TXT

        // Creo el ArrayList
        ArrayList<Radionuclido> lista = new ArrayList<>();

        // Leo el CSV (BufferedReader y FileReader)
        try (BufferedReader br = new BufferedReader(new FileReader("residuos.csv"))) {
            String linea;

            // Ignoro la cabecera
            br.readLine();

            // Leo línea a línea
            while ((linea = br.readLine()) != null){

                // Separo líneas por comas
                String[] partes = linea.split(";");

                String id = partes[0];
                String isotopo = partes[1];
                double actividadEspecificaInicial = Double.parseDouble(partes[2]);
                double masa = Double.parseDouble(partes[3]);
        
                // Creo objeto Residuo
                Radionuclido r = new Radionuclido(id, isotopo, actividadEspecificaInicial, masa);  // Esto da problemas y no sé por qué :(

                // Lo añado al ArrayList
                lista.add(r);
            }
            } catch (IOException e) {   // Si ocurriera un error al leer un archivo, lo muestra
                e.printStackTrace();
            }

            // Genero archivo factura.txt (BufferedWriter y FileWriter)  ... El archivo se genera pero no su contenido :( 
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("factura.txt"))) {
                
                // Recorro Residuo
                for (Radionuclido r : lista) {
                    bw.write(r.toFactura());
                    bw.newLine();
                }
            
            } catch (IOException e) {
                e.printStackTrace();  // Capturo los errores si hubiera y los muestro
            }

            System.out.println("Archivo factura.txt generado correctamente");

        } 




    }

 