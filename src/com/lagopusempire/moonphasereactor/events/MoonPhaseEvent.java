/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor.events;

import com.lagopusempire.moonphasereactor.MoonPhase;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author MrZoraman
 */
public abstract class MoonPhaseEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    private final MoonPhase moonPhase;
    private final World world;
    
    protected MoonPhaseEvent(MoonPhase moonPhase, World world)
    {
        this.moonPhase = moonPhase;
        this.world = world;
    }
    
    public MoonPhase getMoonPhase()
    {
        return moonPhase;
    }
    
    public World getWorld()
    {
        return world;
    }
    
    public boolean hasMoon()
    {
        return world.getEnvironment() == Environment.NORMAL;
    }

    @Override
    public HandlerList getHandlers() 
    {
        return handlers;
    }
}
