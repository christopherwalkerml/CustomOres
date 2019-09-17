package me.darkolythe.customoresplus.Repair;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class RepairListener implements Listener {

    public CustomOresPlus main;
    public RepairListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() != player.getInventory()) {
                Inventory inv = event.getClickedInventory();
                if (inv != null) {
                    if (player.getOpenInventory().getTitle().equals(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Repair Tools")) {
                        int slot = event.getRawSlot();
                        if (slot == 1 || slot == 3 || slot == 4) {
                            event.setCancelled(true);
                        }
                        if (slot == 1 && inv.getItem(2).getType() == Material.RED_STAINED_GLASS_PANE) {
                            ItemStack i = inv.getItem(0);
                            ItemMeta m = i.getItemMeta();
                            if (m != null) {
                                event.setCancelled(true);
                                boolean doSwitch = false;
                                if (((Damageable) m).getDamage() > 0) {
                                    if (event.isShiftClick()) {
                                        if (player.getLevel() >= 10) {
                                            doSwitch = true;
                                            ((Damageable) m).setDamage(((Damageable) m).getDamage() - 100);
                                            player.setLevel(player.getLevel() - 10);
                                        }
                                    } else {
                                        if (player.getLevel() >= 1) {
                                            doSwitch = true;
                                            ((Damageable) m).setDamage(((Damageable) m).getDamage() - 10);
                                            player.setLevel(player.getLevel() - 1);
                                            System.out.println(player.getTotalExperience());
                                        }
                                    }
                                }
                                if (doSwitch) {
                                    i.setItemMeta(m);
                                    inv.setItem(2, i);
                                    ItemStack it = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
                                    ItemMeta meta = it.getItemMeta();
                                    meta.setDisplayName(ChatColor.GREEN + "Place item to repair here");
                                    it.setItemMeta(meta);
                                    inv.setItem(0, it.clone());
                                }
                            }
                        }
                        if (slot == 3) {
                            event.setCancelled(true);
                            main.refinerymain.openRefineries.get(player).openRefinery(player, "Refinery mark " + main.refinerymain.openRefineries.get(player).tier);
                        } else if (slot == 4) {
                            event.setCancelled(true);
                            main.refinerymain.openRefineries.get(player).openRefinery(player, "Special Crafting");
                        }
                        if (slot == 0) {
                            if (inv.getItem(0).getType() == Material.GREEN_STAINED_GLASS_PANE) {
                                event.setCancelled(true);
                            }
                            if (player.getItemOnCursor().getType() != Material.AIR) {
                                inv.setItem(0, player.getItemOnCursor());
                                player.setItemOnCursor(null);
                            } else {
                                player.setItemOnCursor(inv.getItem(0));
                                ItemStack i = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
                                ItemMeta meta = i.getItemMeta();
                                meta.setDisplayName(ChatColor.GREEN + "Place item to repair here");
                                i.setItemMeta(meta);
                                inv.setItem(0, i.clone());
                                event.setCancelled(true);
                            }
                        }
                        if (slot == 2) {
                            if (player.getItemOnCursor().getType() != Material.AIR || inv.getItem(2).getType() == Material.RED_STAINED_GLASS_PANE) {
                                event.setCancelled(true);
                            } else {
                                player.setItemOnCursor(inv.getItem(2));
                                ItemStack i = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                                ItemMeta meta = i.getItemMeta();
                                meta.setDisplayName(ChatColor.RED + "Result will appear here");
                                i.setItemMeta(meta);
                                inv.setItem(2, i.clone());
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
