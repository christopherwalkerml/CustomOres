package me.darkolythe.customoresplus.Generation;

import me.darkolythe.customoresplus.CustomOresPlus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class GenerationConfigHandler {

    public CustomOresPlus main;
    public GenerationConfigHandler(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    private CustomOresPlus plugin = CustomOresPlus.getPlugin(CustomOresPlus.class);

    private FileConfiguration oresConfig;
    private File ores;
    private FileConfiguration playerDataConfig;
    private File playerData;
    private File chunkfolder;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        ores = new File(plugin.getDataFolder(), "CustomOres.yml");
        playerData = new File(plugin.getDataFolder(), "PlayerData.yml");
        chunkfolder = new File(plugin.getDataFolder() + "/ChunkData");

        if (!ores.exists()) {
            plugin.saveResource("CustomOres.yml", false);
            System.out.println(main.prefix + ChatColor.GREEN + "CustomOres.yml has been created");
        }
        if (!playerData.exists()) {
            try {
                playerData.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "PlayerData.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create PlayerData.yml");
            }
        }
        if (!chunkfolder.exists()) {
            try {
                chunkfolder.mkdirs();
                System.out.println(main.prefix + ChatColor.GREEN + "ChunkData folder has been created");
            } catch (SecurityException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create ChunkData folder");
            }
        }

        oresConfig = YamlConfiguration.loadConfiguration(ores);
        playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
    }


    public void getOreData() {
        if (oresConfig.contains("ores")) {
            for (String ore : oresConfig.getConfigurationSection("ores.").getKeys(false)) {
                OreData oredata = new OreData();
                oredata.type = ore;
                oredata.oreid = (short)oresConfig.getInt("ores." + ore + ".oreid");
                oredata.ymin = oresConfig.getInt("ores." + ore + ".ylevel.min");
                oredata.ymax = oresConfig.getInt("ores." + ore + ".ylevel.max");
                oredata.frequency = oresConfig.getInt("ores." + ore + ".size.frequency");
                oredata.veinsize = oresConfig.getInt("ores." + ore + ".size.veinsize");
                oredata.id = oresConfig.getString("ores." + ore + ".texture.id");
                oredata.textures = oresConfig.getString("ores." + ore + ".texture.textures");
                oredata.blocks = oresConfig.getStringList("ores." + ore + ".blocks");
                oredata.colour = oresConfig.getString("ores." + ore + ".colour");
                oredata.blastResistant = oresConfig.getBoolean("ores." + ore + ".blastresistant");
                oredata.worlds = oresConfig.getStringList("ores." + ore + ".worlds");
                oredata.requiredtier = oresConfig.getInt("ores." + ore + ".requiredtool");
                main.genmain.allOreData.add(oredata);
            }
        }
    }

    public void getToolData() {
        if (oresConfig.contains("tools")) {
            for (String tier: oresConfig.getConfigurationSection("tools.").getKeys(false)) {
                main.genmain.tooltiers.put(Integer.parseInt(tier), ChatColor.translateAlternateColorCodes('&', oresConfig.getString("tools." + tier)));
            }
        }
    }

    public void getPlayerData(UUID uuid) {
        if (playerDataConfig.contains("player." + uuid.toString())) {
            main.genmain.playerDrawToggle.put(uuid, playerDataConfig.getBoolean("player." + uuid + ".drawores"));
        } else {
            main.genmain.playerDrawToggle.put(uuid, true);
        }
    }

    public void savePlayerData(UUID uuid) {
        try {
            playerDataConfig.set("player." + uuid.toString() + ".drawores", main.genmain.playerDrawToggle.get(uuid));
            playerDataConfig.save(playerData);
        } catch (IOException e) {
            System.out.println(main.prefix + ChatColor.RED + "Could not save PlayerData.yml");
        }
    }

    /*
    This function reads all the ores in from the file by bit shifting and masking
     */
    public List<CustomOre> getChunkFromFile(World world, Chunk chunk) {
        List<CustomOre> ores = new ArrayList<>();
        String chunkFileString = "c." + chunk.getX() + "." + chunk.getZ();

        byte[] filedata;
        try {
            filedata = Files.readAllBytes(plugin.getDataFolder().toPath().resolve("ChunkData").resolve(world.getName()).resolve(chunkFileString + ".dat"));
        } catch (IOException e) {
            return null;
        }

        for (int i = 0; i < filedata.length; i++) {
            CustomOre ore = new CustomOre(main);
            if (main.genmain.getOreData(filedata[i]) != null) {
                ore.type = main.genmain.getOreData(filedata[i]).type;
                ore.x = (byte)((filedata[i + 1] & 0b11110000) >>> 4);
                ore.z = (byte)(filedata[i + 1] & 0b00001111);
                ore.y = filedata[i + 2];
                i += 2;
                ores.add(ore);
            }
        }
        return ores;
    }

    /*
    This function saves the ores to the file by shifting the bits and packing shorts and halfshorts into one byte
     */
    public void saveChunkToFiles(World world, Chunk chunk, List<CustomOre> ores) {
        File worldFolder = new File(plugin.getDataFolder() + "/ChunkData/" + world.getName());

        if (!worldFolder.exists()) {
            try {
                worldFolder.mkdirs();
                System.out.println(main.prefix + ChatColor.GREEN + world.getName() + " folder has been created");
            } catch (SecurityException e) {
            }
        }

        String chunkFileString = "c." + chunk.getX() + "." + chunk.getZ();
        File chunkFile = new File(plugin.getDataFolder() + File.separator + "ChunkData" + File.separator + world.getName(), chunkFileString + ".dat");
        chunkFile.delete();
        try {
            chunkFile.createNewFile();
        } catch (IOException e) {
            return;
        }

        DataOutputStream stream;
        try {
            stream = new DataOutputStream(Files.newOutputStream(plugin.getDataFolder().toPath().resolve("ChunkData").resolve(world.getName()).resolve(chunkFileString + ".dat")));
        } catch (IOException e) {
            return;
        }

        if (ores != null) {
            for (CustomOre ore : ores) {
                try {
                    stream.writeByte(main.genmain.getOreData(ore.type).oreid); // <- 1 byte
                    byte packed_xz = (byte) ((ore.x << 4) | ore.z);
                    stream.writeByte(packed_xz); //this has a max value of 15
                    stream.writeByte(ore.y);
                } catch (IOException e) {
                    return;
                }
            }
            try {
                stream.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}
