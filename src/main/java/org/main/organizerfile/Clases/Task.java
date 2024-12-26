package org.main.organizerfile.Clases;
/**
 * Clase que representa una tarea en JavaFX.
 * Esta tarea se encarga de ejecutar un proceso encapsulado en un objeto que extiende la clase HiloAbst.
 *
 * @param <T> Tipo del objeto que representa el hilo.
 */
public class Task<T extends HiloAbst> extends javafx.concurrent.Task<Void> {

    private T copiarHilos;

    public Task(T copiarHilos) {
        this.copiarHilos = copiarHilos;
    }
    /**
     * Método que ejecuta la tarea.
     *
     * @return null
     * @throws Exception Si hay algún error durante la ejecución del hilo.
     */
    @Override
    public Void call() throws Exception {
        Thread thread = new Thread(copiarHilos);
        thread.start();
        updateProgress(0,copiarHilos.getTotal());
        while (thread.isAlive()) {
            updateProgress(copiarHilos.getProgress(), copiarHilos.getTotal());
            Thread.sleep(100); // retraso de la  imagen
        }
        updateProgress(copiarHilos.getProgress(), copiarHilos.getTotal());
        return null;
    }

}
