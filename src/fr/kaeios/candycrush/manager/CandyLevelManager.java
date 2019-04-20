package fr.kaeios.candycrush.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.game.elements.CandyLevel;
import fr.kaeios.candycrush.game.elements.CandyLevelTypeAdapter;

import java.io.File;
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

    /**
     * Add a new level
     * @param level level you want to add
     */
    public void addLevel(final CandyLevel level){
        levels.add(level);
    }

    /**
     * Get a candy level
     * @param level level number
     * @return CandyLevel with this number
     */
    public CandyLevel getCandyLevel(final int level){
        return levels.get(level-1);
    }

    /**
     * Check if level number exist
     * @param level level number
     * @return true if it exist
     */
    public boolean isLevel(final int level){
        return levels.size() >= level;
    }


    private void createFolder(){
        final File folder = new File(CandyCrush.getInstance().getDataFolder(), "levels/");
        // If folder is not present create it
        if(!folder.exists()) folder.mkdirs();
    }

    /**
     * Load all levels from files
     */
    public void loadLevels(){
        // Level number
        int level = 1;
        // While level exist as file
        while(isLevelFile(level)){
            // Load level file
            loadLevelFromFile(level);
            level++;
        }
    }

    private boolean isLevelFile(final int number){
        // Check if file exist
        final File file = new File(CandyCrush.getInstance().getDataFolder(), "levels/"+ number +".json");
        return file.exists();
    }

    private void loadLevelFromFile(final int number) {
        // Get file
        final File file = new File(CandyCrush.getInstance().getDataFolder(), "levels/"+ number +".json");
        if(!file.exists()) return;
        // Read content & Convert json text to CandyLevel object
        gson.fromJson(CandyCrush.getInstance().getFileManager().getTextFromFile(file), CandyLevel.class);
    }

    public List<CandyLevel> getLevels(){
        return levels;
    }

}
