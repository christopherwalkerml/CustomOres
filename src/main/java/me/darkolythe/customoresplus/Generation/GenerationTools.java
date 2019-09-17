package me.darkolythe.customoresplus.Generation;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.darkolythe.customoresplus.CustomOresPlus;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

import static java.lang.Math.abs;

public class GenerationTools {

    public CustomOresPlus main;
    public GenerationTools(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public Long generateSeed() {
        String seed;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomLimitedInt = random.nextInt(10) + 48;
            buffer.append((char) randomLimitedInt);
        }
        seed = buffer.toString();

        return Long.parseLong(seed);
    }

    public int clamp(int val, int min, int max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        }
    }

    public ItemStack getSkullFromTexture(String id, String textures, String type, String colour) {
        ItemStack oreStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) oreStack.getItemMeta();

        GameProfile gameProfile = new GameProfile(UUID.fromString(id), null);

        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", textures).getBytes());
        gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField;

        try {
            profileField = sm.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(sm, gameProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        sm.setDisplayName(ChatColor.valueOf(colour) + type + " Ore");
        sm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        oreStack.setItemMeta(sm);

        return oreStack;
    }

    /*
    for negative coordinates, the chunk location needs to be inverted to correctly draw and spawn ores
     */
    public short[] invertLocation(Location worldloc, byte x, short y, byte z) {
        if (worldloc.getX() < 0) {
            x = (byte)(16 - x - 1);
        }
        if (worldloc.getZ() < 0) {
            z = (byte)(16 - z -1);
        }
        short[] xyz = {x, y, z};
        return xyz;
    }

    /*
    checks if an x coord and z coord are inside the specified chunk
     */
    public boolean isLocationInChunk(Chunk chunk, double x, double z) {
        if (chunk.getX() >= 0) {
            if (x < chunk.getX() || x >= chunk.getX() + 1) {
                return false;
            }
        } else {
            if (x > chunk.getX() + 1 || x <= chunk.getX()) {
                return false;
            }
        }
        if (chunk.getZ() >= 0) {
            if (z < chunk.getZ() || z >= chunk.getZ() + 1) {
                return false;
            }
        } else {
            if (z > chunk.getZ() + 1 || z <= chunk.getZ()) {
                return false;
            }
        }
        return true;
    }

    /*
    This function uses a searching argorithm to help check for ores when updating nearby ore blocks
     */
    public CustomOre searchChunkForOre(Chunk chunk, Location worldloc) {
        Location blockloc = new Location(worldloc.getWorld(), abs(worldloc.getX()) % 16, worldloc.getY(), abs(worldloc.getZ()) % 16);
        Location chunkloc = new Location(worldloc.getWorld(), worldloc.getX() / 16, worldloc.getY(), worldloc.getZ() / 16);

        if (isLocationInChunk(chunk, chunkloc.getX(), chunkloc.getZ())) {
            if (main.genmain.allCustomOres.containsKey(chunk.getWorld())) {
                if (main.genmain.allCustomOres.get(chunk.getWorld()).containsKey(chunk)) {

                    List<CustomOre> customOres = main.genmain.getCustomOre(chunk.getWorld(), chunk);

                    int first = 0;
                    int last = customOres.size() - 1;
                    boolean found = false;
                    int midpoint = 0;

                    while (first <= last && !found) {
                        midpoint = (first + last) / 2;
                        if (customOres.get(midpoint).y == blockloc.getY()) {
                            found = true;
                        } else {
                            if (blockloc.getY() < customOres.get(midpoint).y) {
                                last = midpoint - 1;
                            } else {
                                first = midpoint + 1;
                            }
                        }
                    }
                    int left = midpoint;
                    int right = midpoint;
                    if (found) {
                        while (!checkBlock(right, blockloc, customOres) && customOres.get(right).y == blockloc.getY() && right < customOres.size() - 1) {
                            right += 1;
                        }
                        if (checkBlock(right, blockloc, customOres)) {
                            return customOres.get(right);
                        }
                        while (!checkBlock(left, blockloc, customOres) && customOres.get(left).y == blockloc.getY() && left > 0) {
                            left -= 1;
                        }
                        if (checkBlock(left, blockloc, customOres)) {
                            return customOres.get(left);
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
    checks if the block is the searched for block
     */
    private boolean checkBlock(int pos, Location blockloc, List<CustomOre> customOres) {
        if (customOres.get(pos).x == blockloc.getX() && customOres.get(pos).y == blockloc.getY() && customOres.get(pos).z == blockloc.getZ()) {
            return true;
        }
        return false;
    }

    /*
    undraws all the ores for a certain player if toggled or despawned
     */
    public void unDrawOres(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                for (World world : main.genmain.allCustomOres.keySet()) {
                    Iterator<Chunk> c =  main.genmain.allCustomOres.get(world).keySet().iterator();
                    Chunk chunk;
                    while(c.hasNext()) {
                        chunk = c.next();
                        for (CustomOre ore : main.genmain.getCustomOre(chunk.getWorld(), chunk)) {
                            unDrawOre(ore, player);
                        }
                    }
                }
            }
        });
    }

    /*
    undraws a certain ore when mined or mass despawning
     */
    public void unDrawOre(CustomOre ore, Player player) {
        if (ore.packet != null) {
            PacketPlayOutEntityDestroy p = new PacketPlayOutEntityDestroy(ore.packet.getId());
            if (player == null) {
                for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                    ((CraftPlayer) players).getHandle().playerConnection.sendPacket(p);
                }
            } else {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
            }
        }
    }

    public String intToDHM(int num) {
        /*
         * This method turns a number into Days, Hours, Minutse format
         */
        StringBuilder str = new StringBuilder(32);
        str.append(num / 60000);
        str.append(" Minutes ");
        num %= 60000;
        str.append(num / 1000);
        str.append(" Seconds ");
        return str.toString();
    }

    /*
    checks if coord x and coord z are in a loaded chunk
     */
    public Chunk isxyLoadedChunk(int x, int z, World world) {
        for (Chunk chunk : main.genmain.loadedChunks) {
            if ((chunk.getX() == x) && (chunk.getZ() == z) && (chunk.getWorld() == world)) {
                return chunk;
            }
        }
        return null;
    }

    public boolean isRequiredTool(ItemStack tool, OreData oredata) {
        if (oredata.requiredtier != 0) {
            if (tool != null) {
                if (tool.hasItemMeta()) {
                    if (tool.getItemMeta().hasDisplayName()) {
                        String name = tool.getItemMeta().getDisplayName();
                        for (Integer tier : main.genmain.tooltiers.keySet()) {
                            if (main.genmain.tooltiers.get(tier).equals(name)) {
                                if (tier >= oredata.requiredtier) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
