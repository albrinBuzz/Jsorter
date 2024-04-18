package org.example.organizerfile.Clases;

import javafx.concurrent.Task;
import org.example.organizerfile.Clases.historial.Archivador;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Organizador extends HiloAbst{
    private Archivos archivos;
    private String slash;
    private Archivador archivador;
    private int tipo;
    private boolean organ;
    private File direcotorio;
    private String org,key;
    public Organizador(Archivador archivador,Archivos archivos,String key) {
        this.archivos =archivos;
        this.archivador=archivador;
        this.key=key;
        slash= FileSystems.getDefault().getSeparator();
        organ=true;
    }
    public Organizador(Archivos archivos, Archivador archivador, int tipo, File direcotorio, String org) {
        this.archivos = archivos;
        slash= FileSystems.getDefault().getSeparator();
        this.archivador = archivador;
        this.tipo = tipo;
        this.direcotorio = direcotorio;
        this.org = org;
    }

    private void organizarExtension(File carpeta) throws Exception {
        //primero lista todos los archivos del directorio
        if (carpeta.isDirectory()){
            File[]archivos=carpeta.listFiles();
            setTotal(archivos.length);
            setProgress(1);
            for (File archivo:archivos){
                if (archivo.isFile()) {
                    /*comprueba si es achivo, luego agrega una posible nueva extension a los archivo
                    saca la extension del archivo, comprueba si existe la extension en los archivos
                    para luego obtener el archivo donde sera movido,finalmente mueva el archivo segun el valor de la ruta
                    que devuelve el hashmap, y finalmente agrega la carpeta a los registros
                     */
                    this.archivos.setValorExtension(archivo.getAbsolutePath());
                    String extension=getExtension(archivo.getAbsolutePath());
                    HashMap<String, File> ext=this.archivos.getExtensiones(extension);
                    File file=ext.get(extension);
                    moveFile(archivo.getAbsolutePath(),file.getAbsolutePath());
                    archivador.agregarArchivos(file, carpeta.getAbsolutePath());
                    progress++;
                }
                else if (archivo.isDirectory()){
                    /*en prime instancia agrega una clave .folder, donde iran todos los directorios
                    luego obtiente la ruta donde debe ser movido el diretorio, finalmente mueve el directorio
                    y agrega el direcotorio elegido a los registros
                     */
                    this.archivos.setValorExtension(archivo.getAbsolutePath()+".folder");
                    HashMap<String, File> ext=this.archivos.getExtensiones(".folder");
                    File file=ext.get(".folder");
                    moveFile(archivo.getAbsolutePath(), file.getAbsolutePath());
                    archivador.agregarArchivos(file, carpeta.getAbsolutePath());
                    progress++;
                }
            }
        }
        archivador.setOrganizado(carpeta.getAbsolutePath());
        archivador.crearLog(carpeta.getAbsolutePath());
    }
    private void organizarGeneric(File carpeta,int num) throws Exception {
        String type;
        if (carpeta.isDirectory()){
            File[]archivos=carpeta.listFiles();
            setTotal(archivos.length);
            setProgress(1);
            for (File archivo:archivos){
                if (archivo.isFile()) {
                    /*obtiene la extension el archivo, pregunta si existe esa extension en el hashmap
                    luego obtiene el destino, para luego mover el archivo al destino, y agregar
                    el directorio al historial
                     */
                    String extension= getExtension(archivo.getAbsolutePath());
                    type=this.archivos.contains(extension,num);
                    String dest= setDirectorio(archivo.getAbsolutePath(),type);
                    moveFile(archivo.getAbsolutePath(),dest);
                    archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                    progress++;
                }
                else if (archivo.isDirectory()){
                    /*mueve los directorios a un directorio llamado folder
                     */
                    String dest= setDirectorio(archivo.getAbsolutePath(),"folder");
                    moveFile(archivo.getAbsolutePath(),dest);
                    archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                    progress++;

                }
            }
        }
        archivador.setOrganizado(carpeta.getAbsolutePath());
        archivador.crearLog(carpeta.getAbsolutePath());
    }

    private void reorganizar(File root,int num) throws Exception {
        File[]files=root.listFiles();
        StringBuilder cont=new StringBuilder();
        StringBuffer d=new StringBuffer();
        for (File file:files){
            cont.append(root.getAbsoluteFile());
            if (file.isFile()){
                /* luego de obtener la extension, pregunta si esta existe en los archivos, para luego obtener la
                la ruta donde debe ser almacenado el archivo, concatena root y origen con el directorio destino
                para finalmente mover el archivo al directorio destino
                 */
                String extension= getExtension(file.getAbsolutePath());
                String key= archivos.contains(extension,num);
                cont.append(slash);
                cont.append(key);
                System.out.println(file.getAbsolutePath());
                System.out.println(cont);
                System.out.println(" ");
                moveFile(file.getAbsolutePath(), String.valueOf(cont));
            }else {
                String folder=file.getAbsolutePath();
                String org=folder.substring(folder.lastIndexOf(slash)+1);
                if (!archivador.fileContains(root.getAbsolutePath(),org,slash)){
                    String dest= setDirectorio(file.getAbsolutePath(),"folder");
                    moveFile(file.getAbsolutePath(),dest);
                }
            }
            cont=new StringBuilder();
        }
    }
    public void deshacer(String key) throws Exception {
        HashSet<File> archivos=archivador.getCarpeta(key);
        Iterator<File>archivoIterator=archivos.iterator();
        while (archivoIterator.hasNext()){
            File archivo=archivoIterator.next();
            if (archivo.isDirectory()){
                moverDiretorio(archivo,key);
            }
        }
        archivador.setOrganizacion(key);
    }
    private void moverDiretorio(File carpeta,String root) throws Exception {
        System.out.println(carpeta.getAbsolutePath());
        System.out.println(root);
        File[]archivos= carpeta.listFiles();
        for (File archivo:archivos){
            if (archivo.isFile()) {
                moveFile(archivo.getAbsolutePath(),root);
            }
            else if (archivo.isDirectory()){
                moveFile(archivo.getAbsolutePath(),root);
            }
        }
        carpeta.delete();
    }
    private void moveFile(String origen,String destino) throws Exception {
        File des=new File(destino);
        File orig=new File(origen);
        if (des.exists()) {
            Path org = Paths.get(origen);
            Path dest = Paths.get(destino);
            Path target=dest.resolve(org.getFileName());
            /*if (repetido(orig,des)){
                return;
            }*/
            try {
                Files.move(org, target,StandardCopyOption.REPLACE_EXISTING); //el que hace la magia
            }catch (FileAlreadyExistsException e) {
                throw  new IllegalArgumentException("El archivo "+origen.substring(origen
                        .lastIndexOf(slash)+1)+" Ya existe en "+
                        destino.substring(destino
                                .lastIndexOf(slash)+1)+"\n"+
                                "Por lo tanto sera Eliminado, para proceder de manera correcta\n" +
                                "Vuelva a Ejecutar el orden ");

            }
            catch (AccessDeniedException e){
               throw  new AccessDeniedException("No tiene los persmisos para realizar la operacion");
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                //throw  new Exception(e.getMessage());
                e.printStackTrace();
                //throw new RuntimeException(e);
            }
        }else {
            if (des.mkdir()){
                moveFile(origen,destino);
            }
        }
    }
    public void organize(File carpeta,int num,String typeOrg) throws Exception {
        //primero verifica si el directorio seleccionada fue ordenado, si es asi hace la reorganizacion correspodiente, en caso cotratario hace una organizcion por defecto
        if (archivador.ordenado(carpeta.getAbsolutePath())){
            //si el tipo de organizacion seleccionada es diferente al tipo que tenia  el directorio, elimina el orden y vuelve a ordenar segun el orden seleccionado
            if (!archivador.getOrgFile(carpeta.getAbsolutePath()).equals(typeOrg)){
                deshacer(carpeta.getAbsolutePath());
                newOrg(num,carpeta,typeOrg);
            }else {
                reorganizar(carpeta,num);
            }
        }else {
            newOrg(num,carpeta,typeOrg);
        }
    }
    private void newOrg(int num,File carpeta,String typeOrg) throws Exception {
        archivador.agregarCarpeta(carpeta.getAbsolutePath(),typeOrg);
        /*switch (num){
            //agrega el tipo de orden al direcotorio seleccionado
            case 1, 2: archivador.agregarCarpeta(carpeta.getAbsolutePath(),typeOrg);
                break;
        }*/
        //ordena segun el tipo seleccionado
        switch (num){
            case 1, 2: organizarGeneric(carpeta,num);
                break;
            case 0:
                organizarExtension(carpeta);
                break;
        }
    }
    private String setDirectorio(String ruta,String type){
        String doc=ruta.substring(0,ruta.lastIndexOf(slash)+1);
        File file=new File(doc+type);
        return file.getAbsolutePath();
    }
    private String getExtension(String s){
        int indice=s.lastIndexOf(".");
        return s.substring(indice);
    }
    private boolean repetido(File org,File dest){
        String rutaDest=dest.getAbsolutePath();
        String archv=org.getAbsolutePath();
        String existencia=rutaDest+slash+archv.substring(
                archv.lastIndexOf(slash)+1
        );
        File arch=new File(existencia);
        System.out.println(archv+" origen");
        System.out.println(rutaDest+" Destino");
        System.out.println(existencia+" Existencia");
        System.out.println(" ");
        if (arch.exists()){
            org.delete();
            return true;
        }
    return false;
    }

    @Override
    public void run() {
        try {
            if (organ){
                organize(direcotorio,tipo,org);
            }else {
                deshacer(key);
            }
        } catch (Exception e) {
            System.out.println("la carpeta "+key+" no existe en los registros");
            //throw new IllegalArgumentException("Error al Realizar La operacion");
        }
    }
    @Override
    public int getProgress() {
        return this.progress;
    }
    @Override
    public int getTotal() {
        return this.total;
    }
    @Override
    public void setProgress(int progress) {
        ++this.progress;
    }
    public void setOrgan(boolean organ) {
        this.organ = organ;
    }
    @Override
    public void setTotal(int total) {
        this.total=total;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setDirecotorio(File direcotorio) {
        this.direcotorio = direcotorio;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setArchivos(Archivos archivos) {
        this.archivos = archivos;
    }

    public void setArchivador(Archivador archivador) {
        this.archivador = archivador;
    }
}
