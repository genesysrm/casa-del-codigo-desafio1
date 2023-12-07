package com.aluracursos.casadelcodigo.principal;

import com.aluracursos.casadelcodigo.model.Datos;
import com.aluracursos.casadelcodigo.model.DatosLibros;
import com.aluracursos.casadelcodigo.service.ConsumoAPI;
import com.aluracursos.casadelcodigo.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println("Datos");
        datos.resultados().stream()
                .limit(5).forEach(System.out::println);


         //top 10 libros más descargados
        System.out.println("\n Top 10 episodios");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
//                .peek(l -> System.out.println("Ordenando los datos " + l))
                .limit(10)
//                .peek(l -> System.out.println("Limitando a 10 " + l))
                .map(l -> l.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mayusculas " + e))
                .forEach(System.out::println);

        //Buscar libros con al menos un autor vivo ou entre un rango de fechas
        System.out.println("Ingresa la fecha inicial en que deseas buscar");
        var fechaInicial = teclado.nextLine();
        System.out.println("Ingresa la fecha final hasta la que deseas buscar");
        var fechaFinal = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?author_year_start="+ fechaInicial +"&author_year_end=" + fechaFinal);
        var resultadosBusquedaFecha = conversor.obtenerDatos(json, Datos.class);
        resultadosBusquedaFecha.resultados().stream()
                .limit(15)
                .forEach(l -> System.out.println(
                        "Titulo: " + l.titulo() +
                                "Autor/es: " + l.autor() +
                                "Idiomas disponibles: " + l.idiomas()
                ));

        //Buscar por nombre del libro

        System.out.println("Ingresa el nombre del libro que deseas buscar");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        System.out.println(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var resultadosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = resultadosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado");
            System.out.println("Titulo: " + libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado");
        }

        //Calcular cantidad de descargas de los libros
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(e -> e.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("Media de descargas " + est.getAverage());
        System.out.println("Número mayor de descargas: " + est.getMax());
        System.out.println("Número menor de descargas: " + est.getMin());
        System.out.println("Número de registros contados " + est.getCount()); // retorna el valor de los registros analizados

    }
}
