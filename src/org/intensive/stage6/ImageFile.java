package org.intensive.stage6;

public class ImageFile implements ProxyFile {
    private String fileName;
    private File path;

    public ImageFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void load() {
        if(path == null) {
            path = new File(fileName);
        }
        System.out.println("interface imagefile " + fileName);
        path.load();
    }
}

