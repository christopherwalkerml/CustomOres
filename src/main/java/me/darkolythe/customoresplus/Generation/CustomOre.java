package me.darkolythe.customoresplus.Generation;

import me.darkolythe.customoresplus.CustomOresPlus;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class CustomOre {

    public CustomOresPlus main;
    public CustomOre(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public String type = null;
    public byte x;
    public short y;
    public byte z;
    public EntityArmorStand packet = null;

    public void breakOre(Location worldloc, Chunk chunk, Player player) {
        if (player != null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (main.generationtools.isRequiredTool(item, main.genmain.getOreData(type))) {
                chunk.getWorld().dropItemNaturally(worldloc, main.generationtools.getSkullFromTexture(main.genmain.getOreData(type).id, main.genmain.getOreData(type).textures, type, main.genmain.getOreData(type).colour));
            }
        }
        if (packet != null) {
            PacketPlayOutEntityDestroy p = new PacketPlayOutEntityDestroy(packet.getId());
            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(p);
            }
        }
    }

    /*
    This function checks all 6 blocks around it randomly for air and returns the first one found or null if none
     */
    private Location getAirSides(Location worldloc, Chunk chunk) {
        World world = worldloc.getWorld();
        List<Integer> indexes = Arrays.asList(0, 1, 2, 3, 4, 5);
        Collections.shuffle(indexes);
        for (int i = 0; i < 6; i++) {
            int ran = indexes.get(i);
            Location checkloc = new Location(world, (int)worldloc.getX() + main.possibles.get(ran).get(0), (int)worldloc.getY() + main.possibles.get(ran).get(1), (int)worldloc.getZ() + main.possibles.get(ran).get(2));
            if (main.generationtools.isLocationInChunk(chunk, checkloc.getX() / 16, checkloc.getZ() / 16)) {
                Material block = world.getBlockAt(checkloc).getType();
                if (!block.isSolid()) {
                    return new Location(worldloc.getWorld(), (int) worldloc.getX() + main.possibles.get(ran).get(0), (int) worldloc.getY() + main.possibles.get(ran).get(1), (int) worldloc.getZ() + main.possibles.get(ran).get(2));
                }
            }
        }
        return null;
    }

    /*
    This function determines whether or not the ore will be drawn, and if so, the packet is generated in drawHeadFromEvent
     */
    public void draw(Location worldloc, Chunk chunk, Player player) {
        worldloc.setY((int) worldloc.getY());
        Location airloc = getAirSides(worldloc, chunk);
        if (airloc != null) {
            if (main.genmain.loadedChunks.contains(chunk)) {
                drawHeadFromEvent(worldloc, airloc, player);
            }
        }
    }

    /*
    This function creates the ore packet and calls sendOrePacket to display the ore in the world for the player
     */
    public void drawHeadFromEvent(Location worldloc, Location airloc, Player player) {
        if (packet == null) {
            worldloc.setX(worldloc.getX() + 0.5 + (airloc.getX() - worldloc.getX()) * 0.40);
            worldloc.setY(worldloc.getY() - .35 + (airloc.getY() - worldloc.getY()) * 0.40);
            worldloc.setZ(worldloc.getZ() + 0.5 + (airloc.getZ() - worldloc.getZ()) * 0.40);
            EntityArmorStand oreHead = new EntityArmorStand(((CraftWorld) worldloc.getWorld()).getHandle(), worldloc.getX(), worldloc.getY(), worldloc.getZ());
            oreHead.setInvisible(true);
            oreHead.setNoGravity(true);
            oreHead.setSmall(true);
            oreHead.setInvulnerable(true);
            oreHead.setMarker(true);
            oreHead.setHeadPose(new Vector3f(main.random.nextInt(90), main.random.nextInt(90), main.random.nextInt(90)));
            oreHead.setBodyPose(new Vector3f(0, main.random.nextInt(90), 0));
            if (main.generationtools.isxyLoadedChunk((int) (oreHead.locX / 16), (int) (oreHead.locZ / 16), worldloc.getWorld()) != null) {
                sendOrePacket(player, oreHead);
            }
            packet = oreHead;
        } else {
            sendOrePacket(player, packet);
        }
    }

    /*
    This function displays the ore in the world for the players in range
     */
    public void sendOrePacket(Player player, EntityArmorStand packet) {
        PacketPlayOutSpawnEntityLiving p = new PacketPlayOutSpawnEntityLiving(packet);
        PacketPlayOutEntityEquipment e = new PacketPlayOutEntityEquipment(packet.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(main.generationtools.getSkullFromTexture(main.genmain.getOreData(type).id,
                main.genmain.getOreData(type).textures, type, main.genmain.getOreData(type).colour)));
        if (player == null) {
            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(p);
                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(e);
            }
        } else {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(e);
        }
    }
}