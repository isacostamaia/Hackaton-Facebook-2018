package br.com.hackaton.vemcomigo;

public class MapClickedManager {

    private boolean clicked = false;
    private static MapClickedManager manager = new MapClickedManager();

    public static MapClickedManager getManagerInstance() {
        return manager;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }
}