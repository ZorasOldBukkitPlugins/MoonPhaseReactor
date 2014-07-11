/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.lagopusempire.moonphasereactor.MetadataConstants.*;
import org.bukkit.Material;

/**
 *
 * @author MrZoraman
 */
public class MpBlockCommand implements CommandExecutor
{
    private final MetadataUtils metadataUtils;
    
    public MpBlockCommand(MetadataUtils metadataUtils)
    {
        this.metadataUtils = metadataUtils;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player) sender;
            
            if(args.length > 0)
            {
                if(args[0].equalsIgnoreCase("remove"))
                {
                    metadataUtils.setBoolean(player, IS_REMOVING, true);
                    player.sendMessage(ChatColor.GREEN + "Right click blocks to remove them. Type " + ChatColor.YELLOW + "/" + alias + " off" + ChatColor.GREEN + " to stop.");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("off"))
                {
                    metadataUtils.setBoolean(player, IS_REMOVING, false);
                    metadataUtils.setBoolean(player, IS_SELECTING, false);
                    player.sendMessage(ChatColor.GREEN + "Selection mode disabled.");
                    return true;
                }
            }
            
            boolean isSelecting = metadataUtils.getBoolean(player, IS_SELECTING);
            
            isSelecting = !isSelecting;

            if(isSelecting)
            {
                if(args.length < 3)
                {
                    player.sendMessage(ChatColor.RED + "Invalid amount of arguments!");
                    return false;
                }
                else
                {
                    MoonPhase moonPhase = MoonPhase.matchMoonPhase(args[0]);
                    Material normalMaterial = Material.matchMaterial(args[1]);
                    Material specialMaterial = Material.matchMaterial(args[2]);
                    
                    if(moonPhase == null)
                    {
                        player.sendMessage(ChatColor.RED + "Invalid moon phase!");
                    }
                    else if(normalMaterial == null)
                    {
                        player.sendMessage(ChatColor.RED + "Invalid normal material!");
                    }
                    else if(specialMaterial == null)
                    {
                        player.sendMessage(ChatColor.RED + "Invlaid special material!");
                    }
                    else
                    {
                        metadataUtils.setMetadata(player, CONDITION, moonPhase);
                        metadataUtils.setMetadata(player, NORMAL_MATERIAL, normalMaterial);
                        metadataUtils.setMetadata(player, SPECIAL_MATERIAL, specialMaterial);

                        player.sendMessage(ChatColor.GREEN + "Please select a block. To end selection mode, type " + ChatColor.YELLOW + "/" + alias + " off" + ChatColor.GREEN + ".");
                        metadataUtils.setBoolean(player, IS_SELECTING, true);
                    }
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You're already in selection mode! To end selection mode, type " + ChatColor.YELLOW + "/" + alias + " off" + ChatColor.RED + ".");
                return true;
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
        }
        
        return true;
    }
}
