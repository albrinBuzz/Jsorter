package org.example.organizerfile.Clases;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.*;
/**
 * Clase para administrar la organización de archivos en directorios.
 * Proporciona métodos para gestionar extensiones de archivos, categorías personalizadas,
 * así como la asociación de extensiones con directorios específicos.
 */
public class Archivos {
    private HashMap<String, File> extensiones;
    private final  HashMap<String, HashSet<String>> type;
    private HashMap<String,HashSet<String>>custom;
    private String slash;
    public Archivos() {
        extensiones=new HashMap<>();
        type=new HashMap<>();
        custom=new HashMap<>();
        traerDatos();
        setType();
        slash= FileSystems.getDefault().getSeparator();
    }
    /**
     * Establece el valor de la extensión y su directorio asociado.
     * Este método toma una ruta de archivo y asigna la extensión del archivo al directorio correspondiente
     * en función de la extensión del archivo.
     *
     * @param ruta La ruta del archivo.
     */
    public void setValorExtension(String ruta){
        String extension=obtenerExtension(ruta);
        String doc=extension.substring(extension.indexOf(".")+1);
        File archivo=new File(ruta.substring(0,
                ruta
                .lastIndexOf(slash))
                +slash+doc);
        extensiones.put(extension,archivo);
    }

    /**
     * Obtiene la extensión de un archivo dado su nombre o ruta.
     *
     * @param s La ruta o nombre del archivo.
     * @return La extensión del archivo.
     */
    private String obtenerExtension(String s){
        int indice=s.lastIndexOf(".");
        return s.substring(indice);
    }
    /**
     * Obtiene las extensiones asociadas a una clave específica.
     * @param key La clave de la extensión.
     * @return Un HashMap que contiene la extensión y su directorio asociado.
     */
    public HashMap<String,File>getExtensiones(String key){
        File valor=extensiones.get(key);
        HashMap<String,File>ext=new HashMap<>();
        ext.put(key,valor);
        return ext;
    }
    /**
     * Verifica si una extensión ya existe en una categoría específica.
     * @param ext La extensión a verificar.
     * @param generic El mapa que contiene las extensiones.
     * @param <T> Tipo de mapa.
     * @return El nombre de la categoría si la extensión existe, de lo contrario "others".
     */
    private <T extends Map<String, HashSet<String>>>  String containsGeneric(String ext,T generic ){
        String extension=obtenerExtension(ext);
        for (Map.Entry<String, HashSet<String>> characterIntegerEntry : generic.entrySet()) {
            if (characterIntegerEntry.getValue().contains(extension)){
                return characterIntegerEntry.getKey();
            }
        }
        return "others";
    }
    /**
     * Agrega una extensión a una categoría personalizada.
     * @param ext La extensión a agregar.
     * @param categoria La categoría a la que se va a agregar la extensión.
     * @throws IllegalArgumentException Si la extensión ya existe en la categoría especificada.
     * @throws IllegalArgumentException Si la extensión no tiene el formato válido.
     * @throws IllegalArgumentException Si la categoría especificada no existe.
     */
    public void agregarExtension(String ext,String categoria){
        categoria=categoria.toLowerCase();
        ext=ext.toLowerCase();
        if (extensionVal(ext)) {
            if (custom.containsKey(categoria)) {
                String cat = containsGeneric(ext, custom);
                if (cat.equals("others")) {
                    custom.get(categoria).add(ext);
                    guardarCustom();
                    return;
                }
                throw new IllegalArgumentException("Extension en la categoria " + cat);
            }
            throw new IllegalArgumentException("no existe es categoria");
        }else {
            throw new IllegalArgumentException(" Extension no valida, debe ser con un . al principio");
        }
    }
    /**
     * Verifica si una extensión tiene el formato válido.
     * @param ext La extensión a verificar.
     * @return True si la extensión tiene el formato válido, de lo contrario False.
     */
    private boolean extensionVal(String ext) {
        ext=ext.trim();
        return ext.indexOf(".")==0;
    }
    /**
     * Elimina una extensión de una categoría personalizada.
     * @param ext La extensión a eliminar.
     * @param categoria La categoría de la que se eliminará la extensión.
     */
    public void eliminarExtension(String ext,String categoria){
        categoria=categoria.toLowerCase();
        ext=ext.toLowerCase();
        if (custom.containsKey(categoria)) {
            System.out.println(ext);
            if ( custom.get(categoria).remove(ext)){
                System.out.println("eliminado");
                guardarCustom();
            }
            return;
        }
        throw new IllegalArgumentException("no existe es categoria");
    }
    /**
     * Agrega una nueva categoría de archivos personalizada.
     * @param categoria La categoría a agregar.
     */
    public void agregarCategoria(String categoria){

        categoria=categoria.toLowerCase();
        if (!custom.containsKey(categoria)){
            custom.put(categoria,new HashSet<>());
            guardarCustom();
            return;
        }
        throw new IllegalArgumentException("Categoria existente");
    }
    /**
     * Elimina una categoría de archivos personalizada.
     * @param categoria La categoría a eliminar.
     */
    public void eliminarCategoria(String categoria){
        categoria=categoria.toLowerCase();
        if (custom.containsKey(categoria)){
            custom.remove(categoria);
            guardarCustom();
            return;
        }
        throw  new IllegalArgumentException("No existe esa categoria");
    }
    public void guardarCustom(){
        try (ObjectOutputStream objectInputStream= new ObjectOutputStream(new FileOutputStream("./datos.bin"))){{
            objectInputStream.writeObject(custom);
        }} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //@SuppressWarnings("unchecked") //Cambiar y debo comprobar el casteo
    public void traerDatos(){
        if (new File("./datos.bin").exists()){
            try (ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream("./datos.bin"))){

                custom = (HashMap<String, HashSet<String>>) objectInputStream.readObject();

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            guardarCustom();
        }
    }
    private void setType(){
        HashSet<String> text = new HashSet<>(List.of(
                ".txt", ".rtf", ".md", ".nfo", ".log", ".ini", ".cfg", ".bat", ".cmd",
                ".doc", ".docx", ".odt", ".pages", ".wps", ".wks", ".docm", ".dotx", ".dotm"
        ));
        HashSet<String> image = new HashSet<>(List.of(
                ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg", ".ico", ".tif", ".tiff", ".webp",
                ".psd", ".tga", ".eps", ".cdr"
        ));
         HashSet<String> audio = new HashSet<>(List.of(
                ".mp3", ".wav", ".ogg", ".flac", ".aac", ".wma", ".m4a", ".opus", ".mid", ".midi",
                ".dsf", ".dffs"
        ));
         HashSet<String> video = new HashSet<>(List.of(
                ".mp4", ".avi", ".mov", ".mkv", ".wmv", ".flv", ".webm", ".mpeg", ".mpg", ".3gp",
                ".prores", ".dnxhd"
        ));
        HashSet<String> document = new HashSet<>(List.of(
                ".pdf", ".epub", ".mobi", ".azw3", ".azw4", ".cbz", ".cbr", ".xls", ".xlsx"
        ));
         HashSet<String> spreadsheet = new HashSet<>(List.of(
                ".csv", ".ods", ".ots", ".sxc", ".stc"
        ));
         HashSet<String> presentation = new HashSet<>(List.of(
                ".ppt", ".pptx", ".odp", ".pps", ".potx", ".pot", ".pptm", ".thmx"
        ));
         HashSet<String> compressed = new HashSet<>(List.of(
                ".zip", ".rar", ".7z", ".tar.gz", ".tar", ".gz", ".bz2", ".xz", ".rar5", ".zpaq", ".pea"
        ));
        HashSet<String> sourceCode = new HashSet<>(List.of(
                ".java", ".cpp", ".c", ".py", ".html", ".css", ".js", ".php", ".rb", ".swift", ".go", ".cs", ".vb", ".asm", ".pas",
                ".sql", ".db", ".sqlite", ".db3", ".accdb", ".mdb"
        ));
        HashSet<String> executable = new HashSet<>(List.of(
                ".exe", ".jar", ".sh", ".bat", ".cmd", ".app", ".run", ".msi", ".com", ".vb", ".apk", ".ipa",
                ".elf", ".bin", ".out", ".exe.gz", ".exe.bz2", ".deb", ".rpm", ".apk", ".ipa",
                ".app", ".dmg", ".pkg", ".mpkg"
        ));
         HashSet<String> config = new HashSet<>(List.of(
                ".xml", ".json", ".properties", ".ini", ".cfg", ".yaml", ".yml", ".env"
        ));

        type.put("text",text);
        type.put("image",image);
        type.put("audio",audio);
        type.put("video",video);
        type.put("document",document);
        type.put("spreadsheet",spreadsheet);
        type.put("presentation",presentation);
        type.put("compressed",compressed);
        type.put("sourceCode",sourceCode);
        type.put("executable",executable);
        type.put("config",config);
    }
    public HashMap<String, HashSet<String>> getType() {
        return type;
    }
    public HashMap<String, HashSet<String>> getCustom() {
        return custom;
    }

    //sobre carga el metodo
    public String contains(String ext,int numero){
        if (numero==1){
            return containsGeneric(ext,custom);
        }else {
            return containsGeneric(ext,type);
        }
    }
}
