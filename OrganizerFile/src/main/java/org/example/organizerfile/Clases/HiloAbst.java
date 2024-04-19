package org.example.organizerfile.Clases;


/**
 * Clase abstracta para la implementación de hilos.
 * Define métodos abstractos para obtener y establecer el progreso y el total de un proceso.
 */

public abstract class HiloAbst implements Runnable {
    protected int progress;
    protected int total;

    public abstract int getProgress();
    public abstract int getTotal();

   protected abstract void setProgress(int progress);
   protected abstract void setTotal(int total);
}
