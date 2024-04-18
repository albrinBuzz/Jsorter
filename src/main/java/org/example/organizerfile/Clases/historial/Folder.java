package org.example.organizerfile.Clases.historial;


import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;

public class Folder extends File implements Serializable {
    @Serial
    private static final long serialVersionUID =  412446595615245251L;
    private boolean organizada;
    private String organizacion;
    private HashSet<File>archivos;
    public Folder(String pathname,String organizacion) {
        super(pathname);
        archivos=new HashSet<>();
        this.organizacion=organizacion;

    }
    public void agregarArchivo(File file){
        archivos.add(file);
    }
    public boolean isOrganizada() {
        return organizada;
    }
    public String getOrganizacion() {
        return organizacion;
    }
    public void setOrganizada(boolean organizada) {
        this.organizada = organizada;
    }
    public HashSet<File>getArchivos(){
        return archivos;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public boolean contains(String key){
        return archivos.contains(key);
    }

    @Override
    public String toString() {
        return "Folder{" +getPath()+ "\n"+
                "organizada=" + organizada +"\n"+
                ", organizacion='" + organizacion + "\n" +
                ", archivos=" + archivos +
                '}';
    }
}
