package me.darkolythe.customoresplus.Achievements;

import me.darkolythe.customoresplus.Crafting.Recipe;
import me.darkolythe.customoresplus.CustomOresPlus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdvancementsConfigHandler {

    public CustomOresPlus main;
    public AdvancementsConfigHandler(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    private CustomOresPlus plugin = CustomOresPlus.getPlugin(CustomOresPlus.class);

    private FileConfiguration advancementsConfig;
    private File advancements;
    private FileConfiguration playerDataConfig;
    private File playerData;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        advancements = new File(plugin.getDataFolder(), "Advancements.yml");
        playerData = new File(plugin.getDataFolder(), "PlayerData.yml");

        if (!advancements.exists()) {
            try {
                advancements.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "Advancements.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create Advancements.yml");
            }
        }
        if (!playerData.exists()) {
            try {
                playerData.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "PlayerData.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create PlayerData.yml");
            }
        }
        advancementsConfig = YamlConfiguration.loadConfiguration(advancements);
        playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
    }

    public void loadAdvancements() {
        if (advancementsConfig.contains("advancements")) {
            for (String ad : advancementsConfig.getConfigurationSection("advancements.").getKeys(false)) {
                AdvancementData a = new AdvancementData();
                a.name = advancementsConfig.getString("advancements." + ad + "." + "name");
                a.pool = advancementsConfig.getString("advancements." + ad + "." + "pool");
                a.parent = advancementsConfig.getString("advancements." + ad + "." + "parent");
                a.description = advancementsConfig.getString("advancements." + ad + "." + "description");
                a.hidden = advancementsConfig.getBoolean("advancements." + ad + "." + "hidden");
                a.index = advancementsConfig.getInt("advancements." + ad + "." + "index");
                a.icon = advancementsConfig.getItemStack("advancements." + ad + "." + "icon");
                a.requiredItem = advancementsConfig.getItemStack("advancements." + ad + "." + "requireditem");
                main.advancementmain.advancements[advancementsConfig.getInt("advancements." + ad + "." + "index")] = a;
            }
        }
    }

    public void saveAdvancements() {
        advancements.delete();
        try {
            advancements.createNewFile();
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save advancements");
        }
        advancementsConfig = YamlConfiguration.loadConfiguration(advancements);

        for (AdvancementData advancement: main.advancementmain.advancements) {
            if (advancement != null) {
                advancementsConfig.set("advancements." + advancement.index + ".name", advancement.name);
                advancementsConfig.set("advancements." + advancement.index + ".pool", advancement.pool);
                advancementsConfig.set("advancements." + advancement.index + ".parent", advancement.parent);
                advancementsConfig.set("advancements." + advancement.index + ".description", advancement.description);
                advancementsConfig.set("advancements." + advancement.index + ".hidden", advancement.hidden);
                advancementsConfig.set("advancements." + advancement.index + ".index", advancement.index);
                advancementsConfig.set("advancements." + advancement.index + ".icon", advancement.icon);
                advancementsConfig.set("advancements." + advancement.index + ".requireditem", advancement.requiredItem);
            }
        }
        try {
            advancementsConfig.save(advancements);
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save advancements");
        }
    }

    public void getPlayerData(UUID uuid) {
        if (playerDataConfig.contains("player." + uuid.toString())) {
            main.advancementmain.playerAdvancements.put(uuid, playerDataConfig.getStringList("player." + uuid + ".advancements"));
        } else {
            main.advancementmain.playerAdvancements.put(uuid, new ArrayList<>());
        }
    }

    public void savePlayerData(UUID uuid) {
        try {
            playerDataConfig.set("player." + uuid.toString() + ".advancements", main.advancementmain.playerAdvancements.get(uuid));
            playerDataConfig.save(playerData);
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save PlayerData.yml");
        }
    }
}
