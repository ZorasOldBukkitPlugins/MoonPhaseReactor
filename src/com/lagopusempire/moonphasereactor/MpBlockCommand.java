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
                    metadataUtils.setBoolean(player, IS_SELECTING, false);
                    player.sendMessage(ChatColor.GREEN + "Right click blocks to remove them. Type " + ChatColor.YELLOW + "/" + alias + " off" + ChatColor.GREEN + " to stop.");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("done"))
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
                    boolean inferringNormal = false;
                    boolean inferringSpecial = false;
                    
                    if(args[1].equalsIgnoreCase("infer"))
                    {
                        inferringNormal = true;
                    }
                    
                    if(args[2].equalsIgnoreCase("infer"))
                    {
                        inferringSpecial = true;
                    }
                    
                    if(inferringNormal && inferringSpecial)
                    {
                        player.sendMessage(ChatColor.RED + "You can't infer both the normal and special material!");
                        return true;
                    }
                    
                    if(inferringNormal)
                    {
                        metadataUtils.setBoolean(player, INFERRING_NORMAL, true);
                    }
                    else
                    {
                        metadataUtils.setBoolean(player, INFERRING_NORMAL, false);
                    }
                    
                    if(inferringSpecial)
                    {
                        metadataUtils.setBoolean(player, INFERRING_SPECIAL, true);
                    }
                    else
                    {
                        metadataUtils.setBoolean(player, INFERRING_SPECIAL, false);
                    }
                    
                    MoonPhase moonPhase = MoonPhase.matchMoonPhase(args[0]);
                    Material normalMaterial = Material.matchMaterial(args[1]);
                    Material specialMaterial = Material.matchMaterial(args[2]);
                    
                    if(moonPhase == null)
                    {
                        player.sendMessage(ChatColor.RED + "Invalid moon phase!");
                    }
                    else if(!inferringNormal && normalMaterial == null)
                    {
                        player.sendMessage(ChatColor.RED + "Invalid normal material!");
                    }
                    else if(!inferringSpecial && specialMaterial == null)
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
                        metadataUtils.setBoolean(player, IS_REMOVING, false);
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
