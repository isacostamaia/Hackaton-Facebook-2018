package br.com.hackaton.vemcomigo;

public class UserUtils {

    private boolean hasRide = false;
    private static UserUtils manager = new UserUtils();

    public static UserUtils getInstance() {
        return manager;
    }

    public void setHasRide(boolean hasRide) {
        this.hasRide = hasRide;
    }

    public boolean hasRide() {
        return hasRide;
    }
}
