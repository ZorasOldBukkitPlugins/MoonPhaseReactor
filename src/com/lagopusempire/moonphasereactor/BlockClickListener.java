/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import com.avaje.ebean.EbeanServer;

import static com.lagopusempire.moonphasereactor.MetadataConstants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author MrZoraman
 */
public class BlockClickListener implements Listener
{
    private final MetadataUtils metadataUtils;
    private final EbeanServer database;
    private final BlockManager blockManager;
    
    public BlockClickListener(MetadataUtils metadataUtils, EbeanServer database, BlockManager blockManager)
    {
        this.metadataUtils = metadataUtils;
        this.database = database;
        this.blockManager = blockManager;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Player player = event.getPlayer();
            
            Block block = event.getClickedBlock();
                
            if(metadataUtils.getBoolean(player, IS_REMOVING))
            {
                int x = block.getX();
                int y = block.getY();
                int z = block.getZ();

                BlockData data = database.find(BlockData.class).where()
                        .ieq("x", String.valueOf(x))
                        .ieq("y", String.valueOf(y))
                        .ieq("z", String.valueOf(z))
                        .findUnique();

                if(data == null)
                {
                    player.sendMessage(ChatColor.RED + "That block is not registered!");
                }
                else
                {
                    database.delete(data);
                    blockManager.removeBlock(data);
                    player.sendMessage(ChatColor.GREEN + "Block removed!");
                }

                return;
            }
            
            boolean isSelecting = metadataUtils.getBoolean(player, IS_SELECTING);
            if(isSelecting)
            {
                Material normalMaterial = (Material) metadataUtils.getMetadata(player, NORMAL_MATERIAL);
                Material specialMaterial = (Material) metadataUtils.getMetadata(player, SPECIAL_MATERIAL);
                MoonPhase moonPhase = (MoonPhase) metadataUtils.getMetadata(player, CONDITION);
                
                if(normalMaterial == null || specialMaterial == null || moonPhase == null)
                {
                    player.sendMessage(ChatColor.RED + "Internal server error! :(");
                    return;
                }
                
                BlockData data = new BlockData();
                data.setMoon_phase(moonPhase.ordinal());
                data.setNormal_block(normalMaterial.toString());
                data.setSpecial_block(specialMaterial.toString());
                data.setWorldName(player.getWorld().getName());
                data.setX(block.getX());
                data.setY(block.getY());
                data.setZ(block.getZ());

                database.save(data);
                blockManager.addBlock(data);

                player.sendMessage(ChatColor.GREEN + "Block added.");
            }
        }
    }
}
