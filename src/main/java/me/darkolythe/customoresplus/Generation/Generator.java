package me.darkolythe.customoresplus.Generation;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Generator {

    public CustomOresPlus main;
    public Generator(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    /*
    loops through all the ores and tries to spawn chunks for each ore by generating veins equal to those specified in the inv
     */
    public void generateOres(Chunk chunk) {
        List<CustomOre> customores = new ArrayList<>();
        Random rand = new Random(chunk.getX() + chunk.getZ());
        for (OreData ore: main.genmain.allOreData) {
            if (ore.worlds.contains(chunk.getWorld().getName())) {
                for (int i = 0; i < ore.frequency; i++) {
                    int x = rand.nextInt(16);
                    int y = rand.nextInt(ore.ymax - ore.ymin) + ore.ymin;
                    int z = rand.nextInt(16);
                    short[] xyz = main.generationtools.invertLocation(new Location(chunk.getWorld(), (chunk.getX() * 16 + x), y, chunk.getZ() * 16 + z), (byte)x, (short)y, (byte)z);
                    Location location = new Location(chunk.getWorld(), xyz[0], xyz[1], xyz[2]);
                    List<CustomOre> vein = populateVein(ore.type, location, rand, false);
                    customores = loopOres(vein, chunk, customores);
                }
            }
        }
        Collections.sort(customores, new CustomOresCompare());
        main.genmain.setCustomOre(chunk.getWorld(), chunk, customores);
    }

    /*
    This function gets the original position of the ore, then branches out and places more at random
     */
    private ArrayList<CustomOre> populateVein(String type, Location loc, Random rand, boolean manual) {
        ArrayList<CustomOre> vein = new ArrayList<>();
        CustomOre ore = new CustomOre(main);
        ore.type = type;
        ore.x = (byte)loc.getX();
        ore.y = (short)loc.getY();
        ore.z = (byte)loc.getZ();
        vein.add(ore);
        while (vein.size() < main.genmain.getOreData(ore.type).veinsize) {
            List<Integer> indexes = Arrays.asList(0, 1, 2, 3, 4, 5);
            Collections.shuffle(indexes);
            for (int i = 0; i < min(6, main.genmain.getOreData(ore.type).veinsize - vein.size() + 1); i++) {
                ore = new CustomOre(main);
                ore.type = type;
                int ran = rand.nextInt(vein.size());
                int randint = indexes.get(i);
                ore.x = (byte) main.generationtools.clamp((int) vein.get(ran).x + main.possibles.get(randint).get(0), 0, 15);
                if (!manual) {
                    ore.y = (short) main.generationtools.clamp(((int) vein.get(ran).y + main.possibles.get(randint).get(1)), main.genmain.getOreData(ore.type).ymin, main.genmain.getOreData(ore.type).ymax);
                } else {
                    ore.y = (short) (vein.get(ran).y + main.possibles.get(randint).get(1));
                }
                ore.z = (byte) main.generationtools.clamp((int) vein.get(ran).z + main.possibles.get(randint).get(2), 0, 15);
                boolean add = true;
                for (CustomOre ores : vein) {
                    if (ore.x == ores.x && ore.y == ores.y && ore.z == ores.z) {
                        add = false;
                    }
                }
                if (add) {
                    vein.add(ore);
                }
            }
        }
        return vein;
    }

    /*
    checks if the current location is a valid location for that ore (checks based on allowed blocks from the CustomOres.yml
     */
    private boolean isValidOreLocation(Material block, String type) {
        for (String blocks: main.genmain.getOreData(type).blocks) {
            if (Material.getMaterial(blocks) == block) {
                return true;
            }
        }
        return false;
    }

    /*
    generates ore via command. does the same thing as a chunk generator, but just for one ore/vein
     */
    public void generateOresManually(Location location, String type, String veintype) {
        Chunk chunk = location.getChunk();
        Location worldloc = location.clone();
        List<CustomOre> customores = main.genmain.getCustomOre(location.getWorld(), chunk);
        location.setX(abs((int) location.getX() % 16));
        location.setZ(abs((int) location.getZ() % 16));
        location.setY((int) location.getY());
        short[] xyz = main.generationtools.invertLocation(worldloc, (byte)location.getX(), (short)location.getY(), (byte)location.getZ());
        List<CustomOre> vein = new ArrayList<>();
        if (veintype.equals("ore")) {
            CustomOre ore = new CustomOre(main);
            ore.type = type;
            ore.x = (byte) xyz[0];
            ore.y = xyz[1];
            ore.z = (byte) xyz[2];
            vein.add(ore);
        } else {
            location = new Location(worldloc.getWorld(), xyz[0], xyz[1], xyz[2]);
            vein = populateVein(type, location, main.random, true);
        }
        for (CustomOre ore : vein) {
            chunk.getBlock((int)ore.x, (int)ore.y, (int)ore.z).setType(Material.STONE);
            ore.type = type;
            drawAllPlayerOre(chunk, ore);
            xyz = main.generationtools.invertLocation(worldloc, ore.x, ore.y, ore.z);
            ore.x = (byte)xyz[0];
            ore.y = xyz[1];
            ore.z = (byte)xyz[2];
            customores.add(ore);
        }
        Collections.sort(customores, new CustomOresCompare());
        main.genmain.setCustomOre(location.getWorld(), chunk, customores);
    }

    /*
    checks if every ore in a vein is valid
     */
    private List<CustomOre> loopOres(List<CustomOre> vein, Chunk chunk, List<CustomOre> customores) {
        for (CustomOre ore: vein) {
            if (isValidOreLocation(chunk.getBlock((int) ore.x, (int) ore.y, (int) ore.z).getType(), ore.type)) {
                Location worldloc = new Location(chunk.getWorld(), chunk.getX() * 16 + ore.x, ore.y, chunk.getZ() * 16 + ore.z);
                short[] xyz = main.generationtools.invertLocation(worldloc, ore.x, ore.y, ore.z);
                ore.x = (byte)xyz[0];
                ore.y = xyz[1];
                ore.z = (byte)xyz[2];
                customores.add(ore);
            }
        }
        return customores;
    }

    /*
    draws all the ores in a chunk
     */
    public void drawChunkOres(List<CustomOre> ores, Chunk chunk, Player player) {
        for (CustomOre ore: ores) {
            Location worldloc = new Location(chunk.getWorld(), chunk.getX() * 16 + ore.x, ore.y, chunk.getZ() * 16 + ore.z);
            short[] xyz = main.generationtools.invertLocation(worldloc, ore.x, ore.y, ore.z);
            worldloc = new Location(chunk.getWorld(), chunk.getX() * 16 + xyz[0], xyz[1], chunk.getZ() * 16 + xyz[2]);
            ore.draw(worldloc, chunk, player);
        }
    }

    /*
    draws all the ores in a certain chunk for a player
     */
    public void drawAllPlayerOre(Chunk chunk, CustomOre ore) {
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            ore.draw(new Location(chunk.getWorld(), chunk.getX() * 16 + ore.x, ore.y, chunk.getZ() * 16 + ore.z), chunk, player);
        }
    }
}
