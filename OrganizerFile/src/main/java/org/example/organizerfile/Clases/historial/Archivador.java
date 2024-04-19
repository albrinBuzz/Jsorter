package org.example.organizerfile.Clases.historial;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
/**
 * Clase que gestiona el historial de archivos organizados.
 * Permite agregar, consultar y serializar carpetas con su contenido organizado, así como crear un registro de actividades.
 */
public class Archivador {
    private HashMap<String,Folder>carpetas;

    /**
     * Constructor que inicializa un nuevo Archivador y carga los datos del historial si existen.
     */
    public Archivador() {
        carpetas=new HashMap<>();
        deserializar();

    }
    /**
     * Verifica si el historial contiene una carpeta con la clave dada.
     *
     * @param key La clave de la carpeta a buscar en el historial.
     * @return true si la carpeta está en el historial, false en caso contrario.
     */
    public boolean contains(String key){
        return carpetas.containsKey(key);
    }

    /**
     * Verifica si una carpeta contiene un archivo dado su nombre.
     *
     * @param key   La clave de la carpeta.
     * @param file  El nombre del archivo a buscar.
     * @param slash El separador de directorios del sistema operativo.
     * @return true si el archivo está contenido en la carpeta, false en caso contrario.
     */
    public boolean fileContains(String key,String file,String slash){
        return carpetas.get(key).getArchivos().contains(new File(key+slash+file));
    }
    /**
     * Verifica si una carpeta está organizada.
     *
     * @param key La clave de la carpeta.
     * @return true si la carpeta está organizada, false en caso contrario.
     */
    public boolean ordenado(String key){
        if (carpetas.containsKey(key)){
            return carpetas.get(key).isOrganizada();
        }
        return false;
    }
    /**
     * Agrega una carpeta al historial con la clave y organización especificadas.
     *
     * @param ruta         La ruta de la carpeta.
     * @param organizacion El tipo de organización de la carpeta.
     */
    public void agregarCarpeta(String ruta,String organizacion){
        carpetas.put(ruta,new Folder(ruta,organizacion));
    }
    /**
     * Obtiene el tipo de organización de una carpeta.
     *
     * @param key La clave de la carpeta.
     * @return El tipo de organización de la carpeta.
     */
    public String getOrgFile(String key){
        return carpetas.get(key).getOrganizacion();
    }
    /**
     * Agrega archivos a una carpeta del historial.
     *
     * @param file El archivo a agregar.
     * @param key  La clave de la carpeta.
     */
    public void agregarArchivos(File file,String key){
        carpetas.get(key).agregarArchivo(file);
    }
    /**
     * Obtiene el conjunto de carpetas del historial.
     *
     * @return Un mapa que contiene las carpetas del historial.
     */
    public HashMap<String, Folder> getCarpetas() {
        return carpetas;
    }
    /**
     * Obtiene el conjunto de archivos de una carpeta del historial y marca la carpeta como no organizada.
     *
     * @param key La clave de la carpeta.
     * @return El conjunto de archivos de la carpeta.
     */
    public HashSet<File> getCarpeta(String key){
        carpetas.get(key).setOrganizada(false);
        serialiar();
        return carpetas.get(key).getArchivos();
    }

    /**
     * Marca una carpeta del historial como organizada.
     *
     * @param key La clave de la carpeta.
     */
    public void setOrganizado(String key){
        carpetas.get(key).setOrganizada(true);
        serialiar();
    }
    /**
     * Restablece el tipo de organización de una carpeta del historial a un valor predeterminado.
     *
     * @param key La clave de la carpeta.
     */
    public void setOrganizacion(String key){
        carpetas.get(key).setOrganizacion(" ");
    }
    /**
     * Serializa el historial de carpetas y lo guarda en un archivo.
     */
    public void serialiar(){
        try (ObjectOutputStream objectInputStream= new ObjectOutputStream(new FileOutputStream("./historial.bin"))){{
            objectInputStream.writeObject(carpetas);
        }} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Deserializa el historial de carpetas desde un archivo.
     */
    //@SuppressWarnings("unchecked") //Cambiar y debo comprobar el casteo
    public void deserializar(){
        if (new File("./historial.bin").exists()){
            try (ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream("./historial.bin"))){
                carpetas = (HashMap<String, Folder>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            serialiar();
        }
    }
    /**
     * Crea un registro de actividad en un archivo de registro.
     *
     * @param direct La ruta de la carpeta.
     * @throws IOException Si ocurre un error al escribir en el archivo de registro.
     */
    public void crearLog(String direct) throws IOException {
        File logFile = new File("log.txt");

        if (!logFile.exists()) {
            logFile.createNewFile();
        }else {
            logFile.setWritable(true);
        }
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("log.txt", true))) {
            printWriter.println(carpetas.get(direct));
            printWriter.write("Organizada el " + new Date());
            printWriter.println();
            printWriter.println("-------------------");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        logFile.setReadOnly();
    }
}
