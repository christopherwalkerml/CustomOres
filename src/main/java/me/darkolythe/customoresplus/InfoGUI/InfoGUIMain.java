package me.darkolythe.customoresplus.InfoGUI;

import me.darkolythe.customoresplus.CustomOresPlus;
import me.darkolythe.customoresplus.Generation.OreData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InfoGUIMain {

    public CustomOresPlus main;
    public InfoGUIMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public ItemStack giveInfoBook(Player player) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Core Compendium");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Right click to open"));
        book.setItemMeta(meta);
        if (player != null) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().setItem(player.getInventory().firstEmpty(), book);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), book);
            }
        }
        return book;
    }

    public Inventory openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Core Compendium");

        ItemStack edge = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = edge.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        edge.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0 || j == 2 || i == 0 || i == 8) {
                    inv.setItem((j * 9) + i, edge);
                }
            }
        }
        ItemStack item = new ItemStack(Material.CRAFTING_TABLE, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Custom Recipes");
        item.setItemMeta(meta);
        inv.setItem(10, item.clone());

        item.setType(Material.DIAMOND_ORE);
        meta.setDisplayName(ChatColor.AQUA + "Custom Ores");
        item.setItemMeta(meta);
        inv.setItem(12, item.clone());

        item.setType(Material.WHEAT_SEEDS);
        meta.setDisplayName(ChatColor.GREEN + "Custom Crops");
        meta.setLore(Arrays.asList(ChatColor.DARK_RED + "Coming Soon!"));
        item.setItemMeta(meta);
        inv.setItem(14, item.clone());

        item.setType(Material.SKELETON_SKULL);
        meta.setDisplayName(ChatColor.RED + "Custom Mobs");
        meta.setLore(Arrays.asList(ChatColor.DARK_RED + "Coming Soon!"));
        item.setItemMeta(meta);
        inv.setItem(16, item.clone());

        item.setType(Material.ENCHANTED_BOOK);
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Advancements");
        meta.setLore(null);
        item.setItemMeta(meta);
        inv.setItem(22, item.clone());

        if (player != null) {
            player.openInventory(inv);
        }
        return inv;
    }

    public Inventory openOresInfo(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Custom Ores");
        ItemStack edge = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = edge.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        edge.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                if (j == 0 || j == 5 || i == 0 || i == 8) {
                    inv.setItem((j * 9) + i, edge);
                }
            }
        }

        int index = 0;
        for (OreData ores: main.genmain.allOreData) {
            ItemStack ore = new ItemStack(main.generationtools.getSkullFromTexture(ores.id, ores.textures, ores.type, ores.colour));
            meta = ore.getItemMeta();
            meta.setLore(Arrays.asList(ChatColor.GRAY + "--------------------",
                                       ChatColor.GRAY + "required tool: " + main.genmain.tooltiers.get(ores.requiredtier),
                                       ChatColor.GRAY + "max y-level: " + ChatColor.BLUE + ores.ymax,
                                       ChatColor.GRAY + "min y-level: " + ChatColor.BLUE + ores.ymin,
                                       ChatColor.GRAY + "worlds: " + ChatColor.BLUE + ores.worlds.toString().replace("[", "").replace("]", ""),
                                       ChatColor.GRAY + "--------------------"));
            ore.setItemMeta(meta);

            index += 1;
            while (inv.getItem(index) != null && inv.getItem(index).getType() == Material.GRAY_STAINED_GLASS_PANE) {
                index += 1;
            }
            inv.setItem(index, ore.clone());
        }

        ItemStack item = new ItemStack(Material.BARRIER, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Back to main page");
        item.setItemMeta(meta);
        inv.setItem(49, item.clone());

        if (player != null) {
            player.openInventory(inv);
        }
        return inv;
    }
}
