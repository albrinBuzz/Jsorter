package org.example.organizerfile.Clases;

public class Task<T extends HiloAbst> extends javafx.concurrent.Task<Void> {

    private T copiarHilos;

    public Task(T copiarHilos) {
        this.copiarHilos = copiarHilos;
    }

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
