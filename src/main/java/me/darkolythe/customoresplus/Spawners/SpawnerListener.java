package me.darkolythe.customoresplus.Spawners;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SpawnerListener implements Listener {

    public CustomOresPlus main;
    public SpawnerListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.SPAWNER) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                ItemStack handstack = player.getInventory().getItemInMainHand();
                if (handstack.getType().toString().contains("PICKAXE")) {
                    for (Enchantment enchants : handstack.getEnchantments().keySet()) {
                        if (enchants.equals(Enchantment.SILK_TOUCH)) {
                            ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
                            ItemMeta smeta = spawner.getItemMeta();
                            smeta.setDisplayName(ChatColor.YELLOW + "Empty Spawner");
                            smeta.setLore(Arrays.asList(ChatColor.GRAY + "Cannot be placed", ChatColor.GRAY + "Can be used in spawner crafting"));
                            spawner.setItemMeta(smeta);
                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawner);
                            return;
                        }
                    }
                    ItemStack spawner = new ItemStack(Material.IRON_BARS, 1);
                    ItemMeta smeta = spawner.getItemMeta();
                    smeta.setDisplayName(ChatColor.RED + "Broken Spawner");
                    smeta.setLore(Arrays.asList(ChatColor.GRAY + "Cannot be placed", ChatColor.GRAY + "Can be used to make empty spawners"));
                    spawner.setItemMeta(smeta);
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawner);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.getDisplayName().equals(ChatColor.YELLOW + "Empty Spawner") || meta.getDisplayName().equals(ChatColor.RED + "Broken Spawner")) {
                event.setCancelled(true);
                return;
            }
            if (item.getType() == Material.SPAWNER) {
                if (meta.hasLore()) {
                    for (String l : meta.getLore()) {
                        if (l.contains(ChatColor.GRAY + "Spawner Type: " + ChatColor.GREEN)) {
                            Block block = event.getBlock();
                            CreatureSpawner spawner = (CreatureSpawner) block.getState();
                            spawner.setSpawnedType(EntityType.valueOf(l.replace(ChatColor.GRAY + "Spawner Type: " + ChatColor.GREEN, "")));
                            spawner.update();
                            return;
                        }
                    }
                }
            }
        }
    }
}
