package me.darkolythe.customoresplus.Repair;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RepairMain {

    public CustomOresPlus main;
    public RepairMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public void createRepairInventory(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Repair Tools");

        ItemStack item = new ItemStack(Material.ANVIL, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Repair Item");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Requires 1 level to repair 10 durability", ChatColor.GRAY + "Shift click to repair 100 durability for 10 levels"));
        item.setItemMeta(meta);
        inv.setItem(1, item.clone());

        item.setType(Material.GREEN_STAINED_GLASS_PANE);
        meta.setDisplayName(ChatColor.GREEN + "Place item to repair here");
        item.setItemMeta(meta);
        inv.setItem(0, item.clone());

        item.setType(Material.RED_STAINED_GLASS_PANE);
        meta.setDisplayName(ChatColor.RED + "Result will appear here");
        meta.setLore(null);
        item.setItemMeta(meta);
        inv.setItem(2, item.clone());

        item = new ItemStack(Material.ANVIL, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Click to access Refinery");
        item.setItemMeta(meta);
        inv.setItem(3, item.clone());

        item = new ItemStack(Material.CRAFTING_TABLE, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Click to access Special Crafting");
        item.setItemMeta(meta);
        inv.setItem(4, item.clone());

        player.openInventory(inv);
    }

}
