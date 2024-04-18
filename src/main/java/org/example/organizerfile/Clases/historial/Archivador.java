package org.example.organizerfile.Clases.historial;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Archivador {
    private HashMap<String,Folder>carpetas;
    public Archivador() {
        carpetas=new HashMap<>();
        deserializar();

    }
    public boolean contains(String key){
        return carpetas.containsKey(key);
    }
    public boolean fileContains(String key,String file,String slash){
        return carpetas.get(key).getArchivos().contains(new File(key+slash+file));
    }
    public boolean ordenado(String key){
        if (carpetas.containsKey(key)){
            return carpetas.get(key).isOrganizada();
        }
        return false;
    }
    public void agregarCarpeta(String ruta,String organizacion){
        carpetas.put(ruta,new Folder(ruta,organizacion));
    }
    public String getOrgFile(String key){
        return carpetas.get(key).getOrganizacion();
    }
    public void agregarArchivos(File file,String key){
        carpetas.get(key).agregarArchivo(file);
    }
    public HashMap<String, Folder> getCarpetas() {
        return carpetas;
    }
    public HashSet<File> getCarpeta(String key){
        carpetas.get(key).setOrganizada(false);
        serialiar();
        return carpetas.get(key).getArchivos();
    }
    public void setOrganizado(String key){
        carpetas.get(key).setOrganizada(true);
        serialiar();
    }
    public void setOrganizacion(String key){
        carpetas.get(key).setOrganizacion(" ");
    }
    public void serialiar(){
        try (ObjectOutputStream objectInputStream= new ObjectOutputStream(new FileOutputStream("./historial.bin"))){{
            objectInputStream.writeObject(carpetas);
        }} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
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
    public void verHistorial(){
        deserializar();
        for (Map.Entry<String, Folder> entry : carpetas.entrySet()) {
            String key=entry.getKey();
            String value= String.valueOf(entry.getValue().getArchivos());
            System.out.println(key+"---"+value);
        }
    }
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
