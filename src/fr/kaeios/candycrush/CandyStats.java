package fr.kaeios.candycrush;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.kaeios.candycrush.manager.FileManager;

import java.io.File;
import java.util.UUID;

public class CandyStats {

    private static FileManager fileManager = CandyCrush.getInstance().getFileManager();
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private final UUID uuid;

    /*
    Games played
     */

    private int played = 0;
    private int win = 0;
    private int lose = 0;

    /*
    Scores
     */

    private long total = 0;
    private long best = 0;

    /*
    Level
     */
    private int current = 1;

    public CandyStats(final UUID player){
        this.uuid = player;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBest() {
        return best;
    }

    public void setBest(long best) {
        this.best = best;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void save(){
        final String json = gson.toJson(this);
        fileManager.writeTextToFile(new File(CandyCrush.getInstance().getDataFolder(), "players/"+ uuid.toString() +".json"), json);
    }

    public static CandyStats getStats(final UUID uuid){
        final File file = new File(CandyCrush.getInstance().getDataFolder(), "players/"+ uuid.toString() +".json");
        if(!file.exists()) return new CandyStats(uuid);
        return gson.fromJson(fileManager.getTextFromFile(file), CandyStats.class);
    }

}
