package me.darkolythe.customoresplus.InfoGUI;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InfoGUIListener implements Listener {

    public CustomOresPlus main;
    public InfoGUIListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (main.infoguimain.giveInfoBook(null).equals(item)) {
                main.infoguimain.openGUI(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if (event.getClickedInventory() != player.getInventory()) {
                if (player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Custom") || player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE + "Advancements") ||
                        (player.getOpenInventory().getTitle().contains(ChatColor.LIGHT_PURPLE.toString()) && player.getOpenInventory().getTitle().contains("advancements"))) {
                    if (item != null && item.getType() == Material.BARRIER) {
                        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Back to main page")) {
                            event.setCancelled(true);
                            player.openInventory(main.infoguimain.openGUI(player));
                        }
                    }
                }
            }
            if (player.getOpenInventory().getTitle().equals(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Custom Ores")) {
                event.setCancelled(true);
            } else if (player.getOpenInventory().getTitle().equals(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Core Compendium")) {
                event.setCancelled(true);
                if (item != null && event.getClickedInventory() != player.getInventory()) {
                    if (item.getType() == Material.CRAFTING_TABLE) {
                        main.craftingguicreator.recipeListGUI(player, (byte) 0, "");
                    } else if (item.getType() == Material.DIAMOND_ORE) {
                        main.infoguimain.openOresInfo(player);
                    } else if (item.getType() == Material.ENCHANTED_BOOK) {
                        main.advancementmain.openAdvancements(player);
                    }
                }
            }
        }
    }
}
