/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import com.avaje.ebean.EbeanServer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
            Boolean isSelecting = (Boolean) metadataUtils.getMetadata(event.getPlayer(), "mpr_isSelecting");
            if(isSelecting != null && isSelecting)
            {
                Block block = event.getClickedBlock();
                
                if(metadataUtils.getMetadata(event.getPlayer(), "mpr_isRemoving") != null)
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
                        event.getPlayer().sendMessage(ChatColor.RED + "That block is not registered!");
                    }
                    else
                    {
                        database.delete(data);
                        blockManager.removeBlock(data);
                        event.getPlayer().sendMessage(ChatColor.GREEN + "Block removed!");
                    }
                    
                    metadataUtils.removeMetadata(event.getPlayer(), "mpr_isRemoving");
                    
                    return;
                }
                
                String normalMaterialName = (String) metadataUtils.getMetadata(event.getPlayer(), "mpr_normalMaterial");
                String specialMaterialName = (String) metadataUtils.getMetadata(event.getPlayer(), "mpr_specialMaterial");
                String moonPhaseString = (String) metadataUtils.getMetadata(event.getPlayer(), "mpr_condition");
                
                if(normalMaterialName == null || specialMaterialName == null || moonPhaseString == null)
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "Internal server error! :(");
                    return;
                }
                
                Material normalMaterial = Material.matchMaterial(normalMaterialName);
                Material specialMaterial = Material.matchMaterial(specialMaterialName);
                
                MoonPhase moonPhase = null;
                
                try
                {
                    moonPhase = MoonPhase.valueOf(moonPhaseString);
                }
                catch (Exception e)
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "Invalid moon phase!");
                    return;
                }
                
                if(normalMaterial == null)
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "Your normal material is invalid!");
                }
                else if (specialMaterial == null)
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "Your speical material is invalid!");
                }
                else
                {
                    BlockData data = new BlockData();
                    data.setMoon_phase(moonPhase.ordinal());
                    data.setNormal_block(normalMaterial.toString());
                    data.setSpecial_block(specialMaterial.toString());
                    data.setWorldName(event.getPlayer().getWorld().getName());
                    data.setX(block.getX());
                    data.setY(block.getY());
                    data.setZ(block.getZ());
                    
                    database.save(data);
                    blockManager.addBlock(data);
                    
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Block added.");
                }
            }
        }
    }
}
