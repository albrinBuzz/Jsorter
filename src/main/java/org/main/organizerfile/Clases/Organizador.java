package org.main.organizerfile.Clases;

import javafx.application.Platform;
import org.main.organizerfile.Clases.historial.Archivador;
import org.main.organizerfile.observer.ProgressObserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
Clase para organizar archivos en un directorio.
Esta clase proporciona métodos para organizar archivos dentro de un directorio según varios criterios, como extensión o tipo de organización genérica.
También permite deshacer operaciones de organización y restaurar el estado anterior del directorio.
*/
public class Organizador extends HiloAbst{
    // Atributos
    private Archivos archivos;
    private String slash;
    private Archivador archivador;
    private int tipo;
    private boolean organ;
    private File direcotorio;
    private String org,key;
    private ProgressObserver progressObserver;
    private AtomicInteger progreso;
    private final Object lock = new Object(); // Objeto para sincronizar el acceso a la barra de progreso

    /**
     * Constructor de la clase Organizador.
     * Este constructor crea una instancia de Organizador con los parámetros dados.
     *
     * @param archivador Archivador asociado.
     * @param archivos   Archivos asociados.
     * @param key        Clave para operaciones posteriores.
     * Esta clave se utiliza para identificar la operación realizada y permitir su posterior reversión.
     */
    public Organizador(Archivador archivador,Archivos archivos,String key,ProgressObserver  progressObserver) {
        this.archivos =archivos;
        this.archivador=archivador;
        this.key=key;
        slash= FileSystems.getDefault().getSeparator();
        organ=true;
        this.progressObserver=progressObserver;
    }
    /**
     * Constructor alternativo para Organizador.
     *
     * @param archivos    Archivos asociados.
     * @param archivador  Archivador asociado.
     * @param tipo        Tipo de organización.
     * @param direcotorio Directorio de operación.
     * @param org         Origen de la operación.
     */
    public Organizador(Archivos archivos, Archivador archivador, int tipo, File direcotorio, String org) {
        this.archivos = archivos;
        slash= FileSystems.getDefault().getSeparator();
        this.archivador = archivador;
        this.tipo = tipo;
        this.direcotorio = direcotorio;
        this.org = org;
    }
    /**
     * Organiza archivos por extensión.
     * Este método organiza los archivos dentro de la carpeta especificada agrupándolos según su extensión.
     * Por ejemplo, todos los archivos PDF se colocarán en una carpeta específica, los archivos de Word en otra, y así sucesivamente.
     *
     * @param carpeta Carpeta a organizar.
     * @throws Exception Si hay algún error durante el proceso de organización.
     */
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
    /**
     * Organiza archivos de manera genérica.
     * Este método organiza los archivos dentro de la carpeta especificada de manera genérica, según un criterio numérico dado.
     * El criterio numérico determina la forma en que se organizarán los archivos.
     *
     * @param carpeta Carpeta a organizar.
     * @param num     Número de operación que indica el criterio de organización.
     *                Este número se utiliza para determinar cómo se organizarán los archivos.
     * @throws Exception Si hay algún error durante el proceso de organización.
     */
    private void organizarGeneric(File carpeta,int num) throws Exception {
        // Este método organiza los archivos de manera genérica según el criterio numérico dado.
        // El criterio numérico se utiliza para determinar la lógica de organización de los archivos.
        // Por ejemplo, el valor 1 podría significar organizar por tipo, el valor 2 por personalizada, etc.
        double tiempo = System.nanoTime();

        if (carpeta.isDirectory()){
            File[]archivos=carpeta.listFiles();
            setTotal(archivos.length);
            setProgress(1);
            Path folderPath = Paths.get(carpeta.getAbsolutePath());

            /*try (Stream<Path> fileStream = Files.walk(folderPath,1)) {
                Set<Path> uniqueFiles = new HashSet<>();
                fileStream.parallel()  // Procesamiento paralelo para manejar grandes volúmenes de archivos
                        //.filter(Files::isRegularFile)
                        .forEach(filePath -> {
                            if (!uniqueFiles.contains(filePath)) {

                                File archivo = filePath.toFile();
                                if (archivo.isFile()) {
                                    String extension = getExtension(archivo.getAbsolutePath());
                                    String type = this.archivos.contains(extension, num);
                                    String dest = setDirectorio(archivo.getAbsolutePath(), type);
                                    try {
                                        moveFile(archivo.getAbsolutePath(), dest);
                                    } catch (Exception e) {
                                        //throw new RuntimeException(e);
                                        System.out.println(e.getMessage());
                                    }
                                    archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                                    progress++;
                                } else if (archivo.isDirectory()) {

                                    String dest = setDirectorio(archivo.getAbsolutePath(), "folder");
                                    try {
                                        moveFile(archivo.getAbsolutePath(), dest);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                                    progress++;
                                }
                                uniqueFiles.add(filePath);
                            }

                            });
            }*/

            for (File archivo:archivos){
                String type;
                if (archivo.isFile()) {
                    //obtiene la extension el archivo, pregunta si existe esa extension en el hashmap
                    //luego obtiene el destino, para luego mover el archivo al destino, y agregar
                    //el directorio al historial

                    String extension= getExtension(archivo.getAbsolutePath());
                    type=this.archivos.contains(extension,num);
                    String dest= setDirectorio(archivo.getAbsolutePath(),type);
                    moveFile(archivo.getAbsolutePath(),dest);
                    archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                    progress++;
                }
                else if (archivo.isDirectory()){
                    //mueve los directorios a un directorio llamado folder

                    String dest= setDirectorio(archivo.getAbsolutePath(),"folder");
                    moveFile(archivo.getAbsolutePath(),dest);
                    archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                    progress++;
                }
            }
        }

        System.out.println("Version Lineal: " + ((System.nanoTime() - tiempo) / 1000000000.0) + " segundos");

        archivador.setOrganizado(carpeta.getAbsolutePath());
        archivador.crearLog(carpeta.getAbsolutePath());
    }

    public void organizarParalelo(File carpeta,int num) throws Exception {
        double tiempo = System.nanoTime();
        progreso=new AtomicInteger(0);
        if (carpeta.isDirectory()){
            File[]archivos=carpeta.listFiles();
            progressObserver.updateProgres(progreso.get());
            int inicio=0;
            int div=archivos.length/4;
            ExecutorService service=Executors.newFixedThreadPool(4);
            Runnable[] runnables =new Runnable[4];
            int idx=1;
            for (int i = 0; i < runnables.length; i++) {
                int finalInicio = inicio;
                int finalIdx = idx;
                Runnable runnable=() -> {
                    try {
                        organizador(archivos, num, carpeta, finalInicio, div* finalIdx);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                runnables[i]=runnable;
                inicio+=div;
                idx++;
            }
            for (int i = 0; i < runnables.length; i++) {
                service.submit(runnables[i]);
            }
            service.shutdown();
            service.awaitTermination(30, TimeUnit.SECONDS);
        }
        System.out.println("Version concurrente: " + ((System.nanoTime() - tiempo) / 1000000000.0) + " segundos");

        archivador.setOrganizado(carpeta.getAbsolutePath());
        archivador.crearLog(carpeta.getAbsolutePath());
    }

    private void organizador(File[] archivos, int num, File carpeta, int idxMin, int idxMax) throws Exception {
        int progresoInt = 0;
        for (int i = idxMin; i < idxMax; i++) {
            var archivo = archivos[i];
            String type;
            if (archivo.isFile()) {
                // Lógica para organizar archivos (mover, agregar, etc.)

                String extension = getExtension(archivo.getAbsolutePath());
                type = this.archivos.contains(extension, num);
                String dest = setDirectorio(archivo.getAbsolutePath(), type);
                moveFile(archivo.getAbsolutePath(), dest);
                archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                progreso.getAndIncrement();

            }
            else if (archivo.isDirectory()) {
                // Lógica para mover directorios

                String dest = setDirectorio(archivo.getAbsolutePath(), "folder");
                moveFile(archivo.getAbsolutePath(), dest);
                archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
                progreso.getAndIncrement();


            }
        }
        synchronized (lock) {
            // Actualización de la barra de progreso
            Platform.runLater(() -> progressObserver.updateProgres(progreso.get()));
        }
    }


    private void organizar(File archivo,int num,File carpeta) throws Exception {
        String type;
        if (archivo.isFile()) {
            //obtiene la extension el archivo, pregunta si existe esa extension en el hashmap
            //luego obtiene el destino, para luego mover el archivo al destino, y agregar
            //el directorio al historial

            String extension= getExtension(archivo.getAbsolutePath());
            type=this.archivos.contains(extension,num);
            String dest= setDirectorio(archivo.getAbsolutePath(),type);
            moveFile(archivo.getAbsolutePath(),dest);
            archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
            progress++;
        }
        else if (archivo.isDirectory()){
            //mueve los directorios a un directorio llamado folder

            String dest= setDirectorio(archivo.getAbsolutePath(),"folder");
            moveFile(archivo.getAbsolutePath(),dest);
            archivador.agregarArchivos(new File(dest), carpeta.getAbsolutePath());
            progress++;
        }
    }



    /**
     * Reorganiza archivos.
     * Este método se encarga de reorganizar los archivos dentro del directorio raíz especificado según un número de operación dado.
     * El número de operación determina la lógica de reorganización de los archivos.
     *
     * @param root Directorio raíz que contiene los archivos a reorganizar.
     * @param num  Número de operación que indica la lógica de reorganización.
     * @throws Exception Si ocurre algún error durante el proceso de reorganización.
     */
    private void reorganizar(File root,int num) throws Exception {
        // Este método reorganiza los archivos dentro del directorio raíz según el número de operación dado.
        // El número de operación se utiliza para determinar la lógica de reorganización de los archivos.
        // Por ejemplo, el valor 1 podría significar reorganizar por fecha, el valor 2 por tamaño, etc.

        // Se obtienen todos los archivos dentro del directorio raíz.
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
    /**
     * Deshace la operación.
     * Este método deshace la operación especificada por la clave dada, restaurando el estado anterior del directorio.
     * La operación consiste en revertir los cambios realizados en el directorio asociados con la clave especificada.
     *
     * @param key Clave de operación que identifica la operación a deshacer.
     * @throws Exception Si ocurre algún error durante el proceso de reversión.
     */
    public void deshacer(String key) throws Exception {
        HashSet<File> archivos=archivador.getCarpeta(key);
        double tiempo = System.nanoTime();
        Iterator<File>archivoIterator=archivos.iterator();
        System.out.println("Deshaciendo");

        archivos.parallelStream()
                .forEach(file -> {
                    try {
                        moverDiretorio(file,key);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        System.out.println("Version concurrente: " + ((System.nanoTime() - tiempo) / 1000000000.0) + " segundos");

        /*if (archivos.size()>10_000){

        }else {
            while (archivoIterator.hasNext()){
                File archivo=archivoIterator.next();
                if (archivo.isDirectory()){
                    moverDiretorio(archivo,key);
                }
            }
            System.out.println("Version lineal: " + ((System.nanoTime() - tiempo) / 1000000000.0) + " segundos");
        }*/



        archivador.setOrganizacion(key);

    }

    private  void  deshacer(File[]archivos,int idxMin,int idxMax) throws Exception {
        for (int i = idxMin; i < idxMax; i++) {
            var archivo = archivos[i];
            if (archivo.isDirectory()){
                moverDiretorio(archivo,key);
            }
        }
    }
    /**
     * Mueve directorios.
     * Este método se encarga de mover un directorio y su contenido a una nueva ubicación especificada por la raíz de la operación.
     *
     * @param carpeta Carpeta a mover junto con su contenido.
     * @param root    Raíz de la operación que especifica la nueva ubicación del directorio.
     * @throws Exception Si ocurre algún error durante el proceso de mover el directorio.
     */
    private void moverDiretorio(File carpeta,String root) throws Exception {
        File[]archivos= carpeta.listFiles();
        for (File archivo:archivos){
            if (archivo.isFile()) {
                moveFile(archivo.getAbsolutePath(),root);
            }
            else if (archivo.isDirectory()){
                moveFile(archivo.getAbsolutePath(),root);
            }
        }
        // Una vez que se ha movido todo el contenido de la carpeta, se elimina la carpeta.
        carpeta.delete();
    }
    /**
     * Mueve archivos.
     * Este método se encarga de mover un archivo desde su ubicación de origen a una nueva ubicación especificada por el destino.
     *
     * @param origen  Ruta de origen del archivo que se va a mover.
     * @param destino Ruta de destino del archivo donde se va a mover.
     * @throws Exception Si ocurre algún error durante el proceso de mover el archivo.
     */
    private void moveFile(String origen,String destino) throws Exception {
        File des=new File(destino);
        if (des.exists()) {
            Path org = Paths.get(origen);
            Path dest = Paths.get(destino);
            Path target=dest.resolve(org.getFileName());
            try {
                // Se mueve el archivo a la ruta de destino.
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
                System.out.println("!");
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
    /**
     * Organiza archivos en la carpeta especificada.
     * Este método organiza los archivos dentro de la carpeta especificada según el tipo de organización y la configuración proporcionada.
     * Verifica si la carpeta ya ha sido organizada anteriormente. Si ha sido organizada previamente y se especifica un nuevo tipo de organización,
     * deshace la organización anterior y aplica la nueva. Si la carpeta no ha sido organizada previamente, aplica la organización por defecto.
     *
     * @param carpeta Carpeta a organizar.
     * @param num     Número de operación.
     * @param typeOrg Tipo de organización a aplicar.
     * @throws Exception Si ocurre algún error durante el proceso de organización.
     */
    public void organize(File carpeta,int num,String typeOrg) throws Exception {
        // Verifica si la carpeta seleccionada ya ha sido organizada previamente.
        if (archivador.ordenado(carpeta.getAbsolutePath())){
            // Si la carpeta ha sido organizada previamente, verifica si se ha especificado un nuevo tipo de organización.
            if (!archivador.getOrgFile(carpeta.getAbsolutePath()).equals(typeOrg)){
                // Si el tipo de organización seleccionado es diferente al tipo actual, deshace la organización anterior y aplica la nueva.
                deshacer(carpeta.getAbsolutePath());
                newOrg(num,carpeta,typeOrg);
            }else {
                // Si el tipo de organización seleccionado es el mismo que el actual, simplemente reorganiza los archivos.
                reorganizar(carpeta,num);
            }
        }else {
            // Si la carpeta no ha sido organizada previamente, aplica la organización por defecto.
            newOrg(num,carpeta,typeOrg);
        }
    }
    /**
     * Configura un nuevo orden para la carpeta especificada.
     * Este método establece un nuevo tipo de organización para la carpeta seleccionada y aplica la organización correspondiente.
     *
     * @param num     Número de operación.
     * @param carpeta Carpeta a organizar.
     * @param typeOrg Tipo de organización a aplicar.
     * @throws Exception Si ocurre algún error durante el proceso de configuración del nuevo orden.
     */
    private void newOrg(int num,File carpeta,String typeOrg) throws Exception {
        // Establece el nuevo tipo de organización para la carpeta.
        archivador.agregarCarpeta(carpeta.getAbsolutePath(), typeOrg);

        // Aplica la organización correspondiente según el tipo seleccionado.
        switch (num) {
            case 1, 2:
                // Si el número de operación es 1 o 2, se aplica la organización genérica.
                //organizarParalelo(carpeta, num);
                organizarGeneric(carpeta, num);
                break;
            case 0:
                // Si el número de operación es 0, se aplica la organización por extensión.
                organizarExtension(carpeta);
                break;
        }
    }
    /**
     * Configura el directorio destino para un archivo.
     * Este método establece el directorio de destino para el archivo especificado, basado en la ruta y el tipo de archivo proporcionados.
     *
     * @param ruta Ruta del archivo.
     * @param type Tipo de archivo.
     * @return La ruta del directorio destino para el archivo.
     */
    private String setDirectorio(String ruta, String type) {
        // Obtiene el directorio padre de la ruta del archivo.
        String doc = ruta.substring(0, ruta.lastIndexOf(slash) + 1);
        // Crea un nuevo archivo con el directorio padre y el tipo de archivo especificados.
        File file = new File(doc + type);
        // Retorna la ruta absoluta del directorio destino.
        return file.getAbsolutePath();
    }
    /**
     * Obtiene la extensión de un archivo a partir de su ruta.
     * Este método extrae la extensión de un archivo basándose en su ruta y la devuelve como una cadena.
     *
     * @param rt La ruta del archivo.
     * @return La extensión del archivo.
     */
    private String getExtension(String rt){
        int indice=rt.lastIndexOf(".");
        return rt.substring(indice);
    }
    /**
     * Ejecuta la operación.
     * Este método se encarga de ejecutar la operación correspondiente según el estado de la variable 'organ'.
     * Si 'organ' es verdadero, se organiza la carpeta utilizando el método 'organize'. En caso contrario, se deshace
     * de la operación utilizando el método 'deshacer'.
     */
    @Override
    public void run() {
        try {
            if (organ) {
                // Si 'organ' es verdadero, se organiza la carpeta utilizando el método 'organize'.
                organize(direcotorio, tipo, org);
            } else {
                // En caso contrario, se deshace de la operación utilizando el método 'deshacer'.
                deshacer(key);
            }
        } catch (Exception e) {
            // En caso de que ocurra una excepción, se imprime un mensaje de error.
            System.out.println("Error al realizar la operación en la carpeta " + key + ": " + e.getMessage());
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

    public ProgressObserver getProgressObserver() {
        return progressObserver;
    }

    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
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
