package me.darkolythe.customoresplus.Generation;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static java.lang.Math.abs;

public class GenerationListener implements Listener {

    public CustomOresPlus main;
    public GenerationListener(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    /*
    On Chunk Load, it will add all the chunks to a list to iterate through later. If ores are spawned on chunk load,
    It will spawn an ore, load the same chunk, spawn an ore, load the same chunk, etc etc
     */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        loadChunk(event.getChunk());
    }

    public void loadChunk(Chunk chunk) {
        main.genmain.loadedChunks.add(chunk);
        List<CustomOre> ores = main.generationconfighandler.getChunkFromFile(chunk.getWorld(), chunk);
        if (ores != null) {
            main.genmain.getCustomOre(chunk.getWorld(), chunk);
            main.genmain.allCustomOres.get(chunk.getWorld()).put(chunk, ores);
            main.genmain.generatedChunks.add(chunk);
        }
    }

    /*
    On chunk unload, the chunk is removed from the lists, and saved to a file
     */
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (main.genmain.worlds.contains(event.getWorld().getName())) {
            if (main.genmain.allCustomOres.containsKey(event.getWorld())) {
                main.generationconfighandler.saveChunkToFiles(event.getWorld(), event.getChunk(), main.genmain.allCustomOres.get(event.getWorld()).get(event.getChunk()));
                main.genmain.allCustomOres.get(event.getWorld()).remove(event.getChunk());
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    if (main.genmain.playerDrawnChunks.containsKey(player)) {
                        if (main.genmain.playerDrawnChunks.get(player).containsKey(event.getWorld())) {
                            main.genmain.playerDrawnChunks.get(player).get(event.getWorld()).remove(event.getChunk());
                        }
                    }
                }
            }
        }
        main.genmain.loadedChunks.remove(event.getChunk());
    }

    /*
    Draw all the ores on player join if they have drawores enabled
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        main.genmain.getToggleCooldown(event.getPlayer());
        main.generationconfighandler.getPlayerData(event.getPlayer().getUniqueId());
        main.genmain.getPlayerDrawChunks(event.getPlayer(), event.getPlayer().getWorld());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        main.generationconfighandler.savePlayerData(event.getPlayer().getUniqueId());
    }

    /*
    On teleport, the chunks must be drawn for the player
     */
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (main.genmain.playerDrawnChunks.containsKey(event.getPlayer())) {
            for (World world : main.genmain.playerDrawnChunks.get(event.getPlayer()).keySet()) {
                List<Chunk> chunks = main.genmain.getPlayerDrawChunks(event.getPlayer(), world);
                chunks.clear();
            }
        }
    }

    /*
    On world change, old chunks must be undrawn and new chunks must be drawn
     */
    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        main.genmain.getPlayerDrawChunks(event.getPlayer(), event.getFrom()).clear();
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            if (!event.getPlayer().getPotionEffect(PotionEffectType.SLOW_DIGGING).hasParticles() && !event.getPlayer().getPotionEffect(PotionEffectType.SLOW_DIGGING).hasIcon()) {
                event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Location oreloc = block.getLocation();
            if (oreloc.getX() < 0) {
                oreloc.setX(oreloc.getX() + 1);
            }
            if (oreloc.getZ() < 0) {
                oreloc.setZ(oreloc.getZ() + 1);
            }
            CustomOre customore = main.generationtools.searchChunkForOre(block.getChunk(), oreloc);
            ItemStack item = event.getItem();
            if (customore != null) {
                if (!main.generationtools.isRequiredTool(item, main.genmain.getOreData(customore.type))) {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1200, 2, false, false, false));
                }
            }
        }
    }

    /*
    On block break, the blocks around the broken block are checked for ores to display
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            eventHandler(event.getBlock(), "Break", event.getPlayer());
            if (event.getBlock().getType() == Material.PLAYER_HEAD || event.getBlock().getType() == Material.PLAYER_WALL_HEAD) {
                for (OreData data : main.genmain.allOreData) {
                    if ((((Skull)event.getBlock().getState()).getOwningPlayer()) != null) {
                        if (UUID.fromString(data.id).equals(((Skull)event.getBlock().getState()).getOwningPlayer().getUniqueId())) {
                            event.setDropItems(false);
                            ItemStack oreStack = main.generationtools.getSkullFromTexture(data.id, data.textures, data.type, data.colour);
                            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), oreStack);
                            return;
                        }
                    }
                }
            }
        }
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.GOLDEN_PICKAXE && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                for (String l: meta.getLore()) {
                    if (l.contains("Mines everything a diamond pickaxe can")) {
                        item.setType(Material.DIAMOND_PICKAXE);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                            @Override
                            public void run() {
                                item.setType(Material.GOLDEN_PICKAXE);
                            }
                        }, 1);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!event.isCancelled()) {
            eventHandler(event.getBlock(), "Burn", null);
        }
    }

    /*
    On block explode, it goes through all the blocks in range, and if an ore that is blast resistant is included, it wont
    explode
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            Iterator<Block> i = event.blockList().iterator();
            Block block;
            while(i.hasNext()) {
                block = i.next();
                if (!eventHandler(block, "Explode", null)) {
                    i.remove();
                }
            }
        }
    }

    /*
    Pistons cant pull ores
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetractBreak(BlockPistonRetractEvent event) {
        if (!event.isCancelled()) {
            for (Block block : event.getBlocks()) {
                if (!eventHandler(block, "PistonRetract", null)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /*
    Pistons cant push ores
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtendBreak(BlockPistonExtendEvent event) {
        if (!event.isCancelled()) {
            for (Block block : event.getBlocks()) {
                if (!eventHandler(block, "PistonExtend", null)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /*
    This function handles the above events
     */
    private boolean eventHandler(Block block, String eventType, Player player) {
        Location blockloc = block.getLocation();
        Chunk chunk = blockloc.getChunk();
        List<CustomOre> customOres = main.genmain.getCustomOre(chunk.getWorld(), chunk);
        if (customOres != null) {
            if (blockloc.getX() < 0) {
                blockloc.setX(blockloc.getX() + 1);
            }
            if (blockloc.getZ() < 0) {
                blockloc.setZ(blockloc.getZ() + 1);
            }
            Location oreloc = new Location(chunk.getWorld(), (float) abs((int) blockloc.getX() % 16), blockloc.getY(), (float) abs((int) blockloc.getZ()) % 16);
            switch (eventType) {
                case "Break":
                case "Burn":
                case "Explode":
                    breakOreBlock(customOres, blockloc, block, eventType, player);
                case "PistonRetract":
                case "PistonExtend":
                    if (cancelCustomOrePiston(customOres, oreloc)) {
                        return false;
                    }
            }
            updateNearbyBlocks(blockloc);
        }
        return true;
    }

    /*
    This function handles breaking ore blocks, removing the ore from the master list, etc
     */
    private void breakOreBlock(List<CustomOre> customOres, Location oreloc, Block block, String eventType, Player player) {
        CustomOre customore = main.generationtools.searchChunkForOre(block.getLocation().getChunk(), oreloc);
        while (customore != null) {
            if (eventType.equals("Explode")) {
                if (!main.genmain.getOreData(customore.type).blastResistant) {
                    customore.breakOre(block.getLocation(), block.getLocation().getChunk(), player);
                    customOres.remove(customore);
                }
            } else {
                customore.breakOre(block.getLocation(), block.getLocation().getChunk(), player);
                customOres.remove(customore);
            }
            customore = main.generationtools.searchChunkForOre(block.getLocation().getChunk(), oreloc);
        }
    }

    /*
    This function updates the 6 blocks adjacent to it to draw ores if the broken block was an ore
     */
    private void updateNearbyBlocks(Location blockloc) {
        for (List<Integer> possbility : main.possibles) {
            Location oreloc = new Location(blockloc.getWorld(), blockloc.getX() + possbility.get(0), blockloc.getY() + possbility.get(1), blockloc.getZ() + possbility.get(2));
            CustomOre ore = main.generationtools.searchChunkForOre(oreloc.getChunk(), oreloc);
            if (ore != null) {
                Location temploc = blockloc.clone();
                if (blockloc.getX() < 0) {
                    temploc.setX(blockloc.getX() - 2.5);
                }
                if (blockloc.getZ() < 0) {
                    temploc.setZ(blockloc.getZ() - 2.5);
                }
                ore.drawHeadFromEvent(oreloc, temploc, null);
            }
        }
    }

    private boolean cancelCustomOrePiston(List<CustomOre> customOres, Location oreloc) {
        for (CustomOre ore  : customOres) {
            if (ore.x == oreloc.getX() && ore.y == oreloc.getY() && ore.z == oreloc.getZ()) {
                return true;
            }
        }
        return false;
    }
}
