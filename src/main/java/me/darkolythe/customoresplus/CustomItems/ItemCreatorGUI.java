package me.darkolythe.customoresplus.CustomItems;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Comparator;

public class ItemCreatorGUI {

    public CustomOresPlus main;
    public ItemCreatorGUI(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public void getGUI(Player player, ItemStack pitem) {
        if (pitem.getType() != Material.AIR) {
            Inventory gui = Bukkit.getServer().createInventory(player, 27, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI");
            ItemStack item = new ItemStack(Material.GLASS_BOTTLE, 1);
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Status Effects");
            itemmeta.setLore(Arrays.asList(ChatColor.GREEN + "Click to edit the item's status effects", "", ChatColor.GRAY + "Some examples:",
                    ChatColor.GRAY + "Jump Boost", ChatColor.GRAY + "Haste", ChatColor.GRAY + "Health", ChatColor.GRAY + "Flight"));
            item.setItemMeta(itemmeta);
            gui.setItem(6, item);

            item = new ItemStack(Material.DIAMOND, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.BLUE + "Special Modifiers");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's modifiers", "", ChatColor.GRAY + "Some examples:",
                    ChatColor.GRAY + "Armour Strength", ChatColor.GRAY + "Durability", ChatColor.GRAY + "Attack Damage"));
            item.setItemMeta(itemmeta);
            gui.setItem(5, item);

            item = new ItemStack(Material.BOOK, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.BLUE + "Item Lore");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's lore", ChatColor.GRAY + "Right click to remove the item's lore"));
            item.setItemMeta(itemmeta);
            gui.setItem(4, item);

            item = new ItemStack(Material.NAME_TAG, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.GOLD + "Item Name");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's name", ChatColor.GRAY + "Right click to remove the item's name"));
            item.setItemMeta(itemmeta);
            gui.setItem(3, item);

            item = new ItemStack(Material.ENCHANTED_BOOK, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Item Enchantments");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's enchantments", ChatColor.GRAY + "Right click to remove all enchantments"));
            item.setItemMeta(itemmeta);
            gui.setItem(2, item);

            gui.setItem(22, pitem);
            
            player.openInventory(gui);
        } else {
            player.sendMessage(main.prefix + ChatColor.RED + "The item to customize must be in your main hand!");
        }
    }

    public void enchantGUI(Player player, ItemStack item) {
        Inventory gui = Bukkit.getServer().createInventory(player, ((Enchantment.values().length / 9) + 2) * 9, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Enchantment GUI");
        int index = 0;
        gui.setItem((((Enchantment.values().length / 9) + 2) * 9) - 5, item);
        Enchantment[] enchants = Enchantment.values();
        Arrays.sort(enchants, Comparator.comparing(Enchantment::toString));
        for (Enchantment enchant: enchants) {
            ItemStack i = new ItemStack(Material.BOOK, 1);
            ItemMeta meta = i.getItemMeta();
            meta.setLore(Arrays.asList(ChatColor.GOLD + WordUtils.capitalize(enchant.toString().split(",")[0].replace("Enchantment[minecraft:", "").replace("_", " ")),
                    "", ChatColor.GRAY + "Left click to increase by 1", ChatColor.GRAY + "Right click to decrease by 1"));
            i.setItemMeta(meta);
            if (item.containsEnchantment(enchant)) {
                i.addUnsafeEnchantment(enchant, item.getEnchantmentLevel(enchant));
                i.setAmount(item.getEnchantmentLevel(enchant));
            }
            gui.setItem(index, i);
            index += 1;
        }
        ItemStack i = new ItemStack(Material.BARRIER, 1);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to main menu");
        i.setItemMeta(m);
        gui.setItem((((Enchantment.values().length / 9) + 2) * 9) - 1, i);
        player.openInventory(gui);
    }

    public void modifiersGUI(Player player, ItemStack item) {
        Inventory gui = Bukkit.getServer().createInventory(player, 27, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Special Modifiers GUI");
        ItemStack i = new ItemStack(Material.IRON_SWORD, 1, (short) 150);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit item durability");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to decrease durability by 1", ChatColor.GRAY + "Shift left click to decrease durability by 10",
                                    ChatColor.GRAY + "Right click to increase durability by 1", ChatColor.GRAY + "Shift right click to increase durability by 10"));
        i.setItemMeta(m);
        gui.setItem(0, i.clone());

        i = new ItemStack(Material.BEDROCK, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Set unbreakable");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to make item unbreakable", ChatColor.GRAY + "Right click to make item breakable"));
        i.setItemMeta(m);
        gui.setItem(1, i.clone());

        i = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit armour strength");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase armour by 1", ChatColor.GRAY + "Right click to decrease armour by 1"));
        i.setItemMeta(m);
        gui.setItem(2, i.clone());

        i = new ItemStack(Material.LEATHER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit armour toughness");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase toughness by 1", ChatColor.GRAY + "Right click to decrease toughness by 1"));
        i.setItemMeta(m);
        gui.setItem(3, i.clone());

        i = new ItemStack(Material.DIAMOND_SWORD, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit attack damage");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase damage by 1", ChatColor.GRAY + "Right click to decrease damage by 1"));
        i.setItemMeta(m);
        gui.setItem(4, i.clone());

        i = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit health");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase health by 1", ChatColor.GRAY + "Right click to decrease health by 1"));
        i.setItemMeta(m);
        gui.setItem(5, i.clone());

        i = new ItemStack(Material.FEATHER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit speed");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase speed by 0.1", ChatColor.GRAY + "Right click to decrease speed by 0.1"));
        i.setItemMeta(m);
        gui.setItem(6, i.clone());

        i = new ItemStack(Material.TRIDENT, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit attack speed");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase attack speed by 0.1", ChatColor.GRAY + "Right click to decrease attack speed by 0.1"));
        i.setItemMeta(m);
        gui.setItem(7, i.clone());

        i = new ItemStack(Material.OBSIDIAN, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit knockback resistance");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase knockback resistance by 1", ChatColor.GRAY + "Right click to decrease knockback resistance by 1"));
        i.setItemMeta(m);
        gui.setItem(8, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide attribute tags");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide attribute tags", ChatColor.GRAY + "Right click to show attribute tags"));
        i.setItemMeta(m);
        gui.setItem(11, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide Enchantments tag");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide enchantment tags", ChatColor.GRAY + "Right click to show enchantment tags"));
        i.setItemMeta(m);
        gui.setItem(12, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide Unbreaking tag");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide unbreaking tags", ChatColor.GRAY + "Right click to show unbreaking tags"));
        i.setItemMeta(m);
        gui.setItem(14, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide Potion effects tag");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide potion effects tags", ChatColor.GRAY + "Right click to show potion effects tags"));
        i.setItemMeta(m);
        gui.setItem(15, i.clone());

        gui.setItem(22, item);

        i = new ItemStack(Material.BARRIER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to main menu");
        i.setItemMeta(m);
        gui.setItem(26, i);

        player.openInventory(gui);
    }

    public void potionEffectsGUI(Player player, ItemStack item) {
        Inventory gui = Bukkit.getServer().createInventory(player, 36, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI");
        int index = 0;
        for (String effect: main.itemmain.effects) {
            ItemStack i = new ItemStack(Material.GLASS_BOTTLE, 1);
            ItemMeta m = i.getItemMeta();
            m.setLore(Arrays.asList(ChatColor.GOLD + effect, "", ChatColor.GRAY + "Left click to increase by 1", ChatColor.GRAY + "Right click to decrease by 1"));
            i.setItemMeta(m);
            String enchant = m.getLore().get(0).replace(ChatColor.GOLD.toString(), ChatColor.GRAY.toString());
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasLore()) {
                    for (String l : item.getItemMeta().getLore()) {
                        if (l.contains(enchant)) {
                            i.setAmount(Integer.parseInt(l.replace(enchant + " ", "")));
                            i.setType(Material.POTION);
                        }
                    }
                }
            }
            gui.setItem(index, i);
            index += 1;
        }
        ItemStack i = new ItemStack(Material.FEATHER, 1);
        ItemMeta m = i.getItemMeta();
        m.setLore(Arrays.asList(ChatColor.GOLD + "Flight", "", ChatColor.GRAY + "Left click to add flight / flight speed", ChatColor.GRAY + "Right click to remove flight / flight speed"));
        i.setItemMeta(m);
        gui.setItem(index, i);

        gui.setItem(31, item);

        i = new ItemStack(Material.BARRIER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to main menu");
        i.setItemMeta(m);
        gui.setItem(35, i);

        player.openInventory(gui);
    }
}

