package me.darkolythe.customoresplus.Achievements;

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

import java.util.*;

public class AdvancementMain {

    public CustomOresPlus main;
    public AdvancementMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public Map<Player, String> catchChat = new HashMap<>();
    public Map<Player, String> caughtChat = new HashMap<>();
    public Map<Player, Inventory> saveInv = new HashMap<>();
    public Map<UUID, List<String>> playerAdvancements = new HashMap<>();
    public AdvancementData[] advancements = new AdvancementData[162];
    public boolean broadcastAchievements = true;

    public void openAdvancements(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.LIGHT_PURPLE + "Advancements");

        ItemStack edge = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0 || j == 2 || i == 0 || i == 8) {
                    inv.setItem((j * 9) + i, edge);
                }
            }
        }

        OreData ore = main.genmain.getOreData((byte)5);
        ItemStack item = main.generationtools.getSkullFromTexture(ore.id, ore.textures, ore.type, ore.colour);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Collection advancements");
        item.setItemMeta(meta);
        inv.setItem(11, item.clone());

        item.setType(Material.COMPASS);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Levelling advancements");
        meta.setLore(Arrays.asList(ChatColor.DARK_RED + "Coming Soon!"));
        item.setItemMeta(meta);
        inv.setItem(13, item.clone());

        item.setType(Material.SKELETON_SKULL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Hunting advancements");
        meta.setLore(Arrays.asList(ChatColor.DARK_RED + "Coming Soon!"));
        item.setItemMeta(meta);
        inv.setItem(15, item.clone());

        item = new ItemStack(Material.BARRIER, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Back to main page");
        item.setItemMeta(meta);
        inv.setItem(22, item.clone());

        player.openInventory(inv);
    }

    public void openAdvancementPage(Player player, String page, boolean editing) {
        Inventory inv = Bukkit.createInventory(player, 54, page);

        if (player.hasPermission("customoresplus.advancementadmin")) {
            ItemStack item = new ItemStack(Material.GREEN_CONCRETE, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "Toggle edit mode");
            if (editing) {
                item.setType(Material.RED_CONCRETE);
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Currently: " + ChatColor.RED + "editing"));
            } else {
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Currently: " + ChatColor.GREEN + "not editing"));
            }
            item.setItemMeta(meta);
            inv.setItem(53, item.clone());

            item = new ItemStack(Material.BARRIER, 1);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_RED + "Back to main page");
            item.setItemMeta(meta);
            inv.setItem(49, item.clone());
        }

        for (AdvancementData a: advancements) {
            if (a != null) {
                if (a.pool.equals(page)) {
                    if ((a.parent.equals(ChatColor.WHITE + "null") && !a.hidden) ||
                            ((playerAdvancements.containsKey(player.getUniqueId()) && playerAdvancements.get(player.getUniqueId()).contains(a.parent)) && !a.hidden) ||
                            (editing) || (playerAdvancements.containsKey(player.getUniqueId()) && playerAdvancements.get(player.getUniqueId()).contains(a.name))) {
                        ItemStack item = a.icon.clone();
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(a.name);
                        meta.setLore(Arrays.asList(a.description));
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        item.setItemMeta(meta);
                        if (playerAdvancements.containsKey(player.getUniqueId())) {
                            if (playerAdvancements.get(player.getUniqueId()).contains(a.name)) {
                                meta = item.getItemMeta();
                                meta.setLore(Arrays.asList(meta.getLore().get(0), ChatColor.GREEN + "Completed"));
                                item.setItemMeta(meta);
                                item.setAmount(10);
                            }
                        }
                        inv.setItem(a.index, item);
                    }
                }
            }
        }
        
        player.openInventory(inv);
    }

    public void addText() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                Iterator<Player> i = caughtChat.keySet().iterator();
                Player player;
                while (i.hasNext()) {
                    player = i.next();
                    ItemStack item;
                    if (catchChat.get(player).equals("name")) {
                        item = saveInv.get(player).getItem(11);
                    } else if (catchChat.get(player).equals("parent")) {
                        item = saveInv.get(player).getItem(12);
                    } else {
                        item = saveInv.get(player).getItem(13);
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(caughtChat.get(player));
                    item.setItemMeta(meta);
                    player.openInventory(saveInv.get(player));
                    i.remove();
                    catchChat.remove(player);
                }
            }
        }, 1L, 5L);
    }

    public String returnChatColor(String text) {
        for (ChatColor values: ChatColor.values()) {
            if (text.contains(values.toString())) {
                return text;
            }
        }
        return ChatColor.WHITE + text;
    }
}
