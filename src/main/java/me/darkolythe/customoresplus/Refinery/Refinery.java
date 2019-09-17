package me.darkolythe.customoresplus.Refinery;

import me.darkolythe.customoresplus.Crafting.Recipe;
import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Refinery {

    public CustomOresPlus main;
    public Refinery(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public Integer[][] blocklocs = new Integer[3][3];
    public String world;
    public ItemStack[] refine = new ItemStack[2];
    public ItemStack fuel = null;
    public ItemStack[] output = new ItemStack[2];
    public int tier;
    public double lastRefined = System.currentTimeMillis();
    public Recipe recipe = null;

    /*
    This creates the blocks for the refinery and sets the tier type. Depending on the player rotation, the orientation of the refinery will change
     */
    public boolean createRefinery(Location location, byte rotation, int tier) {
        this.tier = tier;
        this.world = location.getWorld().getName();
        Short[][] order = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        Short[][] displace = {{0, 0}, {1, 0}, {1, -1}, {0, -1}};

        World world = location.getWorld();
        int x = (int)location.getX();
        int y = (int)location.getY();
        int z = (int)location.getZ();

        x += displace[rotation][0];
        z += displace[rotation][1];

        List<byte[]> blocks = new ArrayList<>();
        blocks.add(new byte[] {1, -1, 0, 0});
        blocks.add(new byte[] {2, -1, 1, 0});
        blocks.add(new byte[] {3, 0, 1, 0});

        Block[] bs = new Block[3];
        int index = 0;
        for (byte[] locs: blocks) {
            byte[] loc = locs.clone();
            for (int i = 0; i < rotation; i++) {
                loc[1] += order[(loc[0] + i) % 4][0];
                loc[2] += order[(loc[0] + i) % 4][1];
            }
            bs[index] = world.getBlockAt(x + loc[1], y + loc[3], z + loc[2]);
            if (bs[index].getType() != Material.AIR) {
                return false;
            }
            index += 1;
        }

        int i = 0;
        for (Block b: bs) {
            if (b != null) {
                b.setType(Material.BARRIER);
                this.blocklocs[i][0] = b.getX();
                this.blocklocs[i][1] = b.getY();
                this.blocklocs[i][2] = b.getZ();
                i += 1;
            }
        }

        main.refinerytools.drawRefinery((byte)tier, location.getWorld(), this, rotation);

        main.refinerymain.refineries.add(this);
        return true;
    }

    /*
    This creates the refinery inventory
     */
    public void openRefinery(Player player, String type) {
        Inventory inv;
        if (type.equals("Special Crafting")) {
            inv = Bukkit.getServer().createInventory(player, 45, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + type);
        } else {
            if (type.contains("1")) {
                inv = Bukkit.getServer().createInventory(player, 45, ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + type.replace("1", "I"));
            } else {
                inv = Bukkit.getServer().createInventory(player, 45, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + type.replace("2", "II"));
            }
        }

        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        item.setItemMeta(meta);
        for (int i = 0; i < 45; i++) {
            inv.setItem(i, item);
        }

        if (type.equals("Special Crafting")) {
            item = new ItemStack(Material.AIR);
            int j = 0;
            for (int i = 0; i < 9; i++) {
                if (i % 3 == 0) {
                    j += 6;
                }
                inv.setItem(i + j + 4, item);
            }
            inv.setItem(24, item);
            item = new ItemStack(Material.CRAFTING_TABLE, 1);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Attempt Crafting");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Left Click to craft one", ChatColor.GRAY + "Shift Left Click to craft as many as possible"));
            item.setItemMeta(meta);
            inv.setItem(23, item);
            item = new ItemStack(Material.ANVIL, 1);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_RED + "Click to access Refinery");
            item.setItemMeta(meta);
            inv.setItem(44, item);
        } else {
            for (Player players: main.refinerymain.openRefineries.keySet()) {
                if (players != player && main.refinerymain.isOpen.get(players)) {
                    if (this == main.refinerymain.openRefineries.get(players)) {
                        player.openInventory(players.getOpenInventory().getTopInventory());
                        return;
                    }
                }
            }
            inv.setItem(11, this.refine[0]);
            inv.setItem(10, this.refine[1]);
            inv.setItem(29, this.fuel);
            inv.setItem(24, this.output[0]);
            inv.setItem(25, this.output[1]);
            item = new ItemStack(Material.CLOCK, 1);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "0%");
            item.setItemMeta(meta);
            inv.setItem(22, item);
            item = new ItemStack(Material.CRAFTING_TABLE, 1);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Click to access Special Crafting");
            item.setItemMeta(meta);
            inv.setItem(44, item);
        }
        item = new ItemStack(Material.IRON_SWORD, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Click to access Item Repair");
        item.setItemMeta(meta);
        inv.setItem(43, item);

        player.openInventory(inv);
    }
}