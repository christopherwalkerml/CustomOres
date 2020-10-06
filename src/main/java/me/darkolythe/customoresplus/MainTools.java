package me.darkolythe.customoresplus;

import me.darkolythe.customoresplus.Achievements.AdvancementEditor;
import me.darkolythe.customoresplus.Achievements.AdvancementListener;
import me.darkolythe.customoresplus.Achievements.AdvancementMain;
import me.darkolythe.customoresplus.Achievements.AdvancementsConfigHandler;
import me.darkolythe.customoresplus.Crafting.CraftingConfigHandler;
import me.darkolythe.customoresplus.Crafting.CraftingGUICreator;
import me.darkolythe.customoresplus.Crafting.CraftingListener;
import me.darkolythe.customoresplus.Crafting.CraftingMain;
import me.darkolythe.customoresplus.CustomItems.ItemCreatorGUI;
import me.darkolythe.customoresplus.CustomItems.ItemCreatorListener;
import me.darkolythe.customoresplus.CustomItems.ItemEffectListener;
import me.darkolythe.customoresplus.CustomItems.ItemMain;
import me.darkolythe.customoresplus.Generation.*;
import me.darkolythe.customoresplus.InfoGUI.InfoGUIListener;
import me.darkolythe.customoresplus.InfoGUI.InfoGUIMain;
import me.darkolythe.customoresplus.Refinery.*;
import me.darkolythe.customoresplus.Repair.RepairListener;
import me.darkolythe.customoresplus.Repair.RepairMain;
import me.darkolythe.customoresplus.Spawners.SpawnerListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;

import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class MainTools {

    public CustomOresPlus main;
    public MainTools(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public void setUp() {
        main.generationtools = new GenerationTools(main);
        main.generationlistener = new GenerationListener(main);
        main.generator = new Generator(main);
        main.generationconfighandler = new GenerationConfigHandler(main);
        main.customore = new CustomOre(main);
        main.genmain = new GenerationMain(main);

        main.craftingguicreator = new CraftingGUICreator(main);
        main.craftinglistener = new CraftingListener(main);
        main.craftingmain = new CraftingMain(main);
        main.craftingconfighandler = new CraftingConfigHandler(main);


        main.itemcreatorgui = new ItemCreatorGUI(main);
        main.itemcreatorlistener = new ItemCreatorListener(main);
        main.itemeffectlistener = new ItemEffectListener(main);
        main.itemmain = new ItemMain(main);

        main.refinerylistener = new RefineryListener(main);
        main.refinerymain = new RefineryMain(main);
        main.refinery = new Refinery(main);
        main.refinerytools = new RefineryTools(main);
        main.refineryconfighandler = new RefineryConfigHandler(main);

        main.spawnerlistener = new SpawnerListener(main);

        main.repairmain = new RepairMain(main);
        main.repairlistener = new RepairListener(main);

        main.infoguimain = new InfoGUIMain(main);
        main.infoguilistener = new InfoGUIListener(main);

        main.advancementmain = new AdvancementMain(main);
        main.advancementlistener = new AdvancementListener(main);
        main.advancementeditor = new AdvancementEditor(main);
        main.advancementconfighandler = new AdvancementsConfigHandler(main);

        getConfigData();
        main.generationconfighandler.setup();
        main.craftingconfighandler.setup();
        main.refineryconfighandler.setup();
        main.advancementconfighandler.setup();
        main.generationconfighandler.getOreData();
        main.generationconfighandler.getToolData();
        main.craftingconfighandler.loadRecipes();
        main.refineryconfighandler.loadRefineries();
        main.advancementconfighandler.loadAdvancements();
        for (Refinery refinery: main.refinerymain.refineries) {
            main.refinerylistener.checkRefineryRecipe(refinery, null);
        }
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            main.generationconfighandler.getPlayerData(player.getUniqueId());
            main.advancementconfighandler.getPlayerData(player.getUniqueId());
            main.genmain.getToggleCooldown(player);
        }

        for (World world: Bukkit.getServer().getWorlds()) {
            for (Chunk chunk: world.getLoadedChunks()) {
                main.generationlistener.loadChunk(chunk);
            }
        }

        main.getCommand("customoresplus").setExecutor(new CommandHandler());
        final PluginManager pluginManager = Bukkit.getPluginManager();
        Stream.of(main.generationlistener,
                main.craftinglistener,
                main.itemcreatorlistener,
                main.itemeffectlistener,
                main.refinerylistener,
                main.spawnerlistener,
                main.repairlistener,
                main.infoguilistener,
                main.advancementlistener).forEach(it -> pluginManager.registerEvents(it, main));

        main.genmain.generateCustomOres();
        main.refinerymain.refineItemCheck();
        main.craftingmain.checkSearch();
        main.itemmain.addText();
        main.advancementmain.addText();
        main.craftingmain.setUp();
        main.refinerymain.setUp();

        /*
        Add recipe for refinery tier I
         */
        ShapedRecipe refineryT1Recipe = new ShapedRecipe(new NamespacedKey(main, "refinery_mark_i") ,main.refinerymain.refineryT1);
        refineryT1Recipe.shape("CCC", "FSW", "CCC");
        refineryT1Recipe.setIngredient('C', Material.COBBLESTONE);
        refineryT1Recipe.setIngredient('F', Material.FURNACE);
        refineryT1Recipe.setIngredient('S', Material.FLINT_AND_STEEL);
        refineryT1Recipe.setIngredient('W', Material.CRAFTING_TABLE);
        Bukkit.getServer().addRecipe(refineryT1Recipe);

        ShapelessRecipe bookrecipe = new ShapelessRecipe(new NamespacedKey(main, "core_compendium"), main.infoguimain.giveInfoBook(null));
        bookrecipe.addIngredient(Material.BOOK);
        bookrecipe.addIngredient(Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(bookrecipe);
    }

    public void shutDown() {
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            Iterator<Chunk> i = main.genmain.getPlayerDrawChunks(player, player.getWorld()).iterator();
            Chunk chunk;
            while (i.hasNext()) {
                chunk = i.next();
                for (CustomOre ore: main.genmain.getCustomOre(chunk.getWorld(), chunk)) {
                    main.generationtools.unDrawOre(ore, player);
                }
                i.remove();
            }
            main.generationconfighandler.savePlayerData(player.getUniqueId());
            main.advancementconfighandler.savePlayerData(player.getUniqueId());
        }
        System.out.println(main.prefix + ChatColor.AQUA + "Saving CustomOres chunk data...");
        int i = 0;
        for (String worldstr: main.genmain.worlds) {
            World world = Bukkit.getServer().getWorld(worldstr);
            if (main.genmain.allCustomOres.containsKey(world)) {
                for (Chunk chunk : main.genmain.allCustomOres.get(world).keySet()) {
                    main.generationconfighandler.saveChunkToFiles(world, chunk, main.genmain.allCustomOres.get(world).get(chunk));
                    i += 1;
                    if (i % 200 == 0) {
                        System.out.println(main.prefix + ChatColor.AQUA + "...");
                    }
                }
            }
        }
        main.craftingconfighandler.saveRecipes();
        main.advancementconfighandler.saveAdvancements();
        for (Refinery refinery: main.refinerymain.refineries) {
            main.refinerylistener.checkRefineryRecipe(refinery, null);
        }
        main.refineryconfighandler.saveRefineries();
    }

    public void getConfigData() {
        main.saveDefaultConfig();

        main.genmain.seed = main.getConfig().getLong("seed");
        if (main.genmain.seed == 0) {
            main.genmain.seed = main.generationtools.generateSeed();
            main.getConfig().set("seed", main.genmain.seed);
        }
        main.genmain.worlds = main.getConfig().getStringList("worlds");
        main.genmain.cooldown = (main.getConfig().getInt("cooldown") * 1000);
        main.advancementmain.broadcastAchievements = main.getConfig().getBoolean("broadcastachievements");
        main.random = new Random(main.genmain.seed);
        main.itemmain.combinelevel = main.getConfig().getInt("combinelevel");
        main.saveConfig();
    }

    public String getSubCommands(Player player) {
        String subcommand = "";
        boolean prev = false;
        if (player.hasPermission("customoresplus.create")) {
            subcommand = subcommand.concat("create");
            prev = true;
        }
        if (player.hasPermission("customoresplus.toggledraw")) {
            if (prev) {
                subcommand = subcommand.concat("/");
            }
            subcommand = subcommand.concat("toggledraw");
            prev = true;
        }
        if (player.hasPermission("customoresplus.recipe")) {
            if (prev) {
                subcommand = subcommand.concat("/");
            }
            subcommand = subcommand.concat("recipe");
            prev = true;
        }
        if (player.hasPermission("customoresplus.item")) {
            if (prev) {
                subcommand = subcommand.concat("/");
            }
            subcommand = subcommand.concat("item");
            prev = true;
        }
        if (player.hasPermission("customoresplus.book")) {
            if (prev) {
                subcommand = subcommand.concat("/");
            }
            subcommand = subcommand.concat("book");
            prev = true;
        }
        if (player.hasPermission("customoresplus.advancementadmin")) {
            if (prev) {
                subcommand = subcommand.concat("/");
            }
            subcommand = subcommand.concat("advancement");
            prev = true;
        }
        return subcommand;
    }
}
