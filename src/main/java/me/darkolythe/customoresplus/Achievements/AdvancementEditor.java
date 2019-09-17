package me.darkolythe.customoresplus.Achievements;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AdvancementEditor {

    public CustomOresPlus main;
    public AdvancementEditor(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public void editorInventory(Player player, ItemStack icon, String name, String parent, String desc, boolean hidden, ItemStack requiredItem, int index, String pool) {
        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.RED + "Edit Advancement");

        ItemStack edge = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta edgemeta = edge.getItemMeta();
        edgemeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0 || j == 2 || i == 0 || i == 8) {
                    inv.setItem((j * 9) + i, edge);
                }
            }
        }

        edge = inv.getItem(4);
        edgemeta.setDisplayName(pool);
        edgemeta.setLore(Arrays.asList(ChatColor.GRAY.toString() + index));
        edge.setItemMeta(edgemeta);

        if (icon != null) {
            inv.setItem(10, icon);

            ItemStack item = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            inv.setItem(11, item.clone());

            item.setType(Material.PAPER);
            meta.setDisplayName(parent);
            item.setItemMeta(meta);
            inv.setItem(12, item.clone());

            meta.setDisplayName(desc);
            item.setItemMeta(meta);
            inv.setItem(13, item.clone());

            item.setType(Material.GHAST_TEAR);
            item.setType(Material.GHAST_TEAR);
            meta.setDisplayName(ChatColor.WHITE + "Toggle Hidden");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Is Hidden: " + ChatColor.BLUE + hidden));
            item.setItemMeta(meta);
            inv.setItem(14, item.clone());

            inv.setItem(15, requiredItem);

            item.setType(Material.BARRIER);
            meta.setDisplayName(ChatColor.DARK_RED + "Cancel edit");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to cancel edit", ChatColor.GRAY + "Right click to delete advancement"));
            item.setItemMeta(meta);
            inv.setItem(16, item.clone());
        } else {
            ItemStack item = new ItemStack(Material.GRASS_BLOCK, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Put Icon Here");
            item.setItemMeta(meta);
            inv.setItem(10, item.clone());

            item.setType(Material.NAME_TAG);
            meta.setDisplayName(ChatColor.WHITE + "Edit Advancement Name");
            item.setItemMeta(meta);
            inv.setItem(11, item.clone());

            item.setType(Material.PAPER);
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Edit Parent Name");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "type 'null' for no parent"));
            item.setItemMeta(meta);
            inv.setItem(12, item.clone());

            item.setType(Material.PAPER);
            meta.setDisplayName(ChatColor.GRAY + "Edit Description");
            meta.setLore(null);
            item.setItemMeta(meta);
            inv.setItem(13, item.clone());

            item.setType(Material.GHAST_TEAR);
            meta.setDisplayName(ChatColor.WHITE + "Toggle Hidden");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Is Hidden: " + ChatColor.BLUE + "false"));
            item.setItemMeta(meta);
            inv.setItem(14, item.clone());

            item.setType(Material.GRASS_BLOCK);
            meta.setDisplayName(ChatColor.GREEN + "Required Item");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Can be left empty for no item"));
            item.setItemMeta(meta);
            inv.setItem(15, item.clone());

            item.setType(Material.BARRIER);
            meta.setDisplayName(ChatColor.DARK_RED + "Cancel edit");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to cancel edit", ChatColor.GRAY + "Right click to delete advancement"));
            item.setItemMeta(meta);
            inv.setItem(16, item.clone());
        }

        ItemStack item = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Confirm edit");
        meta.setLore(null);
        item.setItemMeta(meta);
        inv.setItem(22, item.clone());

        player.openInventory(inv);
    }
}
