package fr.kaeios.candycrush.manager;

import java.io.*;

public class FileManager {

    public String getTextFromFile(final File file) {
        if(!file.exists()) return "";

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuilder sb = new StringBuilder();
            String line;
            // put all lines together
            while((line = reader.readLine()) != null) sb.append(line);
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void writeTextToFile(final File file, final String text){
        if(!file.exists()) createFile(file);
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile(final File file){
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

}
