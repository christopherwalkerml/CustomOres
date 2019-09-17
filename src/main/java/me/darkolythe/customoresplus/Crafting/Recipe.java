package me.darkolythe.customoresplus.Crafting;

import org.bukkit.inventory.ItemStack;

public class Recipe {

    public Recipe(String workstation, ItemStack[] recipe, ItemStack[] result) {
        this.workstation = workstation;
        this.recipe = recipe;
        this.result = result;
    }

    public String workstation;
    public ItemStack[] recipe;
    public ItemStack[] result;
    public ItemStack fuel;
    public byte tier;
    public int refineTime = 0;
}