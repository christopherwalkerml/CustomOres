package me.darkolythe.customoresplus.Refinery;

import me.darkolythe.customoresplus.CustomOresPlus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class RefineryConfigHandler {

    public CustomOresPlus main;
    public RefineryConfigHandler(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    private CustomOresPlus plugin = CustomOresPlus.getPlugin(CustomOresPlus.class);

    private FileConfiguration refineryDataConfig;
    private File refineryData;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        refineryData = new File(plugin.getDataFolder(), "RefineryData.yml");

        if (!refineryData.exists()) {
            try {
                refineryData.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "RefineryData.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create RefineryData.yml");
            }
        }
        refineryDataConfig = YamlConfiguration.loadConfiguration(refineryData);
    }

    public void saveRefineries() {
        int index = 0;
        refineryData.delete();
        try {
            refineryData.createNewFile();
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save refineries...");
        }
        refineryDataConfig = YamlConfiguration.loadConfiguration(refineryData);

        for (Refinery refinery: main.refinerymain.refineries) {
            String path = "refineries." + "." + index;
            refineryDataConfig.set(path + ".tier", refinery.tier);
            refineryDataConfig.set(path + ".world", refinery.world);
            refineryDataConfig.set(path + ".lastrefined", refinery.lastRefined);
            for (int i = 0; i < refinery.blocklocs.length; i++) {
                refineryDataConfig.set(path + ".blocklocs" + "." + i, refinery.blocklocs[i]);
            }
            refineryDataConfig.set(path + ".refine", refinery.refine);
            refineryDataConfig.set(path + ".output", refinery.output);
            refineryDataConfig.set(path + ".fuel", refinery.fuel);
            index += 1;
        }
        try {
            refineryDataConfig.save(refineryData);
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save recipes");
        }
    }

    public void loadRefineries() {
        if (refineryDataConfig.contains("refineries")) {
            for (String refineries : refineryDataConfig.getConfigurationSection("refineries.").getKeys(false)) {
                Refinery r = new Refinery(main);
                Integer[][] ints = new Integer[3][3];
                int index = 0;
                for (String str: refineryDataConfig.getConfigurationSection("refineries." + "." + refineries + ".blocklocs").getKeys(false)) {
                    Integer[] i = refineryDataConfig.getList("refineries." + "." + refineries + ".blocklocs." + str).toArray(new Integer[3]);
                    ints[index] = i;
                    index += 1;
                }
                r.blocklocs = ints;
                r.refine = (refineryDataConfig.getList("refineries." + "." + refineries + ".refine")).toArray(new ItemStack[9]);
                r.output = (refineryDataConfig.getList("refineries." + "." + refineries + ".output")).toArray(new ItemStack[9]);
                r.fuel = refineryDataConfig.getItemStack("refineries." + "." + refineries + ".fuel");
                r.tier = (byte)refineryDataConfig.getInt("refineries." + "." + refineries + ".tier");
                r.world = refineryDataConfig.getString("refineries." + "." + refineries + ".world");
                r.lastRefined = refineryDataConfig.getDouble("refineries." + "." + refineries + ".lastrefined");
                main.refinerymain.refineries.add(r);
            }
        }
    }
}
