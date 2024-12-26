package org.example.organizerfile.Clases.historial;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
/**
 * Clase que representa un directorio con funcionalidades adicionales.
 * Extiende la clase File y agrega atributos y métodos para gestionar la organización y los archivos contenidos.
 */
public class Folder extends File implements Serializable {
    @Serial
    private static final long serialVersionUID =  412446595615245251L;
    private boolean organizada;
    private String organizacion;
    private HashSet<File>archivos;
    /**
     * Constructor que inicializa un Folder con una ruta y organización específicas.
     *
     * @param pathname      La ruta del directorio.
     * @param organizacion  El tipo de organización del directorio.
     */
    public Folder(String pathname,String organizacion) {
        super(pathname);
        archivos=new HashSet<>();
        this.organizacion=organizacion;

    }
    /**
     * Agrega un archivo al conjunto de archivos del directorio.
     *
     * @param file El archivo a agregar.
     */
    public void agregarArchivo(File file){
        archivos.add(file);
    }

    /**
     * Verifica si el directorio está organizado.
     *
     * @return true si el directorio está organizado, false en caso contrario.
     */
    public boolean isOrganizada() {
        return organizada;
    }
    /**
     * Obtiene el tipo de organización del directorio.
     *
     * @return El tipo de organización del directorio.
     */
    public String getOrganizacion() {
        return organizacion;
    }
    /**
     * Establece si el directorio está organizado.
     *
     * @param organizada true si se quiere marcar como organizado, false en caso contrario.
     */
    public void setOrganizada(boolean organizada) {
        this.organizada = organizada;
    }
    /**
     * Obtiene el conjunto de archivos contenidos en el directorio.
     *
     * @return El conjunto de archivos del directorio.
     */
    public HashSet<File>getArchivos(){
        return archivos;
    }
    /**
     * Establece el tipo de organización del directorio.
     *
     * @param organizacion El nuevo tipo de organización del directorio.
     */
    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }
    /**
     * Verifica si el directorio contiene un archivo dado su nombre.
     *
     * @param key El nombre del archivo a buscar.
     * @return true si el archivo está contenido en el directorio, false en caso contrario.
     */
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
