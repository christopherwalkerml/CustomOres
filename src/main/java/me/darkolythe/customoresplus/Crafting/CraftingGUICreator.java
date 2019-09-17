package me.darkolythe.customoresplus.Crafting;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static java.lang.Math.min;

public class CraftingGUICreator {

    public CustomOresPlus main;
    public CraftingGUICreator(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public void recipeListGUI(Player player, byte page, String search) {
        Inventory gui = Bukkit.getServer().createInventory(player, 54, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Custom Recipe List");
        int i;
        for (i = 0; i < min(45, main.craftingmain.recipes.size() - (page * 45)); i++) {
            ItemStack item = main.craftingmain.recipes.get(i + (page * 45)).result[0].clone();
            ItemMeta itemmeta = item.getItemMeta();
            if (itemmeta.getDisplayName().toLowerCase().contains(search.toLowerCase())) { //Loop through every recipe and add the default lore to it, then display it in the GUI
                List<String> lore = new ArrayList<>();
                if (itemmeta.hasLore()) {
                    lore = itemmeta.getLore();
                }
                lore.add(ChatColor.GRAY + "---------------------");
                lore.add(ChatColor.GRAY + "Workstation: " + ChatColor.WHITE + main.craftingmain.recipes.get(i + (page * 45)).workstation);
                lore.add(ChatColor.GRAY + "");
                if (player.hasPermission("minexchange.recipeadmin")) {
                    lore.add(ChatColor.GRAY + "Left click to edit");
                    lore.add(ChatColor.GRAY + "Right click to delete");
                } else {
                    lore.add(ChatColor.GRAY + "Click to view recipe");
                }
                lore.add(ChatColor.GRAY + "---------------------");
                itemmeta.setLore(lore);
                item.setItemMeta(itemmeta);

                gui.setItem(i, item);
            }
        }
        if (i == main.craftingmain.recipes.size() - (page * 45)) {
            if (player.hasPermission("customoresplus.recipeadmin")) {
                ItemStack item = new ItemStack(Material.GREEN_CONCRETE, 1);
                ItemMeta itemmeta = item.getItemMeta();
                itemmeta.setDisplayName(ChatColor.GREEN + "Create new recipe");
                itemmeta.setLore(Arrays.asList(ChatColor.BLACK + "" + ChatColor.STRIKETHROUGH + ""));
                item.setItemMeta(itemmeta);
                gui.setItem(i, item);
            }
        }

        if (main.craftingmain.recipes.size() > (page * 45) + 45) {
            ItemStack pageitem = new ItemStack(Material.ARROW);
            ItemMeta pagemeta = pageitem.getItemMeta();
            pagemeta.setDisplayName(ChatColor.GRAY + "Click to go to page " + (page + 2));
            pageitem.setItemMeta(pagemeta);
            gui.setItem(51, pageitem.clone());
        }
        if (page > 0) {
            ItemStack pageitem = new ItemStack(Material.ARROW);
            ItemMeta pagemeta = pageitem.getItemMeta();
            pagemeta.setDisplayName(ChatColor.GRAY + "Click to go to page " + page);
            pageitem.setItemMeta(pagemeta);
            gui.setItem(47, pageitem.clone());
        }
        ItemStack searchitem = new ItemStack(Material.COMPASS, 1);
        ItemMeta searchmeta = searchitem.getItemMeta();
        if (!search.equals("")) {
            searchmeta.setDisplayName(ChatColor.GRAY + "Searching... " + ChatColor.BOLD.toString() + "'" + search.toLowerCase() + "'");
        } else {
            searchmeta.setDisplayName(ChatColor.GRAY + "Click here to search for a recipe");
        }
        searchitem.setItemMeta(searchmeta);
        gui.setItem(49, searchitem);

        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Back to main page");
        item.setItemMeta(meta);
        gui.setItem(53, item.clone());

        player.openInventory(gui);
    }

    public void chooseWorkstationGUI(Player player) {
        Inventory gui = Bukkit.getServer().createInventory(player, InventoryType.HOPPER, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Choose Workstation");

        ItemStack item = new ItemStack(Material.CRAFTING_TABLE, 1);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.BLUE + "New special crafting recipe");
        itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "---------------------", ChatColor.GRAY + "Special Crafting occurs", ChatColor.GRAY + "in the silver anvil"));
        item.setItemMeta(itemmeta);
        gui.setItem(1, item);

        item = new ItemStack(Material.ANVIL, 1);
        itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.BLUE + "New refinery recipe");
        itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "---------------------", ChatColor.GRAY + "Refining occurs in the", ChatColor.GRAY + "Refinery mk. I or mk. II"));
        item.setItemMeta(itemmeta);
        gui.setItem(2, item);

        item = new ItemStack(Material.BARRIER, 1);
        itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.RED + "Go back to recipe list");
        item.setItemMeta(itemmeta);
        gui.setItem(4, item);

        player.openInventory(gui);
    }

    public void createSpecialCraftingMenu(Player player, Recipe recipe) {
        Inventory gui = Bukkit.getServer().createInventory(player, 45, ChatColor.LIGHT_PURPLE + "Create Special Crafting Recipe");
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
        }
        for (Integer index : main.craftingmain.specialCraftingSlots.keySet()) {
            if ((index != 41 && index != 43) || player.hasPermission("customoresplus.recipeadmin")) {
                gui.setItem(index, main.craftingmain.specialCraftingSlots.get(index));
            }
        }
        if (!player.hasPermission("customoresples.recipeadmin")) {
            setBarrier(gui);
        }
        if (recipe != null) {
            gui.setItem(10, recipe.recipe[0]);
            gui.setItem(11, recipe.recipe[1]);
            gui.setItem(12, recipe.recipe[2]);
            gui.setItem(19, recipe.recipe[3]);
            gui.setItem(20, recipe.recipe[4]);
            gui.setItem(21, recipe.recipe[5]);
            gui.setItem(28, recipe.recipe[6]);
            gui.setItem(29, recipe.recipe[7]);
            gui.setItem(30, recipe.recipe[8]);
            gui.setItem(24, recipe.result[0]);
        }
        player.openInventory(gui);
    }

    public void createRefineryMenu(Player player, Recipe recipe) {
        Inventory gui = Bukkit.getServer().createInventory(player, 45, ChatColor.LIGHT_PURPLE + "Create Refinery Recipe");
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
        }
        for (Integer index: main.craftingmain.refinerySlots.keySet()) {
            if ((index != 41 && index != 43 && index != 22) || player.hasPermission("customoresplus.recipeadmin")) {
                gui.setItem(index, main.craftingmain.refinerySlots.get(index));
            }
        }
        if (!player.hasPermission("customoresples.recipeadmin")) {
            setBarrier(gui);
        }
        if (recipe != null) {
            gui.setItem(11, recipe.recipe[0]);
            gui.setItem(10, recipe.recipe[1]);
            gui.setItem(29, recipe.fuel);
            gui.setItem(24, recipe.result[0]);
            gui.setItem(25, recipe.result[1]);
            ItemStack time = new ItemStack(Material.CLOCK);
            ItemMeta timemeta = time.getItemMeta();
            timemeta.setDisplayName(ChatColor.GOLD.toString() + (recipe.refineTime / 1000) + " seconds to refine");
            if (player.hasPermission("customoresplus.recipeadmin")) {
                timemeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to increase by 1 second", ChatColor.GRAY + "Right click to decrease by 1 second"));
            }
            time.setItemMeta(timemeta);
            gui.setItem(22, time);
            if (recipe.tier == 2) {
                ItemStack item = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + "Mark II Refinery");
                meta.setLore(Arrays.asList(ChatColor.BLUE.toString() + ChatColor.ITALIC.toString() + "Click to make the recipe",
                        ChatColor.BLUE.toString() + ChatColor.ITALIC.toString() + "require a Mark I Refinery"));
                item.setItemMeta(meta);
                gui.setItem(8, item);
            }
        }
        player.openInventory(gui);
    }

    private void setBarrier(Inventory gui) {
        ItemStack barrier = new ItemStack(Material.BARRIER, 1);
        ItemMeta bmeta = barrier.getItemMeta();
        bmeta.setDisplayName(ChatColor.RED + "Click to return to recipe list");
        barrier.setItemMeta(bmeta);
        gui.setItem(41, barrier);
    }
}
