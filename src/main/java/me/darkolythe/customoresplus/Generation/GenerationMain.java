package me.darkolythe.customoresplus.Generation;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class GenerationMain {

    public List<OreData> allOreData = new ArrayList<>();
    public Map<World, Map<Chunk, List<CustomOre>>> allCustomOres = new HashMap<>();
    public Map<UUID, Boolean> playerDrawToggle = new HashMap<>();
    public Map<UUID, Long> playerToggleCooldown  = new HashMap<>();
    public Integer cooldown;
    public Long seed;
    public List<String> worlds = new ArrayList<>();
    public List<Chunk> loadedChunks = new ArrayList<>();
    public List<Chunk> generatedChunks = new ArrayList<>();
    public Map<UUID, Map<World, List<Chunk>>> playerDrawnChunks = new HashMap<>();
    public Map<Integer, String> tooltiers = new HashMap<>();

    public CustomOresPlus main;
    public GenerationMain(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public OreData getOreData(String type) {
        for (OreData oredata: allOreData) {
            if (oredata.type.equals(type)) {
                return oredata;
            }
        }
        return null;
    }

    public OreData getOreData(short oreid) {
        for (OreData oredata: allOreData) {
            if (oredata.oreid == oreid) {
                return oredata;
            }
        }
        return null;
    }

    public void setCustomOre(World world, Chunk chunk, List<CustomOre> ores) {
        if (allCustomOres.containsKey(world)) {
            allCustomOres.get(world).put(chunk, ores);
        } else {
            allCustomOres.put(world, new HashMap<>());
            allCustomOres.get(world).put(chunk, ores);
        }
    }

    public Boolean getToggleDraw(Player player) {
        if (playerDrawToggle.containsKey(player.getUniqueId())) {
            return playerDrawToggle.get(player.getUniqueId());
        } else {
            playerDrawToggle.put(player.getUniqueId(), true);
            return true;
        }
    }

    public Long getToggleCooldown(Player player) {
        if (playerToggleCooldown.containsKey(player.getUniqueId())) {
            return playerToggleCooldown.get(player.getUniqueId());
        } else {
            playerToggleCooldown.put(player.getUniqueId(), System.currentTimeMillis() - cooldown);
            return playerToggleCooldown.get(player.getUniqueId());
        }
    }

    public List<CustomOre> getCustomOre(World world, Chunk chunk) {
        if (allCustomOres.containsKey(world)) {
            if (allCustomOres.get(world).containsKey(chunk)) {
                return allCustomOres.get(world).get(chunk);
            } else {
                allCustomOres.get(world).put(chunk, new ArrayList<>());
                return allCustomOres.get(world).get(chunk);
            }
        } else {
            allCustomOres.put(world, new HashMap<>());
            return getCustomOre(world, chunk);
        }
    }

    public List<Chunk> getPlayerDrawChunks(Player player, World world) {
        if (playerDrawnChunks.containsKey(player.getUniqueId())) {
            if (playerDrawnChunks.get(player.getUniqueId()).containsKey(world)) {
                return playerDrawnChunks.get(player.getUniqueId()).get(world);
            } else {
                playerDrawnChunks.get(player.getUniqueId()).put(world, new ArrayList<>());
                return playerDrawnChunks.get(player.getUniqueId()).get(world);
            }
        } else {
            playerDrawnChunks.put(player.getUniqueId(), new HashMap<>());
            return getPlayerDrawChunks(player, world);
        }
    }

    /*
    This function checks chunks in a 7x7 around the player. If a chunk isnt drawn, it will be drawn
    If a chunk is outside the 7x7, it will be undrawn
     */
    public void generateCustomOres() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                Iterator<Chunk> chunks = loadedChunks.iterator();
                Chunk c;
                while (chunks.hasNext()) {
                    c = chunks.next();
                    if (!generatedChunks.contains(c)) {
                        main.generator.generateOres(c);
                        generatedChunks.add(c);
                    }
                }
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    List<Chunk> currentChunks = new ArrayList<>();
                    if (getToggleDraw(player)) {
                        for (int x = -3; x < 4; x++) {
                            for (int z = -3; z < 4; z++) {
                                Chunk chunk = main.generationtools.isxyLoadedChunk(((int)(player.getLocation().getX() / 16) + x), ((int)(player.getLocation().getZ() / 16) + z), player.getWorld());
                                if (chunk != null) {
                                    currentChunks.add(chunk);
                                    if (!getPlayerDrawChunks(player, player.getWorld()).contains(chunk)) {
                                        getPlayerDrawChunks(player, player.getWorld()).add(chunk);
                                        main.generator.drawChunkOres(getCustomOre(chunk.getWorld(), chunk), chunk, player);
                                    }
                                }
                            }
                        }
                    }
                    Iterator<Chunk> i = getPlayerDrawChunks(player, player.getWorld()).iterator();
                    Chunk chunk;
                    while (i.hasNext()) {
                        chunk = i.next();
                        if (!currentChunks.contains(chunk)) {
                            for (CustomOre ore: getCustomOre(chunk.getWorld(), chunk)) {
                                main.generationtools.unDrawOre(ore, player);
                            }
                            i.remove();
                        }
                    }
                }
            }
        }, 0L, 100L);
    }

}
