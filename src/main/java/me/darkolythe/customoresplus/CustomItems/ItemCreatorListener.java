package me.darkolythe.customoresplus.CustomItems;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemCreatorListener implements Listener {

    public CustomOresPlus main;
    public ItemCreatorListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        String title = player.getOpenInventory().getTitle();
        ItemStack pitem;
        if (item != null) {
            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (event.getClickedInventory() != player.getInventory()) {
                    if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Enchantment GUI")) {
                        event.setCancelled(true);
                        pitem = event.getInventory().getItem((((Enchantment.values().length / 9) + 2) * 9) - 5);
                        if (event.getRawSlot() < ((Enchantment.values().length / 9) + 1) * 9 && event.getRawSlot() >= 0 && pitem != null) {
                            Enchantment[] enchants = Enchantment.values();
                            Arrays.sort(enchants, Comparator.comparing(Enchantment::toString));
                            Enchantment enchant = enchants[event.getRawSlot()];
                            if (event.isLeftClick()) {
                                if (pitem.containsEnchantment(enchant)) {
                                    pitem.addUnsafeEnchantment(enchant, pitem.getEnchantmentLevel(enchant) + 1);
                                } else {
                                    pitem.addUnsafeEnchantment(enchant, 1);
                                }
                            } else if (event.isRightClick()) {
                                if (pitem.getEnchantmentLevel(enchant) == 1) {
                                    pitem.removeEnchantment(enchant);
                                }
                                if (pitem.containsEnchantment(enchant)) {
                                    pitem.addUnsafeEnchantment(enchant, pitem.getEnchantmentLevel(enchant) - 1);
                                }
                            }
                            main.itemcreatorgui.enchantGUI(player, pitem);
                        }
                        if (event.getSlot() == (((Enchantment.values().length / 9) + 2) * 9) - 1) {
                            main.itemcreatorgui.getGUI(player, pitem);
                        }
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Special Modifiers GUI")) {
                        pitem = event.getInventory().getItem(22);
                        event.setCancelled(true);
                        if (event.getSlot() <= 33 && event.getSlot() >= 0 && pitem != null) {
                            if (event.isLeftClick()) {
                                if (event.getSlot() == 0) {
                                    if (event.isShiftClick()) {
                                        pitem.setDurability((short) (pitem.getDurability() + 10));
                                    } else {
                                        pitem.setDurability((short) (pitem.getDurability() + 1));
                                    }
                                } else if (event.getSlot() == 1) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    pmeta.setUnbreakable(true);
                                    pitem.setItemMeta(pmeta);
                                } else if (event.getSlot() == 2) {
                                    setAttribute(Attribute.GENERIC_ARMOR, pitem, 1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 3) {
                                    setAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, pitem, 1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 4) {
                                    setAttribute(Attribute.GENERIC_ATTACK_DAMAGE, pitem, 1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 5) {
                                    setAttribute(Attribute.GENERIC_MAX_HEALTH, pitem, 1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 6) {
                                    setAttribute(Attribute.GENERIC_MOVEMENT_SPEED, pitem, 0.10, AttributeModifier.Operation.ADD_SCALAR);
                                } else if (event.getSlot() == 7) {
                                    setAttribute(Attribute.GENERIC_ATTACK_SPEED, pitem, 0.10, AttributeModifier.Operation.ADD_SCALAR);
                                } else if (event.getSlot() == 8) {
                                    setAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, pitem, 1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 11) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        pmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    }
                                    pitem.setItemMeta(pmeta);
                                } else if (event.getSlot() == 12) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        pmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    }
                                    pitem.setItemMeta(pmeta);
                                } else if (event.getSlot() == 14) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        pmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                    }
                                    pitem.setItemMeta(pmeta);
                                } else if (event.getSlot() == 15) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        pmeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                    }
                                    pitem.setItemMeta(pmeta);
                                }
                            } else if (event.isRightClick()) {
                                if (event.getSlot() == 0) {
                                    if (event.isShiftClick()) {
                                        pitem.setDurability((short) (pitem.getDurability() - 10));
                                    } else {
                                        pitem.setDurability((short) (pitem.getDurability() - 1));
                                    }
                                } else if (event.getSlot() == 1) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    pmeta.setUnbreakable(false);
                                    pitem.setItemMeta(pmeta);
                                } else if (event.getSlot() == 2) {
                                    setAttribute(Attribute.GENERIC_ARMOR, pitem, -1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 3) {
                                    setAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, pitem, -1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 4) {
                                    setAttribute(Attribute.GENERIC_ATTACK_DAMAGE, pitem, -1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 5) {
                                    setAttribute(Attribute.GENERIC_MAX_HEALTH, pitem, -1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 6) {
                                    setAttribute(Attribute.GENERIC_MOVEMENT_SPEED, pitem, -0.10, AttributeModifier.Operation.ADD_SCALAR);
                                } else if (event.getSlot() == 7) {
                                    setAttribute(Attribute.GENERIC_ATTACK_SPEED, pitem, -0.10, AttributeModifier.Operation.ADD_SCALAR);
                                } else if (event.getSlot() == 8) {
                                    setAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, pitem, -1, AttributeModifier.Operation.ADD_NUMBER);
                                } else if (event.getSlot() == 11) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        if (pmeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                                            pmeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                            pitem.setItemMeta(pmeta);
                                        }
                                    }
                                } else if (event.getSlot() == 12) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        if (pmeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                                            pmeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                                            pitem.setItemMeta(pmeta);
                                        }
                                    }
                                } else if (event.getSlot() == 14) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        if (pmeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) {
                                            pmeta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                            pitem.setItemMeta(pmeta);
                                        }
                                    }
                                } else if (event.getSlot() == 15) {
                                    ItemMeta pmeta = pitem.getItemMeta();
                                    if (pmeta != null) {
                                        if (pmeta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                                            pmeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                            pitem.setItemMeta(pmeta);
                                        }
                                    }
                                }
                            }
                            if (event.getSlot() == 26) {
                                main.itemcreatorgui.getGUI(player, pitem);
                            }
                        }
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI")) {
                        event.setCancelled(true);
                        pitem = event.getInventory().getItem(31);
                        if (event.getSlot() >= 0 && event.getSlot() <= 19) {
                            boolean contains = false;
                            if (item.hasItemMeta()) {
                                ItemMeta itemmeta = item.getItemMeta();
                                String enchant = itemmeta.getLore().get(0).replace(ChatColor.GOLD.toString(), ChatColor.GRAY.toString());
                                List<String> lore = new ArrayList<>();
                                ItemMeta pmeta = pitem.getItemMeta();
                                if (pmeta != null) {
                                    if (pmeta.hasLore()) {
                                        for (String l : pmeta.getLore()) {
                                            if (l.contains(enchant)) {
                                                int level = Integer.parseInt(l.replace(enchant + " ", ""));
                                                if (event.isLeftClick()) {
                                                    lore.add(enchant + " " + (level + 1));
                                                } else if (event.isRightClick()) {
                                                    if (level - 1 > 0) {
                                                        lore.add(enchant + " " + (level - 1));
                                                    }
                                                }
                                                contains = true;
                                            } else {
                                                lore.add(l);
                                            }
                                        }
                                    }
                                }
                                if (!contains) {
                                    List<String> newlore = new ArrayList<>();
                                    if (event.isLeftClick()) {
                                        newlore.add(enchant + " " + 1);
                                    }
                                    for (String l : lore) {
                                        newlore.add(l);
                                    }
                                    pmeta.setLore(newlore);
                                } else {
                                    pmeta.setLore(lore);
                                }
                                pitem.setItemMeta(pmeta);
                            }
                            main.itemcreatorgui.potionEffectsGUI(player, pitem);
                        }
                        if (event.getSlot() == 35) {
                            main.itemcreatorgui.getGUI(player, pitem);
                        }
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI")) {
                        event.setCancelled(true);
                        if (item.hasItemMeta()) {
                            ItemMeta itemmeta = item.getItemMeta();
                            if (itemmeta.getDisplayName().equals(ChatColor.DARK_PURPLE + "Status Effects")) {
                                main.itemcreatorgui.potionEffectsGUI(player, event.getInventory().getItem(22));
                            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "Special Modifiers")) {
                                main.itemcreatorgui.modifiersGUI(player, event.getInventory().getItem(22));
                            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "Item Lore")) {
                                if (event.isLeftClick()) {
                                    main.craftingmain.catchChat.put(player, event.getInventory().getItem(22));
                                    main.craftingmain.catchType.put(player, "lore");
                                    player.closeInventory();
                                    player.sendMessage(main.prefix + ChatColor.GRAY + "Enter a single line of lore in chat. Type 'cancel' to cancel.");
                                } else if (event.isRightClick()) {
                                    ItemStack i = event.getInventory().getItem(22);
                                    ItemMeta m = i.getItemMeta();
                                    m.setLore(null);
                                    i.setItemMeta(m);
                                }
                            } else if (itemmeta.getDisplayName().equals(ChatColor.GOLD + "Item Name")) {
                                if (event.isLeftClick()) {
                                    main.craftingmain.catchChat.put(player, event.getInventory().getItem(22));
                                    main.craftingmain.catchType.put(player, "name");
                                    player.closeInventory();
                                    player.sendMessage(main.prefix + ChatColor.GRAY + "Enter item name in chat. Type 'cancel' to cancel.");
                                } else if (event.isRightClick()) {
                                    ItemStack i = event.getInventory().getItem(22);
                                    ItemMeta m = i.getItemMeta();
                                    m.setDisplayName(null);
                                    i.setItemMeta(m);
                                }
                            } else if (itemmeta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Item Enchantments")) {
                                if (event.isLeftClick()) {
                                    main.itemcreatorgui.enchantGUI(player, event.getInventory().getItem(22));
                                } else if (event.isRightClick()) {
                                    ItemStack i = event.getInventory().getItem(22);
                                    for (Enchantment enchant : i.getEnchantments().keySet()) {
                                        i.removeEnchantment(enchant);
                                    }
                                }
                            } else if (event.getSlot() == 22) {
                                player.getInventory().setItemInMainHand(item);
                                player.closeInventory();
                            }
                        }
                        if (event.getSlot() == 22) {
                            player.getInventory().setItemInMainHand(item);
                            player.closeInventory();
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
            if (!main.craftingmain.catchType.get(player).equals("search")) {
                event.setCancelled(true);
                ItemStack item = main.craftingmain.catchChat.get(player).clone();
                if (!event.getMessage().equals("cancel")) {
                    ItemMeta meta = item.getItemMeta();
                    if (main.craftingmain.catchType.get(player).equals("name")) {
                        meta.setDisplayName(event.getMessage());
                    } else if (main.craftingmain.catchType.get(player).equals("lore")) {
                        meta.setLore(addLore(meta, event.getMessage(), false));
                    }
                    item.setItemMeta(meta);
                }
                main.itemmain.toOpen.put(player, event.getMessage());
                main.craftingmain.catchChat.put(player, item);
            } else {
                return;
            }
        }
    }

    private void setAttribute(Attribute at, ItemStack pitem, double amount, AttributeModifier.Operation operation) {
        ItemMeta pmeta = pitem.getItemMeta();
        if (pitem.hasItemMeta()) {
            if (pmeta.getAttributeModifiers(at) != null) {
                for (AttributeModifier am : pmeta.getAttributeModifiers(at)) {
                    pmeta.removeAttributeModifier(at);
                    pmeta.addAttributeModifier(at, new AttributeModifier(at.name()
                            , am.getAmount() + amount, operation));
                    pmeta.setLore(removeLoreLine(pmeta.getLore(), WordUtils.capitalize(at.name().replace("GENERIC_", "").toLowerCase().replace("_", " "))));
                    if (am.getAmount() + amount < -0.09 || am.getAmount() + amount > 0.09) {
                        pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round((am.getAmount() + amount) * 10) / 10.0, true));
                    } else {
                        pmeta.removeAttributeModifier(at);
                    }
                }
            } else {
                pmeta.addAttributeModifier(at, new AttributeModifier(at.name(), amount, operation));
                pmeta.setLore(removeLoreLine(pmeta.getLore(), WordUtils.capitalize(at.name().replace("GENERIC_", "").toLowerCase().replace("_", " "))));
                pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round(amount * 10) / 10.0, true));
            }
        } else {
            pmeta.addAttributeModifier(at, new AttributeModifier(at.name(), amount, operation));
            pmeta.setLore(removeLoreLine(pmeta.getLore(), WordUtils.capitalize(at.name().replace("GENERIC_", "").toLowerCase()).replace("_", " ")));
            pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round(amount * 10) / 10.0, true));
        }
        pitem.setItemMeta(pmeta);
    }

    private List<String> removeLoreLine(List<String> lore, String line) {
        if (lore != null) {
            Iterator<String> i = lore.iterator();
            String s;
            while (i.hasNext()) {
                s = i.next();
                if (s.contains(line)) {
                    i.remove();
                }
            }
        }
        return lore;
    }

    @EventHandler
    public void prepareAnvilEvent(PrepareAnvilEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getItem(0) != null && inv.getItem(1) != null) {
            ItemStack book = null;
            ItemStack result = null;
            if (inv.getItem(0).getType() == Material.BOOK) {
                book = inv.getItem(0).clone();
                result = inv.getItem(1).clone();
            } else if (inv.getItem(1).getType() == Material.BOOK) {
                book = inv.getItem(1).clone();
                result = inv.getItem(0).clone();
            }
            if (book != null) {
                if (book.hasItemMeta()) {
                    ItemMeta bookmeta = book.getItemMeta();
                    if (bookmeta.hasLore()) {
                        for (String l: bookmeta.getLore()) {
                            if (l.contains("Requires " + main.itemmain.combinelevel + " levels to combine in an anvil")) {
                                if (bookmeta.isUnbreakable()) {
                                    ItemMeta resultmeta = result.getItemMeta();
                                    resultmeta.setUnbreakable(true);
                                    result.setItemMeta(resultmeta);
                                    event.setResult(result);
                                }
                            }
                        }
                        for (String l: bookmeta.getLore()) {
                            if (l.contains("Requires " + main.itemmain.combinelevel + " levels to combine in an anvil")) {
                                for (int i = 0; i < bookmeta.getLore().size(); i++) {
                                    if (!bookmeta.getLore().get(i).contains("Requires " + main.itemmain.combinelevel + " levels to combine in an anvil")) {
                                        String effect = bookmeta.getLore().get(i);
                                        ItemMeta resultmeta = result.getItemMeta();
                                        resultmeta.setLore(addLore(resultmeta, effect, true));
                                        result.setItemMeta(resultmeta);
                                        event.setResult(result);
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().getType() == InventoryType.ANVIL) {
                if (event.getRawSlot() == 2) {
                    if (player.getItemOnCursor().getType() == Material.AIR) {
                        Inventory inv = event.getInventory();
                        if (inv.getItem(0) != null && inv.getItem(1) != null) {
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                                @Override
                                public void run() {
                                    ItemStack book = null;
                                    if (inv.getItem(0) != null) {
                                        if (inv.getItem(0).getType() == Material.BOOK) {
                                            book = inv.getItem(0);
                                        }
                                    }
                                    if (book != null) {
                                        if (book.hasItemMeta()) {
                                            if (book.getItemMeta().hasLore()) {
                                                for (String l : book.getItemMeta().getLore()) {
                                                    if (l.contains("Requires " + main.itemmain.combinelevel + " levels to combine in an anvil")) {
                                                        if (player.getExpToLevel() >= 45) {
                                                            player.setItemOnCursor(inv.getItem(2));
                                                            player.setLevel(player.getLevel() - 45);
                                                            inv.getItem(0).setAmount(inv.getItem(0).getAmount() - 1);
                                                            inv.getItem(1).setAmount(inv.getItem(1).getAmount() - 1);
                                                            inv.getItem(2).setAmount(inv.getItem(2).getAmount() - 1);
                                                            player.updateInventory();
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }, 1);
                        }
                    }
                }
            }
        }
    }

    public List<String> addLore(ItemMeta meta, String line, boolean top) {
        List<String> newlore = new ArrayList<>();
        if (!top) {
            if (meta.hasLore()) {
                newlore = meta.getLore();
            } else {
                newlore = new ArrayList<>();
            }
            newlore.add(line);
        } else {
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                newlore.add(line);
                for (String str : lore) {
                    newlore.add(str);
                }
            } else {
                newlore.add(line);
            }
        }
        return newlore;
    }
}
