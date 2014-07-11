/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import com.avaje.ebean.EbeanServer;
import com.lagopusempire.moonphasereactor.events.MoonPhaseChangedEvent;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Mr
 */
public class BlockManager implements Listener
{
    private final Set<BlockData> blockData;
    
    public BlockManager(EbeanServer database)
    {
        blockData = database.find(BlockData.class).findSet();
    }
    
    public void addBlock(BlockData data)
    {
        blockData.add(data);
    }
    
    @EventHandler
    public void onMoonPhaseChange(MoonPhaseChangedEvent event)
    {
        for(BlockData data : blockData)
        {
            Block block = toBlock(data);
            
            if(isValid(block))
            {
                if(block.getWorld() == event.getWorld())
                {
                    MoonPhase worldMoonPhase = MoonPhase.getMoonPhase(event.getWorld());
                    MoonPhase targetMoonPhase = MoonPhase.values()[data.getMoon_phase()];
                    
                    String blockType = worldMoonPhase == targetMoonPhase
                            ? data.getSpecial_block() 
                            : data.getNormal_block();
                    
                    Material materialToChangeTo = Material.matchMaterial(blockType);
                    
                    if(materialToChangeTo != null)
                    {
                        Material currentMaterial = block.getType();
                        
                        if(currentMaterial != materialToChangeTo)
                        {
                            block.setType(materialToChangeTo);
                        }
                    }
                }
            }
        }
    }
    
    private boolean isValid(Block block)
    {
        if(block == null)
        {
            return false;
        }
        
        return block.getChunk().isLoaded();
    }
    
    private Block toBlock(BlockData data)
    {
        String worldName = data.getWorldName();
        
        World world = Bukkit.getWorld(worldName);
        
        if(world == null)
        {
            return null;
        }
        
        int x = data.getX();
        int y = data.getY();
        int z = data.getZ();
        
        Block block = world.getBlockAt(x, y, z);
        
        return block;
    }
}
