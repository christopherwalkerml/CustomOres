package me.darkolythe.customoresplus.Crafting;

import me.darkolythe.customoresplus.CustomOresPlus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class CraftingConfigHandler {

    public CustomOresPlus main;
    public CraftingConfigHandler(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    private CustomOresPlus plugin = CustomOresPlus.getPlugin(CustomOresPlus.class);

    private FileConfiguration recipeDataConfig;
    private File recipeData;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        recipeData = new File(plugin.getDataFolder(), "RecipeData.yml");

        if (!recipeData.exists()) {
            try {
                recipeData.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "RecipeData.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create RecipeData.yml");
            }
        }
        recipeDataConfig = YamlConfiguration.loadConfiguration(recipeData);
    }

    public void loadRecipes() {
        if (recipeDataConfig.contains("recipes")) {
            for (String workstation : recipeDataConfig.getConfigurationSection("recipes.").getKeys(false)) {
                for (String recipes : recipeDataConfig.getConfigurationSection("recipes." + workstation).getKeys(false)) {
                    Recipe r = new Recipe(workstation, null, null);
                    r.result = (recipeDataConfig.getList("recipes." + workstation + "." + recipes + ".result")).toArray(new ItemStack[2]);
                    r.recipe = (recipeDataConfig.getList("recipes." + workstation + "." + recipes + ".recipe")).toArray(new ItemStack[9]);
                    if (workstation.equals("Refinery")) {
                        r.fuel = recipeDataConfig.getItemStack("recipes." + workstation + "." + recipes + ".fuel");
                        r.tier = (byte)recipeDataConfig.getInt("recipes." + workstation + "." + recipes + ".tier");
                        r.refineTime = recipeDataConfig.getInt("recipes." + workstation + "." + recipes + ".refinetime");
                    }
                    main.craftingmain.recipes.add(r);
                }
            }
        }
    }

    public void saveRecipes() {
        int index = 0;
        recipeData.delete();
        try {
            recipeData.createNewFile();
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save recipes");
        }
        recipeDataConfig = YamlConfiguration.loadConfiguration(recipeData);

        for (Recipe recipe: main.craftingmain.recipes) {
            String path = "recipes." + recipe.workstation + "." + index;
            if (recipe.workstation.equals("Refinery")) {
                recipeDataConfig.set(path + ".tier", recipe.tier);
                recipeDataConfig.set(path + ".refinetime", recipe.refineTime);
                recipeDataConfig.set(path + ".fuel", recipe.fuel);
            }
            recipeDataConfig.set(path + ".result", recipe.result);
            recipeDataConfig.set(path + ".recipe", recipe.recipe);
            index += 1;
        }
        try {
            recipeDataConfig.save(recipeData);
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save recipes");
        }
    }
}
