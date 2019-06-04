package fr.kaeios.candycrush.game;

import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.CandyStats;
import fr.kaeios.candycrush.game.animations.CandyAnimation;
import fr.kaeios.candycrush.game.animations.CandyFallAnimation;
import fr.kaeios.candycrush.game.elements.CandyCombo;
import fr.kaeios.candycrush.game.elements.CandyLevel;
import fr.kaeios.candycrush.game.elements.CandyType;
import fr.kaeios.candycrush.game.sounds.CandyMusic;
import net.minecraft.server.v1_10_R1.ChatMessage;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
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
        this.menu = Bukkit.createInventory(getPlayer(), 54, "§eLevel - "+ level.getLevel() +"   §c"+ (level.getMoves()-moves) +" coups");
        // Reset points
        for(int i = 1; i<CandyType.values().length; i++){
            points.put(CandyType.values()[i], 0);
        }
    }

    /**
     * Get slot to swap with next clicked slot
     * @return slot number
     */
    public int getSlotToSwap() {
        return slotToSwap;
    }

    /**
     * Set slot to swap with next clicked slot
     * @param slotToSwap number of slot
     */
    public void setSlotToSwap(int slotToSwap) {
        this.slotToSwap = slotToSwap;
        if(slotToSwap == -1) return;
        menu.getItem(slotToSwap).setAmount(2);
    }

    /**
     * Get number of moves
     * @return number of moves
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Set number of moves
     * @param moves number of moves
     */
    public void setMoves(int moves) {
        this.moves = moves;
        updateTitle();
    }

    /**
     * Add a move
     */
    public void addMove(){
        setMoves(this.moves+1);
    }

    /**
     * Add point for the specified type
     * @param candy type of candy to add point
     */
    public void addPoint(final CandyType candy){
        points.put(candy, points.get(candy)+1);
    }

    /**
     * Get game level
     * @return current game level
     */
    public CandyLevel getLevel() {
        return level;
    }

    /**
     * Check if game is stopped
     * @return true if it is
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Check if game is playable
     * @return true if it is
     */
    public boolean isPlayable() {
        return playable;
    }

    /**
     * Set if game is playable
     * @param playable true if is otherwise false
     */
    public void setPlayable(final boolean playable) {
        this.playable = playable;
    }

    /**
     * Get game menu
     * @return game inventory
     */
    public Inventory getMenu(){
        return menu;
    }

    /**
     * Get player uuid
     * @return player uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Check if number of moves left is positive
     * @return true if it is
     */
    public boolean hasMoves(){
        return moves < level.getMoves();
    }

    /**
     * Start the game
     */
    public void start(){
        // Update stats
        final CandyStats stats = CandyStats.getStats(uuid);
        stats.setPlayed(stats.getPlayed()+1);
        stats.save();
        CandyCrush.getInstance().getGames().addGame(this);
        // Open menu
        getPlayer().openInventory(menu);
        updateStatsItem();
        // Launch fall animation
        final CandyFallAnimation animation = new CandyFallAnimation(this);
        animation.start();
        // Start music
        music = new CandyMusic(this);
        music.start();
    }

    /**
     * Set currently playing animation & stop the other
     * @param animation currently playing animation
     */
    public void setAnimation(final CandyAnimation animation){
        // Stop previous animation
        if(this.active != null && !active.isStopped()) active.stop();
        active = animation;
    }

    /**
     * Check if game is won
     * @return true if it is
     */
    public boolean isWin(){
        // Check global score needed
        if(getTotalPoints() < level.getWinScore()) return false;
        // Check score for each type
        for(final CandyType type : points.keySet()){
            if(points.get(type) < level.getCandiesNeeded(type)) return false;
        }
        return true;
    }

    /**
     * Update item with scores
     */
    public void updateStatsItem(){
        // Create item
        final ItemStack item = new ItemStack(Material.GOLD_BLOCK, 1);
        final ItemMeta meta = item.getItemMeta();
        // Store global score
        int total = 0;
        final List<String> lines = new ArrayList<>();
        lines.add("§c ");
        // Load score for each type
        for(final CandyType type : points.keySet()){
            final int point = points.get(type);
            final String line = type.getDisplay() + "§7: "+ (point >= level.getCandiesNeeded(type) ? "§a" : "§c") + point +"/"+ level.getCandiesNeeded(type);
            // Add to global score
            total += points.get(type);
            lines.add(line);
        }
        // Set global score as name & types scores as lore
        meta.setDisplayName("§cPoints §7("+ (total >= level.getWinScore() ? "§a" : "§c") +  total+"/"+level.getWinScore() +"§7)");
        meta.setLore(lines);
        item.setItemMeta(meta);
        // Put item in inventory
        getPlayer().getInventory().setItem(13, item);
    }

    /**
     * Stop the game
     */
    public void stop(){
        stopped = true;
        // Stop animation
        if(!active.isStopped()) active.stop();
        // Close inventory
        if(getPlayer().getOpenInventory() != null) getPlayer().closeInventory();
        setPlayable(false);
        // Clear scores item
        getPlayer().getInventory().setItem(13, null);
        // Remove game from GameManager
        CandyCrush.getInstance().getGames().removeGame(uuid);
        music.stop();
    }


    /**
     * Check if player can swap the item to swap with this slot
     * @param slot slot to check
     * @return true if it can
     */
    public boolean canSwap(final int slot){
        // Check if slot is next to swap slot
        if(!isNextTo(slot, slotToSwap)){
            // Set amount back to 1
            menu.getItem(slotToSwap).setAmount(1);
            return false;
        }
        // Try to move and look for combo
        swap(slot);
        if(isCombo(slot) || isCombo(slotToSwap)){
            //If there is combo return can swap & cancel swap
            swap(slot);
            return true;
        }
        // Cancel the swap
        swap(slot);
        return false;
    }

    /**
     * Swap the slot with previously selected slot
     * @param slot slot to swap
     */
    public void swap(final int slot){
        final ItemStack slotItem = menu.getItem(slot);
        final ItemStack swapItem = menu.getItem(slotToSwap);
        swapItem.setAmount(1);
        menu.setItem(slot, swapItem);
        menu.setItem(slotToSwap, slotItem);
    }

    /**
     * Get all possibles combo in the game
     * @return List of combos
     */
    public List<CandyCombo> getPossibleCombos(){
        // Get horizontal combos + vertical combos
        final List<CandyCombo> combos = getHorizontalCombos();
        combos.addAll(getVerticalCombos());
        return combos;
    }

    private List<CandyCombo> getVerticalCombos(){
        final List<CandyCombo> combos = new ArrayList<>();
        // Loop through all columns of the inventory
        for(int column=0; column<9; column++){
            // Loop through the first 4 rows (no need to check the two other because a candy line as a minimum length of 3)
            int row = 0;
            while(row < 4) {
                // Get candy info on this slot
                final CandyType candy = getCandyAt(row, column);
                final int color = candy.getColor();
                final int slot = getSlotAt(row, column);
                // Check if the two slot under this slot have the same candy type
                if(color == menu.getItem(slot+9).getDurability() &&
                        color == menu.getItem(slot+18).getDurability()){
                    // If they have the same type create a new vertical combo
                    final CandyCombo combo = new CandyCombo(CandyCombo.Direction.VERTICAL);
                    do {
                        // Add all candies connected to this line vertically
                        combo.addCandy(getSlotAt(row, column));
                        row+=1;
                    }while(row < 6 && getCandyAt(row, column).equals(candy));
                    // Add combo to combos list
                    combos.add(combo);
                }
                row+=1;
            }
        }
        return combos;
    }

    private List<CandyCombo> getHorizontalCombos(){
        final List<CandyCombo> combos = new ArrayList<>();
        // Loop through each row
        for(int row=0; row<6; row++){
            // Loop through the first 7 columns (because minimal length of a combo is 3)
            int column = 0;
            while(column < 7) {
                // Get candy info for this position
                final CandyType candy = getCandyAt(row, column);
                final int slot = getSlotAt(row, column);
                final int color = candy.getColor();
                // Check if the is a combo
                if(color == menu.getItem(slot+1).getDurability() &&
                        color == menu.getItem(slot+2).getDurability()){
                    // If it is create a new combo
                    final CandyCombo combo = new CandyCombo(CandyCombo.Direction.HORIZONTAL);
                    do {
                        // Add all vertically connected candies to the combo
                        combo.addCandy(getSlotAt(row, column));
                        column+=1;
                    }while(column < 9 && menu.getItem(getSlotAt(row, column)).getDurability() == color);
                    // Add combo to combos list
                    combos.add(combo);
                }
                column+=1;
            }
        }
        return combos;
    }

    private boolean isCombo(final int slot){
        // Get candy type of this slot
        final CandyType candy = CandyType.getTypeOf(menu.getItem(slot));
        // Range to check candy
        final int range = 3;

        // Look for combo horizontally
        int horizontal = 1;
        // Look for candies to the left
        for(int i = 1; i<range; i++){
            // get the slot to the left with i as offset
            final int leftSlot = slot-i;
            // If slot is invalid or candy is not the same type stop the loop
            if(leftSlot/9 != slot/9) break;
            if(leftSlot < 0) break;
            if(!CandyType.getTypeOf(menu.getItem(leftSlot)).equals(candy)) break;
            // Increment horizontal amount of candies
            horizontal++;
        }
        // Look for candies to the right
        for(int i = 1; i<range; i++){
            // get the slot to the right with i as offset
            final int rightSlot = slot+i;
            // If slot is invalid or candy is not the same type stop the loop
            if(rightSlot/9 != slot/9) break;
            if(rightSlot > 53) break;
            if(!CandyType.getTypeOf(menu.getItem(rightSlot)).equals(candy)) break;
            // Increment horizontal amount of candies
            horizontal++;
        }

        // If there is combo return immediately to save resources
        if(horizontal >= range) return true;

        // Look for combo vertically
        int vertical = 1;
        for(int i = 1; i<range; i++){
            // Get slot upper with i as offset
            final int upperSlot = slot-9*i;
            // If slot is invalid or not of the same type stop the loop
            if(upperSlot < 0) break;
            if(!CandyType.getTypeOf(menu.getItem(upperSlot)).equals(candy)) break;
            // Increment vertical amount of candies
            vertical++;
        }
        for(int i = 1; i<range; i++){
            // Get slot under with i as offset
            final int underSlot = slot+9*i;
            // If slot is invalid or not of the same type stop the loop
            if(underSlot > 53) break;
            if(!CandyType.getTypeOf(menu.getItem(underSlot)).equals(candy)) break;
            // Increment vertical amount of candies
            vertical++;
        }

        // Return true if there is 3 or more candies
        return vertical >= range;
    }

    // Check if slot are next to each other
    private boolean isNextTo(final int slot1, final int slot2){
        if(slot1/9 == slot2/9){
            // Horizontally
            return slot1 + 1 == slot2 || slot1 - 1 == slot2;
        }else{
            // Vertically
            return (slot1 + 9 < 54 && slot1 + 9 == slot2) || (slot1 - 9 >= 0 && slot1 - 9 == slot2);
        }
    }

    /**
     * Check if candies can fall
     * @return true if they can
     */
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

    /**
     * Fall the candies
     */
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

    /**
     * Fill gaps on the top of the inventory
     */
    public void fillTopGaps(){
        IntStream.range(0, 9).forEach(slot ->{
            if(menu.getItem(slot) != null) return;
            final ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) CandyType.getRandomCandyType().getColor());
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§f ");
            item.setItemMeta(meta);
            menu.setItem(slot, item);
        });
    }

    /**
     * Update players stats
     */
    public void updateStats(){
        final CandyStats stats = CandyStats.getStats(uuid);
        final int total = getTotalPoints();
        if(stats.getBest() < getTotalPoints()) stats.setBest(total);
        stats.setTotal(stats.getTotal()+total);
        if(isWin()){
            stats.setWin(stats.getWin()+1);
            if(level.getLevel() == stats.getCurrent()) stats.setCurrent(stats.getCurrent()+1);
        } else {
            stats.setLose(stats.getLose()+1);
        }
        stats.save();
    }

    /**
      * Convert row & column to slot number
      */
    private int getSlotAt(final int row, final int column){
        return row*9+column;
    }

    /**
     * Get total amount of points
     * @return score of the game
     */
    private int getTotalPoints(){
        int total = 0;
        // Add each type points to the total
        for(final CandyType type : points.keySet()){
            total += points.get(type);
        }
        return total;
    }

    /**
     * Get player which is playing
     * @return Bukkit player
     */
    private Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    // Get the candy type at this location
    private CandyType getCandyAt(final int row, final int column){
        // Get candy type at location
        return CandyType.getTypeOf(menu.getItem(getSlotAt(row, column)));
    }

    // Update title of inventory
    private void updateTitle(){
        EntityPlayer ep = ((CraftPlayer) getPlayer()).getHandle();
        // Create new inventory with packet
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:chest", new ChatMessage("§eLevel - "+ level.getLevel() +"   §c"+ (level.getMoves()-moves) +" coups"), getPlayer().getOpenInventory().getTopInventory().getSize());
        // Send packet to player
        ep.playerConnection.sendPacket(packet);
        // Fill client side inventory
        ep.updateInventory(ep.activeContainer);
    }

}
