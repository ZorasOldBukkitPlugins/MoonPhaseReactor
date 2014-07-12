/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import com.avaje.ebean.EbeanServer;

import static com.lagopusempire.moonphasereactor.MetadataConstants.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author MrZoraman
 */
public class BlockClickListener implements Listener
{
    private final MetadataUtils metadataUtils;
    private final EbeanServer database;
    private final BlockManager blockManager;
    private final Plugin plugin;
    
    public BlockClickListener(MetadataUtils metadataUtils, EbeanServer database, BlockManager blockManager, Plugin plugin)
    {
        this.metadataUtils = metadataUtils;
        this.database = database;
        this.blockManager = blockManager;
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Player player = event.getPlayer();
            
            final Block block = event.getClickedBlock();
            
            final int x = block.getX();
            final int y = block.getY();
            final int z = block.getZ();
                
            if(metadataUtils.getBoolean(player, IS_REMOVING))
            {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //ASYNC
                        final BlockData data = database.find(BlockData.class).where()
                            .ieq("x", String.valueOf(x))
                            .ieq("y", String.valueOf(y))
                            .ieq("z", String.valueOf(z))
                            .findUnique();
                        
                        Bukkit.getScheduler().runTask(plugin, new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //SYNC
                                if(data == null)
                                {
                                    player.sendMessage(ChatColor.RED + "That block is not registered!");
                                }
                                else
                                {
                                    Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            //ASYNC
                                            database.delete(data);
                                            
                                            Bukkit.getScheduler().runTask(plugin, new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    blockManager.removeBlock(data);
                                                    player.sendMessage(ChatColor.GREEN + "Block removed!");
                                                }//
                                            });//END SYNC
                                            
                                        }//
                                    });//END ASYNC
                                    
                                }
                            }//
                        });//END SYNC
                        
                    }//
                });//END ASYNC
            }
            
            boolean isSelecting = metadataUtils.getBoolean(player, IS_SELECTING);
            if(isSelecting)
            {
                final Material normalMaterial = (Material) metadataUtils.getMetadata(player, NORMAL_MATERIAL);
                final Material specialMaterial = (Material) metadataUtils.getMetadata(player, SPECIAL_MATERIAL);
                final MoonPhase moonPhase = (MoonPhase) metadataUtils.getMetadata(player, CONDITION);
                
                if(normalMaterial == null || specialMaterial == null || moonPhase == null)
                {
                    player.sendMessage(ChatColor.RED + "Internal server error! :(");
                    return;
                }
                
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() 
                {
                    @Override
                    public void run()
                    {
                        //ASYNC
                        final BlockData data = database.find(BlockData.class).where()
                            .ieq("x", String.valueOf(x))
                            .ieq("y", String.valueOf(y))
                            .ieq("z", String.valueOf(z))
                            .findUnique();
                        
                        Bukkit.getScheduler().runTask(plugin, new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //SYNC
                                if(data == null)
                                {
                                    BlockData blockData = new BlockData();
                                    blockData.setMoon_phase(moonPhase.ordinal());
                                    blockData.setNormal_block(normalMaterial.toString());
                                    blockData.setSpecial_block(specialMaterial.toString());
                                    blockData.setWorldName(player.getWorld().getName());
                                    blockData.setX(block.getX());
                                    blockData.setY(block.getY());
                                    blockData.setZ(block.getZ());

                                    database.save(blockData);
                                    blockManager.addBlock(blockData);

                                    player.sendMessage(ChatColor.GREEN + "Block added.");
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.RED + "That block is already registered! To unregister it, type " + ChatColor.YELLOW + "/mpr remove " + ChatColor.RED + ".");
                                }
                            }//
                        });//END SYNC
                    }//
                });//END ASYNC
            }
        }
    }
}
