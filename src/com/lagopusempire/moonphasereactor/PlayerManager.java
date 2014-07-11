/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author MrZoraman
 */
public class PlayerManager implements Listener
{
    private final Set<UUID> selectingUUIDs = new HashSet<UUID>();
    
    public boolean isSelecting(UUID uuid)
    {
        return selectingUUIDs.contains(uuid);
    }
    
    public void setSelecting(UUID uuid, boolean selecting)
    {
        if(selecting == true)
        {
            selectingUUIDs.add(uuid);
        }
        else
        {
            if(selectingUUIDs.contains(uuid))
            {
                selectingUUIDs.remove(uuid);
            }
        }
    }
    
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();
        
        if(selectingUUIDs.contains(uuid))
        {
            selectingUUIDs.remove(uuid);
        }
    }
}
