package fr.kaeios.candycrush.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.game.elements.CandyLevel;
import fr.kaeios.candycrush.game.elements.CandyLevelTypeAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class CandyLevelManager {

    private final List<CandyLevel> levels = new ArrayList<>();
    private final Gson gson;

    public CandyLevelManager(){
        createFolder();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(CandyLevel.class, new CandyLevelTypeAdapter())
                .create();
    }

    public void addLevel(final CandyLevel level){
        levels.add(level);
    }

    public CandyLevel getCandyLevel(final int level){
        return levels.get(level-1);
    }

    public boolean isLevel(final int level){
        return levels.size() >= level;
    }

    public void createFolder(){
        final File folder = new File(CandyCrush.getInstance().getDataFolder(), "levels/");
        if(!folder.exists()) folder.mkdirs();
    }

    public void loadLevels(){
        int level = 1;
        while(isLevelFile(level)){
            try {
                loadLevelFromFile(level);
            } catch (IOException e) {
                e.printStackTrace();
            }
            level++;
        }
    }

    public boolean isLevelFile(final int number){
        final File file = new File(CandyCrush.getInstance().getDataFolder(), "levels/"+ number +".json");
        return file.exists();
    }

    public void loadLevelFromFile(final int number) throws IOException {
        final File file = new File(CandyCrush.getInstance().getDataFolder(), "levels/"+ number +".json");
        if(!file.exists()) return;
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) sb.append(line);
        reader.close();
        gson.fromJson(sb.toString(), CandyLevel.class);
    }

    public List<CandyLevel> getLevels(){
        return levels;
    }

}
