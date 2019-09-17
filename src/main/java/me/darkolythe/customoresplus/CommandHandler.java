package me.darkolythe.customoresplus;

import me.darkolythe.customoresplus.Achievements.AdvancementData;
import me.darkolythe.customoresplus.Generation.OreData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandHandler implements CommandExecutor {

    private CustomOresPlus main = CustomOresPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("customoresplus.command")) {
            if (cmd.getName().equalsIgnoreCase("customoresplus")) { //if the player has permission, and the command is right
                if (args.length == 0) {
                    player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus " + main.maintools.getSubCommands(player));
                } else {
                    if (args[0].equalsIgnoreCase("create")) { //if there's more than one argument, and it's cooldown
                        if (player.hasPermission("customoresplus.create")) {
                            if (main.genmain.worlds.contains(player.getWorld().getName())) {
                                if (args.length == 1) {
                                    player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus create ore/vein/chunk");
                                } else if (args[1].equalsIgnoreCase("ore") || args[1].equalsIgnoreCase("vein")) {
                                    if (args.length == 2) {
                                        player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus create ore/vein type");
                                    } else {
                                        for (OreData ore : main.genmain.allOreData) {
                                            if (ore.type.equalsIgnoreCase(args[2])) {
                                                main.generator.generateOresManually(player.getLocation(), ore.type, args[1]);
                                                player.sendMessage(main.prefix + ChatColor.GREEN + "Successfully spawned " + args[2] + " " + args[1]);
                                                return true;
                                            }
                                        }
                                        player.sendMessage(main.prefix + ChatColor.RED + "Invalid Ore Type: " + args[2]);
                                    }
                                } else if (args[1].equalsIgnoreCase("chunk")) {
                                    main.generator.generateOres(player.getLocation().getChunk());
                                    player.sendMessage(main.prefix + ChatColor.GREEN + "Successfully spawned ores in chunk");
                                } else if (!(args[1].equalsIgnoreCase("ore")) && !(args[1].equalsIgnoreCase("vein")) && !(args[1].equalsIgnoreCase("chunk"))) {
                                    player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus create " + args[1].toLowerCase() + " type");
                                } else {
                                    player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus create ore/vein/chunk");
                                }
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "This world has ores disabled!");
                            }
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + "You do not have permission to use that command!");
                        }
                    } else if (args[0].equalsIgnoreCase("toggledraw")) {
                        if ((System.currentTimeMillis() - main.genmain.getToggleCooldown(player)) >= main.genmain.cooldown) {
                            main.genmain.playerDrawToggle.put(player.getUniqueId(), !main.genmain.getToggleDraw(player));
                            if (main.genmain.playerDrawToggle.get(player.getUniqueId())) {
                                player.sendMessage(main.prefix + ChatColor.GREEN + "CustomOres will now be visible!");
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "CustomOres will no longer be visible!");
                                main.generationtools.unDrawOres(player);
                                main.genmain.playerToggleCooldown.put(player.getUniqueId(), System.currentTimeMillis());
                                main.genmain.getPlayerDrawChunks(player, player.getWorld()).clear();
                            }
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + "You can toggle ore drawing again in " + main.generationtools.intToDHM((int) (main.genmain.cooldown + main.genmain.getToggleCooldown(player) - System.currentTimeMillis())));
                        }
                    } else if (args[0].equalsIgnoreCase("recipe")) {
                        if (player.hasPermission("customoresplus.recipe")) {
                            if (args.length == 1) {
                                main.craftingguicreator.recipeListGUI(player, (byte) 0, "");
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus " + main.maintools.getSubCommands(player));
                            }
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + "You do not have permission to use that command!");
                        }
                    } else if(args[0].equalsIgnoreCase("item")) {
                        if (player.hasPermission("customoresplus.item")) {
                            if (args.length == 1) {
                                main.itemcreatorgui.getGUI(player, player.getInventory().getItemInMainHand());
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus " + main.maintools.getSubCommands(player));
                            }
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + "You do not have permission to use that command!");
                        }
                    } else if (args[0].equalsIgnoreCase("book") || args[0].equalsIgnoreCase("compendium")) {
                        if (player.hasPermission("customoresplus.book")) {
                            main.infoguimain.giveInfoBook(player);
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + "You do not have permission to use that command!");
                        }
                    } else if (args[0].equalsIgnoreCase("advancement")) {
                        if (player.hasPermission("customoresplus.advancementadmin")) {
                            if (args.length == 4) {
                                String name = ChatColor.translateAlternateColorCodes('&', args[2].replace("_", " "));
                                String plr = args[3];
                                if (args[1].equalsIgnoreCase("remove")) {
                                    for (Player p: Bukkit.getServer().getOnlinePlayers()) {
                                        if (plr.toLowerCase().equalsIgnoreCase(p.getName())) {
                                            for (String n : main.advancementmain.playerAdvancements.get(p.getUniqueId())) {
                                                if (n.equals(name)) {
                                                    main.advancementmain.playerAdvancements.get(p.getUniqueId()).remove(n);
                                                    player.sendMessage(main.prefix + ChatColor.GREEN + "Successfully removed " + name + ChatColor.GREEN + " from " + p.getName());
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                    player.sendMessage(main.prefix + ChatColor.RED + "Could not remove " + name + ChatColor.RED + " from " + plr);
                                } else if (args[1].equalsIgnoreCase("give")) {
                                    for (Player p: Bukkit.getServer().getOnlinePlayers()) {
                                        if (plr.toLowerCase().equalsIgnoreCase(p.getName())) {
                                            player.sendMessage(main.prefix + ChatColor.GREEN + "Successfully gave " + name + ChatColor.GREEN + " to " + p.getName());
                                            if (main.advancementmain.playerAdvancements.containsKey(p.getUniqueId())) {
                                                main.advancementmain.playerAdvancements.get(p.getUniqueId()).add(name);
                                            } else {
                                                main.advancementmain.playerAdvancements.put(p.getUniqueId(), new ArrayList<>());
                                                main.advancementmain.playerAdvancements.get(p.getUniqueId()).add(name);
                                            }
                                            return true;
                                        }
                                    }
                                    player.sendMessage(main.prefix + ChatColor.GREEN + "Could not give " + name + ChatColor.GREEN + " to " + plr);
                                } else {
                                    player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus advancement remove/give name_name player");
                                }
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus advancement remove/give name_name player");
                            }
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + "You do not have permission to use that command!");
                        }
                    } else {
                        player.sendMessage(main.prefix + ChatColor.RED + "Invalid Arguments: /customoresplus " + main.maintools.getSubCommands(player));
                    }
                }
            }
        }
        return true;
    }
}
