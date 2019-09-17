package me.darkolythe.customoresplus.CustomItems;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ItemMain {

    public CustomOresPlus main;
    public ItemMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public int combinelevel;

    String[] effects = {"Blindness", "Conduit Power", "Confusion", "Dolphin's Grace", "Haste", "Fire Resistance", "Glowing", "Hunger", "Invisibility", "Jump Boost", "Levitation", "Night vision",
            "Poison", "Regeneration", "Saturation", "Mining Fatigue", "Slow Falling", "Water Breathing", "Wither"};
    String[] trueEffects = {"BLINDNESS", "CONDUIT_POWER", "CONFUSION", "DOLPHINS_GRACE", "FAST_DIGGING", "FIRE_RESISTANCE", "GLOWING", "HUNGER", "INVISIBILITY", "JUMP", "LEVITATION", "NIGHT_VISION",
            "POISON", "REGENERATION", "SATURATION", "SLOW_DIGGING", "SLOW_FALLING", "WATER_BREATHING", "WITHER"};
    public Map<Player, List<PotionEffectType>> armourEffects = new HashMap<>();
    public Map<Player, Boolean> isFlight = new HashMap<>();
    public Map<Player, String> toOpen = new HashMap<>();

    public List<PotionEffectType> getEffects(Player player) {
        if (armourEffects.containsKey(player)) {
            return armourEffects.get(player);
        } else {
            armourEffects.put(player, new ArrayList<>());
            return armourEffects.get(player);
        }
    }

    public boolean getFlight(Player player) {
        if (isFlight.containsKey(player)) {
            return isFlight.get(player);
        } else {
            isFlight.put(player, false);
            return isFlight.get(player);
        }
    }

    public void addText() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : toOpen.keySet()) {
                    if (toOpen.get(player) != null) {
                        ItemStack item = main.craftingmain.catchChat.get(player).clone();
                        main.itemcreatorgui.getGUI(player, item);
                        main.craftingmain.catchChat.remove(player);
                        toOpen.put(player, null);
                        main.itemcreatorgui.getGUI(player, item);
                    }
                }
            }
        }, 1L, 5L);
    }
}
