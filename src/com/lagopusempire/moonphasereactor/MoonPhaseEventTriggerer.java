/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import com.lagopusempire.moonphasereactor.events.MoonPhaseChangedEvent;
import com.lagopusempire.moonphasereactor.events.MoonPhasePeriodicEvent;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author Mr
 */
public class MoonPhaseEventTriggerer implements Runnable
{
    private final Map<World, MoonPhase> moonPhases = new HashMap<World, MoonPhase>();

    @Override
    public void run()
    {
        for(World world : Bukkit.getWorlds())
        {
            if(world.getEnvironment() == World.Environment.NORMAL)
            {
                MoonPhase currentMoonPhase = MoonPhase.getMoonPhase(world);

                if(moonPhases.containsKey(world))
                {
                    MoonPhase previousMoonPhase = moonPhases.get(world);
                    if(currentMoonPhase != previousMoonPhase)
                    {
                        moonPhases.put(world, currentMoonPhase);
                        Bukkit.getPluginManager().callEvent(new MoonPhaseChangedEvent(currentMoonPhase, world));
                    }
                }
                else
                {
                    moonPhases.put(world, currentMoonPhase);
                    Bukkit.getPluginManager().callEvent(new MoonPhaseChangedEvent(currentMoonPhase, world));
                }

                Bukkit.getPluginManager().callEvent(new MoonPhasePeriodicEvent(currentMoonPhase, world));
            }
        }
    }
}
