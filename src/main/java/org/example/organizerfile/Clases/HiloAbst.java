package org.example.organizerfile.Clases;

public abstract class HiloAbst implements Runnable {

    protected int progress;
    protected int total;

    public abstract int getProgress();
    public abstract int getTotal();

   protected abstract void setProgress(int progress);
   protected abstract void setTotal(int total);
}
