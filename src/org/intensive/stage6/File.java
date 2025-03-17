package org.intensive.stage6;

public class File implements ProxyFile{
    private String path;

    public File(String path) {
        this.path = path;
        firstLoad();
    }

    private void firstLoad() {
        System.out.println("First load path: " + path);
    }

    @Override
    public void load() {
        System.out.println("Loading " + path);
    }
}
