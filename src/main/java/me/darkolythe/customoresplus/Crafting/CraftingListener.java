package me.darkolythe.customoresplus.Crafting;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

public class CraftingListener implements Listener {

    public CustomOresPlus main;
    public CraftingListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (event.getClickedInventory() != null) {
            String title = player.getOpenInventory().getTitle();
            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (item != null) {
                    if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Custom Recipe List")) {
                        if (event.getClickedInventory() != player.getInventory()) {
                            event.setCancelled(true);
                            if (item.hasItemMeta()) {
                                ItemMeta itemmeta = item.getItemMeta();
                                if (itemmeta.hasLore()) {
                                    if (itemmeta.getLore().equals(Arrays.asList(ChatColor.BLACK + "" + ChatColor.STRIKETHROUGH + ""))) {
                                        if (item.getType() == Material.GREEN_CONCRETE) {
                                            if (itemmeta.getDisplayName().equals(ChatColor.GREEN + "Create new recipe")) {
                                                main.craftingguicreator.chooseWorkstationGUI(player);
                                                return;
                                            }
                                        }
                                    }
                                } else if (item.getItemMeta().getDisplayName().contains("Click to go to page ")) {
                                    String search = "";
                                    if (event.getInventory().getItem(49).getItemMeta().getDisplayName().contains("Searching... ")) {
                                        search = event.getInventory().getItem(49).getItemMeta().getDisplayName().replace(ChatColor.GRAY.toString() + "Searching... " + ChatColor.BOLD.toString(), "").replace("'", "");
                                    }
                                    main.craftingguicreator.recipeListGUI(player, (byte) (Byte.parseByte(item.getItemMeta().getDisplayName().replace(ChatColor.GRAY + "Click to go to page ", "")) - 1), search);
                                    return;
                                } else if (item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Click here to search for a recipe")) {
                                    main.craftingmain.catchChat.put(player, new ItemStack(Material.AIR, 1));
                                    main.craftingmain.catchType.put(player, "search");
                                    player.closeInventory();
                                    player.sendMessage(main.prefix + ChatColor.GRAY + "Enter word(s) to search in chat, or type 'cancel' to cancel");
                                    return;
                                } else if (event.getRawSlot() == 53) {
                                    return;
                                }
                                int add = 0;
                                if (event.getInventory().getItem(47) != null) {
                                    if (event.getInventory().getItem(47).getType() == Material.ARROW) {
                                        add = Integer.parseInt(event.getInventory().getItem(47).getItemMeta().getDisplayName().replace(ChatColor.GRAY + "Click to go to page ", ""));
                                    }
                                }
                                add *= 45;
                                if (event.isRightClick()) {
                                    if (player.hasPermission("customoresplus.recipeadmin")) {
                                        main.craftingmain.recipes.remove(event.getSlot() + add);
                                        main.craftingguicreator.recipeListGUI(player, (byte) 0, "");
                                    } else {
                                        if (main.craftingmain.recipes.get(event.getSlot() + add).workstation.contains("Refinery")) {
                                            main.craftingguicreator.createRefineryMenu(player, main.craftingmain.recipes.get(event.getSlot() + add));
                                        } else {
                                            main.craftingguicreator.createSpecialCraftingMenu(player, main.craftingmain.recipes.get(event.getSlot() + add));
                                        }
                                    }
                                } else if (event.isLeftClick()) {
                                    if (main.craftingmain.recipes.get(event.getSlot() + add).workstation.contains("Refinery")) {
                                        main.craftingguicreator.createRefineryMenu(player, main.craftingmain.recipes.get(event.getSlot() + add));
                                    } else {
                                        main.craftingguicreator.createSpecialCraftingMenu(player, main.craftingmain.recipes.get(event.getSlot() + add));
                                    }
                                    if (player.hasPermission("customoresplus.recipeadmin")) {
                                        main.craftingmain.recipes.remove(event.getSlot() + add);
                                    }
                                }
                            }
                        } else {
                            event.setCancelled(true);
                        }
                    } else if (title.contains(ChatColor.LIGHT_PURPLE + "Create ") && title.contains(" Recipe")) {
                        if (!player.hasPermission("customoresplus.recipeadmin")) {
                            event.setCancelled(true);
                        }
                        String workbench = title.replace(ChatColor.LIGHT_PURPLE + "Create ", "");
                        workbench = workbench.replace(" Recipe", "");
                        Map<Integer, ItemStack> map;
                        if (workbench.equals("Special Crafting")) {
                            map = main.craftingmain.specialCraftingSlots;
                        } else if (workbench.equals("Refinery")) {
                            map = main.craftingmain.refinerySlots;
                        } else {
                            map = main.craftingmain.refinerySlots;
                        }
                        if (event.isShiftClick()) {
                            event.setCancelled(true);
                        }
                        if (event.getClickedInventory() != player.getInventory() && player.getOpenInventory().getTitle().equals(title)) {
                            if (!map.keySet().contains(event.getSlot()) || event.getSlot() == 41 || event.getSlot() == 43 ||
                                    event.getSlot() == 8 || event.getSlot() == 22 || !player.hasPermission("customoresplus.recipeadmin")) {
                                event.setCancelled(true);
                            } else if (map.containsValue(item) || (item.getType() == Material.RED_STAINED_GLASS_PANE && (item.getItemMeta().getDisplayName().contains(ChatColor.RED + "The recipe") ||
                                    item.getItemMeta().getDisplayName().contains(ChatColor.RED + "The fuel")))) {
                                event.setCurrentItem(null);
                            }
                            if (event.getSlot() == 41) {
                                main.craftingguicreator.recipeListGUI(player, (byte) 0, "");
                            } else if (event.getSlot() == 43 && event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                                main.craftingmain.createRecipe(event.getInventory(), workbench, player);
                            } else if (event.getSlot() == 8 && player.hasPermission("customoresplus.recipeadmin")) {
                                if (item.getType() == Material.BLUE_STAINED_GLASS_PANE) {
                                    item.setType(Material.PURPLE_STAINED_GLASS_PANE);
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setDisplayName(ChatColor.DARK_PURPLE + "Mark II Refinery");
                                    meta.setLore(Arrays.asList(ChatColor.BLUE.toString() + ChatColor.ITALIC.toString() + "Click to make the recipe",
                                            ChatColor.BLUE.toString() + ChatColor.ITALIC.toString() + "require a Mark I Refinery"));
                                    item.setItemMeta(meta);
                                    event.setCurrentItem(item);
                                } else if (item.getType() == Material.PURPLE_STAINED_GLASS_PANE) {
                                    item.setType(Material.BLUE_STAINED_GLASS_PANE);
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setDisplayName(ChatColor.BLUE + "Mark I Refinery");
                                    meta.setLore(Arrays.asList("Click to make the recipe", "require a Mark II Refinery"));
                                    item.setItemMeta(meta);
                                    event.setCurrentItem(item);
                                }
                            } else if (event.getSlot() == 22 && player.hasPermission("customoresplus.recipeadmin")) {
                                item = event.getClickedInventory().getItem(22);
                                ItemMeta meta = item.getItemMeta();
                                int time = Integer.parseInt(meta.getDisplayName().replace(" seconds to refine", "").replace(ChatColor.GOLD.toString(), ""));
                                if (event.isLeftClick()) {
                                    meta.setDisplayName(ChatColor.GOLD.toString() + main.generationtools.clamp(time + 1, 1, 1000) + " seconds to refine");
                                } else if (event.isRightClick()) {
                                    meta.setDisplayName(ChatColor.GOLD.toString() + main.generationtools.clamp(time - 1, 1, 1000) + " seconds to refine");
                                }
                                item.setItemMeta(meta);
                            }
                        }
                    }
                }
            } else if (event.getInventory().getType() == InventoryType.HOPPER) {
                if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Choose Workstation")) {
                    event.setCancelled(true);
                    if (item != null) {
                        if (item.hasItemMeta()) {
                            ItemMeta itemmeta = item.getItemMeta();
                            if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "New refinery recipe")) {
                                main.craftingguicreator.createRefineryMenu(player, null);
                            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "New special crafting recipe")) {
                                main.craftingguicreator.createSpecialCraftingMenu(player, null);
                            } else if (itemmeta.getDisplayName().equals(ChatColor.RED + "Go back to recipe list")) {
                                main.craftingguicreator.recipeListGUI(player, (byte) 0, "");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (main.craftingmain.catchChat.containsKey(player)) {
            if (main.craftingmain.catchType.get(player).equals("search")) {
                event.setCancelled(true);
                if (!event.getMessage().equals("cancel")) {
                    main.craftingmain.search.put(player, event.getMessage());
                    main.craftingmain.catchChat.remove(player);
                } else {
                    main.craftingmain.search.put(player, "");
                    main.craftingmain.catchChat.remove(player);
                }
            }
        }
    }
}
