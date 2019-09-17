package me.darkolythe.customoresplus;

import me.darkolythe.customoresplus.Achievements.AdvancementEditor;
import me.darkolythe.customoresplus.Achievements.AdvancementListener;
import me.darkolythe.customoresplus.Achievements.AdvancementMain;
import me.darkolythe.customoresplus.Achievements.AdvancementsConfigHandler;
import me.darkolythe.customoresplus.Crafting.*;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CustomOresPlus extends JavaPlugin {

    public static CustomOresPlus plugin;
    public GenerationTools generationtools;
    public GenerationListener generationlistener;
    public Generator generator;
    public GenerationConfigHandler generationconfighandler;
    public CustomOre customore;
    public GenerationMain genmain;

    public CraftingGUICreator craftingguicreator;
    public CraftingListener craftinglistener;
    public CraftingMain craftingmain;
    public ItemCreatorGUI itemcreatorgui;
    public ItemCreatorListener itemcreatorlistener;
    public ItemEffectListener itemeffectlistener;
    public ItemMain itemmain;
    public CraftingConfigHandler craftingconfighandler;

    public RefineryListener refinerylistener;
    public RefineryMain refinerymain;
    public Refinery refinery;
    public RefineryTools refinerytools;
    public RefineryConfigHandler refineryconfighandler;

    public SpawnerListener spawnerlistener;

    public RepairListener repairlistener;
    public RepairMain repairmain;

    public InfoGUIMain infoguimain;
    public InfoGUIListener infoguilistener;

    public AdvancementMain advancementmain;
    public AdvancementEditor advancementeditor;
    public AdvancementListener advancementlistener;
    public AdvancementsConfigHandler advancementconfighandler;

    public MainTools maintools;

    public Random random;
    public List<List<Integer>> possibles = Arrays.asList(Arrays.asList(1, 0, 0), Arrays.asList(-1, 0, 0), Arrays.asList(0, 1, 0), Arrays.asList(0, -1, 0), Arrays.asList(0, 0, 1), Arrays.asList(0, 0, -1));
    public String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "Core" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    @Override
    public void onEnable() {
        plugin = this;

        maintools = new MainTools(plugin);
        maintools.setUp();

        System.out.println(prefix + "CustomOresPlus Enabled!");
    }

    @Override
    public void onDisable() {
        maintools.shutDown();

        System.out.println(prefix + "CustomOresPlus Disabled!");
    }

    public static CustomOresPlus getInstance() {
        return plugin;
    }
}
