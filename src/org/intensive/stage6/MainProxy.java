package org.intensive.stage6;

public class MainProxy {
    public static void main(String[] args) {
        ImageFile file = new ImageFile("ImageFile");
        file.load();
        file.load();
    }
}
