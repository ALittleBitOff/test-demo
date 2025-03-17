package org.intensive.stage4;

import java.io.*;
import java.util.Base64;

class MyClassData implements Serializable {
    private String text;

    MyClassData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

class MyClassDataEx implements Externalizable {
    private String text;

    public MyClassDataEx() {}

    public MyClassDataEx(String text) {
        this.text = text;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        text = this.decriptingString((String) in.readObject());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.encriptingString(this.getText()));
    }

    private String encriptingString(String data) {
        String encriptData = Base64.getEncoder().encodeToString(data.getBytes());
        System.out.println(encriptData);
        return encriptData;
    }

    private String decriptingString(String data) {
        String decrypted = new String(Base64.getDecoder().decode(data));
        System.out.println(decrypted);
        return decrypted;
    }

    public String getText() {
        return text;
    }

}

public class MyClassStage4{

    public static void main(String[] args) throws FileNotFoundException {
        //Serializable
        MyClassData stageDate = new MyClassData(
                "this text need to write!");
//        stageDate.setText("this text need to write!");
        System.out.println(stageDate.getText());

        try (ObjectOutputStream txtOut = new ObjectOutputStream(
                new FileOutputStream("notes.txt"))) {
            txtOut.writeObject(stageDate);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try(ObjectInputStream txtIn = new ObjectInputStream(
                new FileInputStream("notes.txt"))){
            MyClassData temp = ((MyClassData)txtIn.readObject());
            System.out.printf("Deserialization %s \n",temp.getText());
        }
        catch (Exception  ex) {
            System.out.println(ex.getMessage());
        }

        //Externalizable

        MyClassDataEx txtOut2 = new MyClassDataEx(
                "Externalizable: this text need to write!");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("notesEx.txt"))) {
            objectOutputStream.writeObject(txtOut2);
//            objectOutputStream.close();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        try(ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream("notesEx.txt"))){
            MyClassDataEx temp1 = ((MyClassDataEx)objectInputStream.readObject());
//            System.out.println(temp1.getText());
//            objectInputStream.close();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        // список каталогов
        System.out.println("\n список каталогов \n");
        try {
            File dir = new File("/Users/andreysabancheev/Downloads/");
            File[] files = dir.listFiles();
            System.out.println("Список файлов:");
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //запись в файл в режиме добавления
        System.out.println("\n запись в файл в режиме добавления");

        try(FileWriter writer = new FileWriter("NotesWriter.txt",true)) {
            String text = "запись в файл в режиме добавления \n";
            writer.write(text);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}