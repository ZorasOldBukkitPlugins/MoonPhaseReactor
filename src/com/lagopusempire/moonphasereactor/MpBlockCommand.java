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
                    metadataUtils.setMetadata(player, "mpr_isRemoving", new Object());
                    player.sendMessage(ChatColor.GREEN + "Click a block to remove it.");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("off"))
                {
                    metadataUtils.removeMetadata(player, "mpr_isRemoving");
                    metadataUtils.setMetadata(player, "mpr_isSelecting", false);
                    player.sendMessage(ChatColor.GREEN + "Selection mode disabled.");
                    return true;
                }
            }
            
            boolean isSelecting;
            
            Boolean isSelecting_obj = (Boolean) metadataUtils.getMetadata(player, "mpr_isSelecting");
            if(isSelecting_obj == null)
            {
                isSelecting = true;//no questions. only tears.
            }
            else
            {
                isSelecting = !isSelecting_obj;
            }

            if(isSelecting)
            {
                if(args.length < 3)
                {
                    player.sendMessage(ChatColor.RED + "Invalid amount of arguments!");
                    return false;
                }
                else
                {
                    String condition = args[0];
                    String normalMaterial = args[1];
                    String specialMaterial = args[2];
                    
                    metadataUtils.setMetadata(player, "mpr_condition", condition);
                    metadataUtils.setMetadata(player, "mpr_normalMaterial", normalMaterial);
                    metadataUtils.setMetadata(player, "mpr_specialMaterial", specialMaterial);
                    
                    player.sendMessage(ChatColor.GREEN + "Please select a block.");
                    metadataUtils.setMetadata(player, "mpr_isSelecting", true);
                }
            }
            else
            {
                player.sendMessage(ChatColor.GREEN + "No longer selecting a block");
                metadataUtils.setMetadata(player, "mpr_isSelecting", false);
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
        }
        
        return true;
    }
}
