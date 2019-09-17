package me.darkolythe.customoresplus.Crafting;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CraftingMain {

    public CustomOresPlus main;
    public CraftingMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public List<Recipe> recipes = new ArrayList<>();
    public Map<Integer, ItemStack> specialCraftingSlots = new HashMap<>();
    public Map<Integer, ItemStack> refinerySlots = new HashMap<>();
    public Map<Player, ItemStack> catchChat = new HashMap<>();
    public Map<Player, String> catchType = new HashMap<>();
    public Map<Player, String> search = new HashMap<>();

    public void setUp() {
        createSpecialCraftingSlots();
        createRefinerySlots();
    }

    public void createSpecialCraftingSlots() {
        ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Drag crafting requirements here");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "-------------", ChatColor.GRAY + "Includes:", ChatColor.GRAY + "Durability", ChatColor.GRAY + "Number of items", ChatColor.GRAY + "Lore", ChatColor.GRAY + "etc..."));
        item.setItemMeta(meta);
        int j = 0;
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                j += 6;
            }
            specialCraftingSlots.put(i + j + 4, item.clone());
        }
        meta.setDisplayName(ChatColor.BLUE + "Drag crafting result here");
        item.setItemMeta(meta);
        specialCraftingSlots.put(24, item.clone());

        addButtons(specialCraftingSlots);
    }

    public void createRefinerySlots() {
        ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Drag refinery requirements here");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "-------------", ChatColor.GRAY + "Includes:", ChatColor.GRAY + "Durability", ChatColor.GRAY + "Number of items", ChatColor.GRAY + "Lore", ChatColor.GRAY + "etc...", ChatColor.GRAY + "-------------"));
        item.setItemMeta(meta);
        refinerySlots.put(11, item.clone());

        meta.setDisplayName(ChatColor.BLUE + "Drag refinery catalyst here");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "-------------", ChatColor.GRAY + "This gets consumed with", ChatColor.GRAY + "the requirements. Can be empty", ChatColor.GRAY + "-------------"));
        item.setItemMeta(meta);
        refinerySlots.put(10, item.clone());

        meta.setDisplayName(ChatColor.BLUE + "Drag refinery fuel here");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "-------------", ChatColor.GRAY + "One fuel gets consumed per recipe.", ChatColor.GRAY + "Doesn't work like furnaces", ChatColor.GRAY + "-------------"));
        item.setItemMeta(meta);
        refinerySlots.put(29, item.clone());

        meta.setDisplayName(ChatColor.BLUE + "Drag refinery result here");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "-------------", ChatColor.GRAY + "Recipes can have a main output", ChatColor.GRAY + "and a byproduct. Byproduct", ChatColor.GRAY + "goes in the right-hand slot", ChatColor.GRAY + "-------------"));
        item.setItemMeta(meta);
        for (int i = 0; i < 2; i++) {
            refinerySlots.put(24 + i, item.clone());
        }

        item = new ItemStack(Material.CLOCK, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD.toString() + "10 seconds to refine");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to increase by 1 second", ChatColor.GRAY + "Right click to decrease by 1 second"));
        item.setItemMeta(meta);
        refinerySlots.put(22, item.clone());

        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setDisplayName(ChatColor.BLUE + "Mark I Refinery");
        meta.setLore(Arrays.asList("Click to make the recipe", "require a Mark II Refinery"));
        item.setItemMeta(meta);
        refinerySlots.put(8, item.clone());

        addButtons(refinerySlots);
    }

    public void addButtons(Map<Integer, ItemStack> slots) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Cancel custom recipe creation");
        item.setItemMeta(meta);
        slots.put(41, item.clone());

        item.setType(Material.LIME_STAINED_GLASS_PANE);
        meta.setDisplayName(ChatColor.GREEN + "Create custom recipe");
        item.setItemMeta(meta);
        slots.put(43, item.clone());
    }

    public void createRecipe(Inventory inv, String workstation, Player player) {
        ItemStack[] requirements;
        ItemStack[] result;
        if (workstation.equals("Special Crafting")) {
            requirements = new ItemStack[]{inv.getItem(10), inv.getItem(11), inv.getItem(12),
                                        inv.getItem(19), inv.getItem(20), inv.getItem(21),
                                        inv.getItem(28), inv.getItem(29), inv.getItem(30)};
            result = new ItemStack[]{inv.getItem(24)};
        } else {
            requirements = new ItemStack[]{inv.getItem(11), inv.getItem(10), inv.getItem(29)};
            result = new ItemStack[]{inv.getItem(24), inv.getItem(25)};
        }
        if (!isAir(result) && !isAir(requirements) && ((workstation.equals("Refinery") && requirements[2] != null && requirements[2].getType() != Material.AIR) || workstation.equals("Special Crafting"))) {
            Recipe recipe = new Recipe(workstation, requirements, result);
            if (workstation.equals("Refinery")) {
                recipe.fuel = inv.getItem(29);
                recipe.tier = 1;
                if (inv.getItem(8).getType() == Material.PURPLE_STAINED_GLASS_PANE) {
                    recipe.tier = 2;
                }
                ItemStack item = inv.getItem(22);
                ItemMeta meta = item.getItemMeta();
                recipe.refineTime = Integer.parseInt(meta.getDisplayName().replace(" seconds to refine", "").replace(ChatColor.GOLD.toString(), "")) * 1000;
            }
            recipes.add(recipe);
            main.craftingguicreator.recipeListGUI(player, (byte) 0, "");
        } else {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta meta = item.getItemMeta();
            if (isAir(result)) {
                meta.setDisplayName(ChatColor.RED + "The recipe needs a result");
                item.setItemMeta(meta);
                inv.setItem(24, item);
            }
            if (isAir(requirements)) {
                meta.setDisplayName(ChatColor.RED + "The recipe can't be air");
                item.setItemMeta(meta);
                inv.setItem(11, item);
            }
            if (workstation.equals("Refinery") && (requirements[2] == null) || requirements[2].getType() == Material.AIR) {
                meta.setDisplayName(ChatColor.RED + "The fuel can't be air");
                item.setItemMeta(meta);
                inv.setItem(29, item);
            }
        }
    }

    public boolean isAir(ItemStack[] items) {
        for (ItemStack item: items) {
            if (item != null) {
                if (item.getType() != Material.AIR) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkSearch() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : search.keySet()) {
                    if (search.get(player) != null) {
                        main.craftingguicreator.recipeListGUI(player, (byte) 0, search.get(player));
                        search.put(player, null);
                    }
                }
            }
        }, 1L, 5L);
    }
}
