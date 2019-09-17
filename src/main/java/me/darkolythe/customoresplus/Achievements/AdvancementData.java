package me.darkolythe.customoresplus.Achievements;

import org.bukkit.inventory.ItemStack;

public class AdvancementData {

    public String name;
    public String pool;
    public String parent; //can be null
    public ItemStack icon;
    public String description;
    public boolean hidden;
    public ItemStack requiredItem; //can be null
    public int index;

}
