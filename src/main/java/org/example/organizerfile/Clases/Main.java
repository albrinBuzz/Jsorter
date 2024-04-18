package org.example.organizerfile.Clases;

import org.example.organizerfile.Clases.historial.Archivador;
import org.example.organizerfile.HelloApplication;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;

public class Main {
    private static HashMap<String,HashSet<String>> custom;
    public static void main(String[] args) throws IOException {
        HelloApplication.main(args);
        /*Archivos archivos=new Archivos();

        /*archivos.agregarCategoria("ejecutable");
        archivos.agregarCategoria("compresos");
        archivos.agregarCategoria("documentos");
        archivos.listarCustom();
        archivos.agregarExtension(".exe","ejecutable");
        archivos.listarCustom();
        archivos.agregarExtension(".rpm","ejecutable");
        archivos.listarCustom();
        Set<String> categoris=archivos.getCustom().keySet();
        System.out.println(categoris);*/

        Archivador archivador=new Archivador();
        //archivador.listarArchivos("/home/cris/down");
        archivador.verHistorial();


        //InputStream outputStream=new FileInputStream(new File("/home/cris/down/OracleLinux-R8-U6-x86_64-dvd.iso"));
        //outputStream.readAllBytes();
        //outputStream.close();



        /*Archivos archivos=new Archivos();
        Organizador organizador=new Organizador();

        File file=new File("/home/cris/down");




        long startTime = System.nanoTime();
        organizador.organizarType(file);
        long endTime = System.nanoTime();

        long tiempoEjecucion = endTime - startTime;

        double tiempoSegundos = tiempoEjecucion / 1_000_000_000.0;
        double tiempoMilisegundos = tiempoEjecucion / 1_000_000.0;

        System.out.println("Tiempo de ejecución: " + tiempoSegundos + " segundos");
        System.out.println("Tiempo de ejecución: " + tiempoMilisegundos + " milisegundos");*/



    }


    public static <Key extends Set<String>,V extends String> String contains(String ext){
        for (Map.Entry<String, HashSet<String>> characterIntegerEntry : custom.entrySet()) {
            if (characterIntegerEntry.getValue().contains(ext)){
                return characterIntegerEntry.getKey();
            }
        }
        return "others";
    }

    public static <Key extends Set<String>,V extends String> void listar(){
        for (Map.Entry<String, HashSet<String>> characterIntegerEntry : custom.entrySet()) {
            System.out.print(characterIntegerEntry.getKey()+" ");
            System.out.print(characterIntegerEntry.getValue());
            System.out.println();
        }
    }
}
