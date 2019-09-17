package me.darkolythe.customoresplus.Refinery;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.darkolythe.customoresplus.Crafting.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RefineryListener implements Listener {

    public CustomOresPlus main;
    public RefineryListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            ItemStack item = event.getItemInHand().clone();
            item.setAmount(1);
            double x = player.getLocation().getYaw();
            if (x < 0) {
                x += 360;
            }
            byte rotation;
            if (x >= 270 && x < 360) {
                rotation = 1;
            } else if (x >= 180 && x < 270) {
                rotation = 2;
            } else if (x >= 90 && x < 180) {
                rotation = 3;
            } else {
                rotation = 0;
            }
            if (item.equals(main.refinerymain.refineryT1)) {
                Refinery refinery = new Refinery(main);
                if (!refinery.createRefinery(event.getBlockPlaced().getLocation(), rotation, 1)) {
                    player.sendMessage(main.prefix + ChatColor.RED + "There's not enough space here for a refinery!");
                    event.setCancelled(true);
                } else {
                    event.getBlockPlaced().setType(Material.AIR);
                }
            } else if (item.equals(main.refinerymain.refineryT2)) {
                Refinery refinery = new Refinery(main);
                if (!refinery.createRefinery(event.getBlockPlaced().getLocation(), rotation, 2)) {
                    player.sendMessage(main.prefix + ChatColor.RED + "There's not enough space here for a refinery!");
                    event.setCancelled(true);
                } else {
                    event.getBlockPlaced().setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().isSneaking()) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = event.getClickedBlock();
                    if (block.getType() == Material.BARRIER) {
                        for (Refinery refinery : main.refinerymain.refineries) {
                            for (Integer[] i : refinery.blocklocs) {
                                if (i[0] != null) {
                                    if (block.getX() == i[0] && block.getY() == i[1] && block.getZ() == i[2]) {
                                        event.setCancelled(true);
                                        main.refinerymain.openRefineries.put(event.getPlayer(), refinery);
                                        main.refinerymain.isOpen.put(event.getPlayer(), true);
                                        refinery.openRefinery(event.getPlayer(), "Refinery mark " + refinery.tier);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        breakRefinery(event.getBlock());
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        breakRefinery(event.getBlock());
    }

    private void breakRefinery(Block block) {
        if (block.getType() == Material.BARRIER) {
            for (Refinery refinery: main.refinerymain.refineries) {
                for (Integer[] i: refinery.blocklocs) {
                    if (i[0] != null) {
                        if (block.getX() == i[0] && block.getY() == i[1] && block.getZ() == i[2]) {
                            for (Integer[] integer: refinery.blocklocs) {
                                if (integer[0] != null) {
                                    block.getWorld().getBlockAt(integer[0], integer[1], integer[2]).breakNaturally();
                                    for (Entity entity : block.getWorld().getNearbyEntities(new Location(block.getWorld(), integer[0], integer[1], integer[2]), 1, 2, 1)) {
                                        if (entity instanceof ArmorStand) {
                                            entity.remove();
                                        }
                                    }
                                }
                            }
                            checkRefineryRecipe(refinery, null);
                            ItemStack[] refineryItems = {refinery.refine[0], refinery.refine[1], refinery.fuel, refinery.output[0], refinery.output[1]};
                            for (ItemStack item: refineryItems) {
                                if (item != null && item.getType() != Material.AIR) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                                }
                            }
                            if (refinery.tier == 1) {
                                block.getWorld().dropItemNaturally(block.getLocation(), main.refinerymain.refineryT1);
                            } else {
                                block.getWorld().dropItemNaturally(block.getLocation(), main.refinerymain.refineryT2);
                            }
                            main.refinerymain.refineries.remove(refinery);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            if (event.getInventory() != player.getInventory()) {
                if (player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Special Crafting") ||
                    (player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Refinery mark "))) {
                    for (Integer slot : event.getInventorySlots()) {
                        if (slot == 24 || slot == 25) {
                            event.setCancelled(true);
                            return;
                        } else if (slot == 29 || (slot >= 10 && slot <= 12) || (slot >= 19 && slot <= 21) || (slot >= 28 && slot <= 30)) {
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                                @Override
                                public void run() {
                                    if (event.getInventory() != player.getInventory() && player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Refinery mark ")) {
                                        main.refinerymain.openRefineries.get(player).recipe = null;
                                    }
                                }
                            }, 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        short slot = (short)event.getRawSlot();
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            if (event.getClickedInventory() != player.getInventory()) {
                if (event.getInventory() != player.getInventory() && player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Refinery mark ")) {
                    if (!(slot == 10 || slot == 11 || slot == 29 || slot == 24 || slot == 25) && event.getClickedInventory() != null) {
                        event.setCancelled(true);
                        if (slot == 44) {
                            main.refinerymain.openRefineries.get(player).openRefinery(player, "Special Crafting");
                        } else if (slot == 43) {
                            main.repairmain.createRepairInventory(player);
                        }
                    } else if ((slot == 24 || slot == 25) && event.getCursor().getType() != Material.AIR) {
                        event.setCancelled(true);
                    } else {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                            @Override
                            public void run() {
                                main.refinerymain.openRefineries.get(player).recipe = null;
                            }
                        }, 1);
                    }
                } else if (event.getInventory() != player.getInventory() && player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Special Crafting")) {
                    if (!((slot >= 10 && slot <= 12) || (slot >= 19 && slot <= 21) || (slot >= 28 && slot <= 30) || slot == 23 || slot == 24)) {
                        event.setCancelled(true);
                        if (slot == 44) {
                            main.refinerymain.openRefineries.get(player).openRefinery(player, "Refinery mark " + main.refinerymain.openRefineries.get(player).tier);
                        } else if (slot == 43) {
                            main.repairmain.createRepairInventory(player);
                        }
                    } else if (slot == 23) {
                        event.setCancelled(true);
                        checkCraftingRecipes(player.getOpenInventory().getTopInventory(), player);
                        Recipe recipe = main.refinerymain.currentRecipe.get(player);
                        if (recipe != null) {
                            Inventory inv = event.getInventory();
                            if (inv.getItem(24) == null) {
                                inv.setItem(24, recipe.result[0]);
                            } else if (main.refinerytools.itemstackEquals(inv.getItem(24), recipe.result[0])) {
                                if (inv.getItem(24).getAmount() + recipe.result[0].getAmount() <= recipe.result[0].getMaxStackSize()) {
                                    inv.getItem(24).setAmount(inv.getItem(24).getAmount() + recipe.result[0].getAmount());
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                            int j = 0;
                            for (int i = 0; i < 9; i++) {
                                if (i % 3 == 0) {
                                    j += 6;
                                }
                                ItemStack curitem = player.getOpenInventory().getTopInventory().getItem(i + j + 4);
                                if (curitem != null) {
                                    curitem.setAmount(curitem.getAmount() - recipe.recipe[i].getAmount());
                                }
                            }
                            main.refinerymain.currentRecipe.put(player, null);
                            player.updateInventory();
                        }
                    }
                }
            } else {
                if (player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Refinery mark ")) {
                    if ((player.getOpenInventory().getTopInventory().firstEmpty() == 24 || player.getOpenInventory().getTopInventory().firstEmpty() == 25) && event.isShiftClick()) {
                        event.setCancelled(true);
                    }
                    if (event.getClick() == ClickType.DOUBLE_CLICK) {
                        Inventory inv = player.getOpenInventory().getTopInventory();
                        if (main.refinerytools.itemstackEquals(event.getCursor(), inv.getItem(11)) || main.refinerytools.itemstackEquals(event.getCursor(), inv.getItem(10)) ||
                                main.refinerytools.itemstackEquals(event.getCursor(), inv.getItem(29))) {
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                                @Override
                                public void run() {
                                    main.refinerymain.openRefineries.get(player).recipe = null;
                                }
                            }, 1);
                        }
                    }
                } else if (player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Special Crafting")) {
                    if (player.getOpenInventory().getTopInventory().firstEmpty() == 24 && event.isShiftClick()) {
                        event.setCancelled(true);
                    }
                    if (event.getClick() == ClickType.DOUBLE_CLICK) {
                        Inventory inv = player.getOpenInventory().getTopInventory();
                        if (main.refinerytools.itemstackEquals(event.getCursor(), inv.getItem(24))) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player)event.getPlayer();
        if (event.getInventory() != player.getInventory() && player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Refinery mark ")) {
            main.refinerymain.isOpen.put(player, true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        if (player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Special Crafting")) {
            int j = 0;
            for (int i = 0; i < 9; i++) {
                if (i % 3 == 0) {
                    j += 6;
                }
                ItemStack curitem = player.getOpenInventory().getTopInventory().getItem(i + j + 4);
                if (curitem != null) {
                    player.getWorld().dropItemNaturally(player.getLocation(), curitem);
                }
            }
            if (player.getOpenInventory().getTopInventory().getItem(24) != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), player.getOpenInventory().getTopInventory().getItem(24));
            }
        } else if (player.getOpenInventory().getTitle().contains(ChatColor.BOLD.toString() + "Refinery mark ")) {
            updateRefinery(player.getOpenInventory().getTopInventory(), main.refinerymain.openRefineries.get(player));
            main.refinerymain.isOpen.put(player, false);
        }
    }

    private void checkCraftingRecipes(Inventory inv, Player player) {
        List<ItemStack> recipe = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                j += 6;
            }
            recipe.add(inv.getItem(i + j + 4));
        }
        for (Recipe recipes: main.craftingmain.recipes) {
            int yesCount = 0;
            for (int i = 0; i < recipes.recipe.length; i++) {
                if (recipes.recipe[i] != null && recipe.get(i) != null) {
                    if (main.refinerytools.itemstackEquals(recipes.recipe[i], recipe.get(i))) {
                        if (recipe.get(i).getAmount() < recipes.recipe[i].getAmount()) {
                            break;
                        } else {
                            yesCount += 1;
                        }
                    } else {
                        break;
                    }
                } else if (recipes.recipe[i] == null && recipe.get(i) == null) {
                    yesCount += 1;
                }
            }
            if (yesCount == 9) {
                main.refinerymain.currentRecipe.put(player, recipes);
                return;
            } else {
                main.refinerymain.currentRecipe.put(player, null);
            }
        }
    }

    public void checkRefineryRecipe(Refinery refinery, Player player) {
        if (player != null) {
            updateRefinery(player.getOpenInventory().getTopInventory(), refinery);
        }
        if (refinery.recipe == null) {
            if (refinery.fuel != null && refinery.fuel.getType() != Material.AIR && refinery.refine[0] != null && refinery.refine[0].getType() != Material.AIR) {
                int tier1 = refinery.tier;
                for (Recipe r : main.craftingmain.recipes) {
                    byte tier2 = r.tier;
                    boolean isRecipe = false;
                    if (main.refinerytools.itemstackEquals(refinery.refine[1], r.recipe[1])) {
                        if (main.refinerytools.itemstackEquals(refinery.fuel, r.fuel) && main.refinerytools.itemstackEquals(refinery.refine[0], r.recipe[0]) && tier1 == tier2) {
                            if (refinery.fuel.getAmount() >= r.fuel.getAmount()) {
                                if (refinery.refine[1] != null) {
                                    if (refinery.refine[0].getAmount() >= r.recipe[0].getAmount()) {
                                        if (refinery.refine[1] != null) {
                                            if (refinery.refine[1].getAmount() >= r.recipe[1].getAmount()) {
                                                isRecipe = true;
                                            }
                                        } else {
                                            isRecipe = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (isRecipe) {
                        refinery.lastRefined = System.currentTimeMillis() - 500;
                        refinery.recipe = r;
                    }
                }
            }
        }
        if (refinery.recipe != null) {
            updateRefining(refinery);
            if (player != null) {
                double yield;
                if (refinery.recipe != null) {
                    yield = getYield(refinery);
                } else {
                    yield = 0;
                }
                Inventory inv = player.getOpenInventory().getTopInventory();
                ItemStack item = inv.getItem(22);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD.toString() + (int) (yield * 100) + "%");
                item.setItemMeta(meta);
                updateRefineryInventory(inv, refinery);
            }
        } else if (player != null) {
            Inventory inv = player.getOpenInventory().getTopInventory();
            ItemStack item = inv.getItem(22);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD.toString() + "0%");
            item.setItemMeta(meta);
            updateRefineryInventory(inv, refinery);
        }
    }

    public void updateRefinery(Inventory inv, Refinery refinery) {
        refinery.refine[0] = inv.getItem(11);
        refinery.refine[1] = inv.getItem(10);
        refinery.fuel = inv.getItem(29);
        refinery.output[0] = inv.getItem(24);
        refinery.output[1] = inv.getItem(25);
    }

    public void updateRefineryInventory(Inventory inv, Refinery refinery) {
        inv.setItem(11, refinery.refine[0]);
        inv.setItem(10, refinery.refine[1]);
        inv.setItem(29, refinery.fuel);
        inv.setItem(24, refinery.output[0]);
        inv.setItem(25, refinery.output[1]);
    }

    public double getYield(Refinery refinery) {
        double timeSince = System.currentTimeMillis() - refinery.lastRefined;
        double yield = 0;
        if (refinery.recipe != null) {
            yield = refinery.refine[0].getAmount() / refinery.recipe.recipe[0].getAmount();
            if (refinery.refine[1] != null && refinery.recipe.recipe[1] != null) {
                yield = Double.min(yield, refinery.refine[1].getAmount() / refinery.recipe.recipe[1].getAmount());
            } else if (refinery.refine[1] == null && refinery.recipe.recipe[1] != null) {
                yield = 0;
            }
            yield = Double.min(yield, refinery.fuel.getAmount() / refinery.recipe.fuel.getAmount());
            yield = Double.min(yield, (timeSince / refinery.recipe.refineTime));
        } else {
            yield = 0;
        }
        return yield;
    }

    public void updateRefining(Refinery refinery) {
        if (refinery.recipe != null) {
            double yield = getYield(refinery);
            if (yield >= 1) {
                for (int i = 0; i < (int) yield; i++) {
                    boolean canRefine = false;
                    /*
                    If both output slots are empty, we know it can output
                     */
                    if (refinery.output[0] == null && refinery.output[1] == null) {
                        refinery.output[0] = refinery.recipe.result[0].clone();
                        canRefine = true;
                        if (refinery.recipe.result[1] != null) {
                            refinery.output[1] = refinery.recipe.result[1].clone();
                        }
                    /*
                    If both output slots are full, checks must be made
                     */
                    } else if (refinery.output[0] != null && refinery.output[1] != null) {
                        /*
                        If both output slots are full and both recipe output slots are full
                         */
                        if (refinery.recipe.result[1] != null) {
                            if (main.refinerytools.itemstackEquals(refinery.output[0], refinery.recipe.result[0]) && main.refinerytools.itemstackEquals(refinery.output[1], refinery.recipe.result[1])) {
                                if (((refinery.output[0].getAmount() + refinery.recipe.result[0].getAmount()) <= refinery.output[0].getMaxStackSize()) &&
                                        (refinery.output[1].getAmount() + refinery.recipe.result[1].getAmount()) <= refinery.output[1].getMaxStackSize()) {
                                    refinery.output[0].setAmount(refinery.output[0].getAmount() + refinery.recipe.result[0].getAmount());
                                    refinery.output[1].setAmount(refinery.output[1].getAmount() + refinery.recipe.result[1].getAmount());
                                    canRefine = true;
                                }
                            }
                        } else {
                            /*
                            If both output slots are full and only the first recipe output slot has an item
                            If the recipe output can go in the first slot
                             */
                            if (main.refinerytools.itemstackEquals(refinery.output[0], refinery.recipe.result[0])) {
                                if ((refinery.output[0].getAmount() + refinery.recipe.result[0].getAmount()) <= refinery.output[0].getMaxStackSize()) {
                                    refinery.output[0].setAmount(refinery.output[0].getAmount() + refinery.recipe.result[0].getAmount());
                                    canRefine = true;
                                }
                            /*
                            If the recipe output can go in the second slot
                             */
                            } else if (main.refinerytools.itemstackEquals(refinery.output[1], refinery.recipe.result[0])) {
                                if ((refinery.output[1].getAmount() + refinery.recipe.result[0].getAmount()) <= refinery.output[0].getMaxStackSize()) {
                                    refinery.output[1].setAmount(refinery.output[1].getAmount() + refinery.recipe.result[1].getAmount());
                                    canRefine = true;
                                }
                            }
                        }
                    /*
                    If only the first output slot is full
                     */
                    } else if (refinery.output[0] != null) {
                        if (main.refinerytools.itemstackEquals(refinery.output[0], refinery.recipe.result[0])) {
                            if ((refinery.output[0].getAmount() + refinery.recipe.result[0].getAmount()) <= refinery.output[0].getMaxStackSize()) {
                                refinery.output[0].setAmount(refinery.output[0].getAmount() + refinery.recipe.result[0].getAmount());
                                canRefine = true;
                            } else if (refinery.output[1] == null) {
                                refinery.output[1] = refinery.recipe.result[0].clone();
                                canRefine = true;
                            }
                        }
                    /*
                    If only the second output slot is full
                     */
                    } else {
                        if (main.refinerytools.itemstackEquals(refinery.output[1], refinery.recipe.result[1])) {
                            if (refinery.recipe.result[1] == null) {
                                refinery.output[0] = refinery.recipe.result[0].clone();
                                canRefine = true;
                            } else if ((refinery.output[1].getAmount() + refinery.recipe.result[1].getAmount()) <= refinery.output[1].getMaxStackSize()) {
                                refinery.output[1].setAmount(refinery.output[1].getAmount() + refinery.recipe.result[1].getAmount());
                                canRefine = true;
                            }
                        }
                    }
                    /*
                    Output managing items
                     */
                    if (canRefine) {
                        refinery.lastRefined = System.currentTimeMillis();
                        refinery.refine[0].setAmount(refinery.refine[0].getAmount() - refinery.recipe.recipe[0].getAmount());
                        if (refinery.refine[1] != null) {
                            refinery.refine[1].setAmount(refinery.refine[1].getAmount() - refinery.recipe.recipe[1].getAmount());
                        }
                        refinery.fuel.setAmount(refinery.fuel.getAmount() - refinery.recipe.fuel.getAmount());
                    }
                }
                refinery.recipe = null;
            }
        }
    }
}