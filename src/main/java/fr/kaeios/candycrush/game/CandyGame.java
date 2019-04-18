package fr.kaeios.candycrush.game;

import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.game.animations.CandyAnimation;
import fr.kaeios.candycrush.game.animations.CandyFallAnimation;
import fr.kaeios.candycrush.game.elements.CandyCombo;
import fr.kaeios.candycrush.game.elements.CandyLevel;
import fr.kaeios.candycrush.game.elements.CandyType;
import fr.kaeios.candycrush.game.sounds.CandyMusic;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.IntStream;

public class CandyGame {

    private final UUID uuid;
    private final Inventory menu;
    private boolean playable = false;
    private int slotToSwap = -1;
    private CandyMusic music;
    private boolean stopped = false;

    private final CandyLevel level;
    private int moves = 0;
    private Map<CandyType, Integer> points = new HashMap<>();

    private CandyAnimation active;

    public CandyGame(final UUID uuid, final CandyLevel level){
        this.uuid = uuid;
        this.level = level;
        this.menu = Bukkit.createInventory(null, 54, "§cCandyCrush §e(Level - "+ level.getLevel() +")");
        // Reset points
        for(int i = 1; i<CandyType.values().length; i++){
            points.put(CandyType.values()[i], 0);
        }
    }

    public void start(){
        CandyCrush.getInstance().getGames().addGame(this);
        // Open menu
        getPlayer().openInventory(menu);
        updateStatsItem();
        // Launch fall animation
        final CandyFallAnimation animation = new CandyFallAnimation(this);
        animation.start();
        music = new CandyMusic(this);
        music.start();
    }

    public void setAnimation(final CandyAnimation animation){
        if(this.active != null && !active.isStopped()) active.stop();
        active = animation;
    }

    public boolean isWin(){
        if(getTotalPoints() < level.getWinScore()) return false;
        for(final CandyType type : points.keySet()){
            if(points.get(type) < level.getCandiesNeeded(type)) return false;
        }
        return true;
    }

    public int getTotalPoints(){
        int total = 0;
        for(final CandyType type : points.keySet()){
            total += points.get(type);
        }
        return total;
    }

    public void updateStatsItem(){
        final ItemStack item = new ItemStack(Material.GOLD_BLOCK, 1);
        final ItemMeta meta = item.getItemMeta();
        int total = 0;
        final List<String> lines = new ArrayList<>();
        lines.add("§c ");
        for(final CandyType type : points.keySet()){
            final String line = type.getDisplay() + "§7: §c" + points.get(type) +"/"+ level.getCandiesNeeded(type);
            total += points.get(type);
            lines.add(line);
        }
        meta.setDisplayName("§cPoints §7("+ total+"/"+level.getWinScore() +")");
        meta.setLore(lines);
        item.setItemMeta(meta);
        getPlayer().getInventory().setItem(13, item);
    }

    public void stop(){
        stopped = true;
        if(!active.isStopped()) active.stop();
        if(getPlayer().getOpenInventory() != null) getPlayer().closeInventory();
        setPlayable(false);
        getPlayer().getInventory().setItem(13, null);
        CandyCrush.getInstance().getGames().removeGame(uuid);
        music.stop();
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(final boolean playable) {
        this.playable = playable;
    }

    public CandyType getCandyAt(final int row, final int column){
        return CandyType.getTypeOf(menu.getItem(getSlotAt(row, column)));
    }

    public Inventory getMenu(){
        return menu;
    }

    public UUID getUuid() {
        return uuid;
    }

    private Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public boolean canSwap(final int slot){
        if(!isNextTo(slot, slotToSwap)) return false;
        swap(slot);
        if(isCombo(slot) || isCombo(slotToSwap)){
            swap(slot);
            return true;
        }
        swap(slot);
        return false;
    }

    public void swap(final int slot){
        final ItemStack slotItem = menu.getItem(slot);
        final ItemStack swapItem = menu.getItem(slotToSwap);
        menu.setItem(slot, swapItem);
        menu.setItem(slotToSwap, slotItem);
    }

    public List<CandyCombo> getPossibleCombos(){
        final List<CandyCombo> combos = getHorizontalCombos();
        combos.addAll(getVerticalCombos());
        return combos;
    }

    public List<CandyCombo> getVerticalCombos(){
        final List<CandyCombo> combos = new ArrayList<>();
        for(int column=0; column<9; column++){
            int row = 0;
            while(row < 4) {
                final CandyType candy = getCandyAt(row, column);
                final int color = candy.getColor();
                final int slot = getSlotAt(row, column);
                if(color == menu.getItem(slot+9).getDurability() &&
                        color == menu.getItem(slot+18).getDurability()){
                    final CandyCombo combo = new CandyCombo(CandyCombo.Direction.VERTICAL);
                    do {
                        combo.addCandy(getSlotAt(row, column));
                        row+=1;
                    }while(row < 6 && getCandyAt(row, column).equals(candy));
                    combos.add(combo);
                }
                row+=1;
            }
        }
        return combos;
    }

    public boolean isCombo(final int slot){
        final CandyType candy = CandyType.getTypeOf(menu.getItem(slot));
        final int range = 3;

        int horizontal = 1;
        for(int i = 1; i<range; i++){
            final int leftSlot = slot-i;
            if(leftSlot/9 != slot/9) break;
            if(leftSlot < 0) break;
            if(!CandyType.getTypeOf(menu.getItem(leftSlot)).equals(candy)) break;
            horizontal++;
        }
        for(int i = 1; i<range; i++){
            final int rightSlot = slot+i;
            if(rightSlot/9 != slot/9) break;
            if(rightSlot < 0) break;
            if(!CandyType.getTypeOf(menu.getItem(rightSlot)).equals(candy)) break;
            horizontal++;
        }

        int vertical = 1;
        for(int i = 1; i<range; i++){
            final int upperSlot = slot-9*i;
            if(upperSlot < 0) break;
            if(!CandyType.getTypeOf(menu.getItem(upperSlot)).equals(candy)) break;
            vertical++;
        }
        for(int i = 1; i<range; i++){
            final int underSlot = slot+9*i;
            if(underSlot > 53) break;
            if(!CandyType.getTypeOf(menu.getItem(underSlot)).equals(candy)) break;
            vertical++;
        }

        return horizontal >= range || vertical >= range;
    }

    public boolean isNextTo(final int slot1, final int slot2){
        if(slot1/9 == slot2/9){
            // A coté horizontalement
            return slot1 + 1 == slot2 || slot1 - 1 == slot2;
        }else{
            // A coté verticalement
            return (slot1 + 9 < 54 && slot1 + 9 == slot2) || (slot1 - 9 >= 0 && slot1 - 9 == slot2);
        }
    }

    public boolean hasMoves(){
        return moves < level.getMoves();
    }

    public List<CandyCombo> getHorizontalCombos(){
        final List<CandyCombo> combos = new ArrayList<>();
        for(int row=0; row<6; row++){
            int column = 0;
            while(column < 7) {
                final CandyType candy = getCandyAt(row, column);
                final int slot = getSlotAt(row, column);
                final int color = candy.getColor();

                if(color == menu.getItem(slot+1).getDurability() &&
                        color == menu.getItem(slot+2).getDurability()){
                    final CandyCombo combo = new CandyCombo(CandyCombo.Direction.HORIZONTAL);
                    do {
                        combo.addCandy(getSlotAt(row, column));
                        column+=1;
                    }while(column < 9 && menu.getItem(getSlotAt(row, column)).getDurability() == color);
                    combos.add(combo);
                }
                column+=1;
            }
        }
        return combos;
    }

    public boolean canCandyFall(){
        for(int column = 0; column < 9; column++){
            int row = 5;
            boolean hasGap = false;
            while(row >= 0){
                if(getCandyAt(row, column).equals(CandyType.NONE)) hasGap = true;
                else if(hasGap) return true;
                row--;
            }
        }
        return false;
    }

    public void handleFallCandy(){
        IntStream.range(0, 45).forEach(num ->{
            final int slot = 44-num;
            final ItemStack slotItem = menu.getItem(slot);
            if(slotItem == null) return;
            final int under = slot+9;
            if(menu.getItem(under) != null) return;
            menu.setItem(under, slotItem);
            menu.setItem(slot, null);
        });
    }

    public void fillTopGaps(){
        IntStream.range(0, 9).forEach(slot ->{
            if(menu.getItem(slot) == null) menu.setItem(slot, new ItemStack(Material.INK_SACK, 1, (short) CandyType.getRandomCandyType().getColor()));
        });
    }

    public int getSlotAt(final int row, final int column){
        return row*9+column;
    }

    public int getSlotToSwap() {
        return slotToSwap;
    }

    public void setSlotToSwap(int slotToSwap) {
        this.slotToSwap = slotToSwap;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void addMove(){
        this.moves++;
    }

    public void addPoint(final CandyType candy){
        points.put(candy, points.get(candy)+1);
    }

    public CandyLevel getLevel() {
        return level;
    }

    public boolean isStopped() {
        return stopped;
    }

}
