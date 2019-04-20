package fr.kaeios.candycrush.game.elements;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public enum CandyType {

    NONE(0, "NONE"),
    GREEN(10, "§aVert"),
    ROSE(9, "§cRose"),
    PURPLE(5, "§5Violet"),
    BLUE(12, "§bBleu"),
    ORANGE(14, "§6Orange");

    private final int color;
    private final String display;

    CandyType(final int color, final String display){
        this.color = color;
        this.display = display;
    }

    /**
     * Get color of the candy
     * @return color number
     */
    public int getColor() {
        return color;
    }

    /**
     * Get the candy type of an item
     * @param item item you want to know the type
     * @return type of the item
     */
    public static CandyType getTypeOf(final ItemStack item){
        if(item == null || !item.getType().equals(Material.INK_SACK)) return NONE;
        final short durability = item.getDurability();
        for(final CandyType type : CandyType.values())
            if(type.color == durability) return type;
        return NONE;
    }

    /**
     * Get a random candy type
     * @return random candy type
     */
    public static CandyType getRandomCandyType(){
        final int index = new Random().nextInt(CandyType.values().length-1)+1;
        return CandyType.values()[index];
    }

    /**
     * Get display name of the candy
     * @return candy display name
     */
    public String getDisplay() {
        return display;
    }
}
