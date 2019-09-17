package me.darkolythe.customoresplus.Refinery;

import me.darkolythe.customoresplus.Crafting.Recipe;
import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RefineryMain {

    CustomOresPlus main;
    public RefineryMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public ItemStack refineryT1 = new ItemStack(Material.ANVIL, 1);
    public ItemStack refineryT2 = new ItemStack(Material.ANVIL, 1);
    public List<Refinery> refineries = new ArrayList<>();
    public Map<Player, Refinery> openRefineries = new HashMap<>();
    public Map<Player, Recipe> currentRecipe = new HashMap<>();
    public Map<Player, Boolean> isOpen = new HashMap<>();

    public void setUp() {
        ItemMeta refinerymeta = refineryT1.getItemMeta();
        refinerymeta.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Refinery");
        refinerymeta.setLore(Arrays.asList(ChatColor.GRAY + "---------------", ChatColor.GRAY + "Tier: " + ChatColor.BLUE + "Mark I", ChatColor.GRAY + "Size: 2x1x2",
                                           ChatColor.GRAY + "---------------"));
        refineryT1.setItemMeta(refinerymeta);
        refinerymeta = refineryT2.getItemMeta();
        refinerymeta.setDisplayName(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "Refinery");
        refinerymeta.setLore(Arrays.asList(ChatColor.GRAY + "---------------", ChatColor.GRAY + "Tier: " + ChatColor.DARK_PURPLE + "Mark II", ChatColor.GRAY + "Size: 2x1x2",
                                           ChatColor.GRAY + "---------------"));
        refineryT2.setItemMeta(refinerymeta);
    }

    /*
    This updates the refining progress in refineries that are currently open
     */
    public void refineItemCheck() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player: openRefineries.keySet()) {
                    if (isOpen.containsKey(player)) {
                        if (isOpen.get(player)) {
                            if (openRefineries.get(player) != null) {
                                main.refinerylistener.checkRefineryRecipe(openRefineries.get(player), player);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                                    @Override
                                    public void run() {
                                        player.updateInventory();
                                    }
                                }, 1);
                            }
                        }
                    }
                }
            }
        }, 0L, 10L);
    }
}
