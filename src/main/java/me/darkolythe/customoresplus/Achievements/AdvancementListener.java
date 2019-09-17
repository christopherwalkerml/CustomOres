package me.darkolythe.customoresplus.Achievements;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class AdvancementListener implements Listener {

    public CustomOresPlus main;
    public AdvancementListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String title = player.getOpenInventory().getTitle();
            Inventory inv = event.getClickedInventory();
            ItemStack item = event.getCurrentItem();
            if (title.contains(ChatColor.LIGHT_PURPLE.toString()) && title.contains("Advancements")) {
                event.setCancelled(true);
                if (item != null && event.getRawSlot() >= 10 && event.getRawSlot() <= 16) {
                    main.advancementmain.openAdvancementPage(player, item.getItemMeta().getDisplayName(), false);
                }
            } else if (title.contains(ChatColor.LIGHT_PURPLE.toString()) && title.contains("advancements")) {
                event.setCancelled(true);
                if (event.getRawSlot() == 53) {
                    if (item != null && item.getType() != Material.AIR) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.getLore().get(0).equals(ChatColor.GRAY + "Currently: " + ChatColor.GREEN + "not editing")) {
                            main.advancementmain.openAdvancementPage(player, title, true);
                        } else {
                            main.advancementmain.openAdvancementPage(player, title, false);
                        }
                    }
                } else {
                    if (inv != player.getInventory()) {
                        if (inv != null && inv.getItem(53) != null && inv.getItem(53).getType() != Material.AIR) {
                            if (!inv.getItem(53).getItemMeta().getLore().get(0).contains("not editing")) {
                                int index = getPage(title);
                                AdvancementData a = main.advancementmain.advancements[(index * 54) + event.getRawSlot()];
                                if (a != null) {
                                    main.advancementeditor.editorInventory(player, a.icon, a.name, a.parent, a.description, a.hidden, a.requiredItem, event.getRawSlot(), title);
                                } else {
                                    main.advancementeditor.editorInventory(player, null, null, null, null, false, null, event.getRawSlot(), title);
                                }
                            }
                        }
                    }
                }
            } else if (player.getOpenInventory().getTitle().equals(ChatColor.RED + "Edit Advancement")) {
                if (inv != player.getInventory() && inv != null) {
                    event.setCancelled(true);
                    int index = Integer.parseInt(inv.getItem(4).getItemMeta().getLore().get(0).replace(ChatColor.GRAY.toString(), ""));
                    int page = getPage(inv.getItem(4).getItemMeta().getDisplayName());
                    switch (event.getRawSlot()) {
                        case 10:
                            event.setCancelled(false);
                            event.setCurrentItem(null);
                            break;
                        case 11:
                            main.advancementmain.catchChat.put(player, "name");
                            main.advancementmain.saveInv.put(player, inv);
                            player.sendMessage(main.prefix + ChatColor.GRAY + "Enter a name for the advancement or 'cancel'");
                            player.closeInventory();
                            break;
                        case 12:
                            main.advancementmain.catchChat.put(player, "parent");
                            main.advancementmain.saveInv.put(player, inv);
                            player.sendMessage(main.prefix + ChatColor.GRAY + "Enter the parent's name for the advancement or 'cancel'");
                            player.closeInventory();
                            break;
                        case 13:
                            main.advancementmain.catchChat.put(player, "description");
                            main.advancementmain.saveInv.put(player, inv);
                            player.sendMessage(main.prefix + ChatColor.GRAY + "Enter a description for the advancement or 'cancel'");
                            player.closeInventory();
                            break;
                        case 14:
                            item = event.getCurrentItem();
                            ItemMeta meta = item.getItemMeta();
                            if (meta.getLore().get(0).contains("true")) {
                                meta.setLore(Arrays.asList(ChatColor.GRAY + "Is Hidden: " + ChatColor.BLUE + "false"));
                            } else {
                                meta.setLore(Arrays.asList(ChatColor.GRAY + "Is Hidden: " + ChatColor.BLUE + "true"));
                            }
                            item.setItemMeta(meta);
                            break;
                        case 15:
                            event.setCancelled(false);
                            event.setCurrentItem(null);
                            break;
                        case 16:
                            if (event.isRightClick()) {
                                main.advancementmain.advancements[index] = null;
                                main.advancementmain.openAdvancementPage(player, inv.getItem(4).getItemMeta().getDisplayName(), true);
                            } else if (event.isLeftClick()) {
                                main.advancementmain.openAdvancementPage(player, inv.getItem(4).getItemMeta().getDisplayName(), true);
                            }
                            break;
                        case 22:
                            AdvancementData ad = new AdvancementData();
                            ad.index = index;
                            item = inv.getItem(10);
                            if (item != null && item.getType() != Material.AIR) {
                                item.setAmount(1);
                                ad.icon = item;
                                ad.name = main.advancementmain.returnChatColor(inv.getItem(11).getItemMeta().getDisplayName());
                                ad.parent = main.advancementmain.returnChatColor(inv.getItem(12).getItemMeta().getDisplayName());
                                ad.description = main.advancementmain.returnChatColor(inv.getItem(13).getItemMeta().getDisplayName());
                                ad.hidden = Boolean.parseBoolean(inv.getItem(14).getItemMeta().getLore().get(0).replace(ChatColor.GRAY + "Is Hidden: " + ChatColor.BLUE, ""));
                                ad.requiredItem = inv.getItem(15);
                                ad.pool = inv.getItem(4).getItemMeta().getDisplayName();
                                main.advancementmain.advancements[(page * 54) + index] = ad;
                                main.advancementmain.openAdvancementPage(player, inv.getItem(4).getItemMeta().getDisplayName(), true);
                            }
                            break;
                        default:
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (main.advancementmain.catchChat.containsKey(player)) {
            event.setCancelled(true);
            String message = event.getMessage();
            main.advancementmain.caughtChat.put(player, main.advancementmain.returnChatColor(message));
        }
    }

    @EventHandler
    public void onPlayerjoin(PlayerJoinEvent event) {
        main.advancementconfighandler.getPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        main.advancementconfighandler.savePlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onItemAddToInventory(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            checkAdvancement(player, event.getCurrentItem());
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            checkAdvancement(player, event.getItem().getItemStack());
        }
    }

    public void checkAdvancement(Player player, ItemStack item) {
        for (AdvancementData a: main.advancementmain.advancements) {
            if (a != null) {
                if (main.refinerytools.itemstackEquals(a.requiredItem, item)) {
                    if (!main.advancementmain.playerAdvancements.containsKey(player.getUniqueId())) {
                        main.advancementmain.playerAdvancements.put(player.getUniqueId(), new ArrayList<>());
                    }
                    if (!main.advancementmain.playerAdvancements.get(player.getUniqueId()).contains(a.name)) {
                        if (main.advancementmain.playerAdvancements.get(player.getUniqueId()).contains(a.parent) || a.parent.equals(ChatColor.WHITE + "null")) {
                            completeAdvancement(a, player);
                        }
                    }
                }
            }
        }
    }

    public void completeAdvancement(AdvancementData a, Player player) {
        if (!main.advancementmain.playerAdvancements.containsKey(player.getUniqueId())) {
            main.advancementmain.playerAdvancements.put(player.getUniqueId(), new ArrayList<>());
        }
        main.advancementmain.playerAdvancements.get(player.getUniqueId()).add(a.name);
        if (main.advancementmain.broadcastAchievements) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.getServer().dispatchCommand(console, "tellraw @a " + "[\"\",{\"text\":\"" + player.getName() + " has made the advancement \"},{\"text\":\"" + a.name + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + a.description + "\"}}]");
            System.out.println(player.getName() + " has made the advancement " + a.name);
        }
    }

    public int getPage(String title) {
        int index;
        if (title.contains("Collection")) {
            index = 0;
        } else if (title.contains("Levelling")) {
            index = 1;
        } else {
            index = 2;
        }
        return index;
    }
}
