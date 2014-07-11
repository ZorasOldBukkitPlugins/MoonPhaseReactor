/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor.events;

import com.lagopusempire.moonphasereactor.MoonPhase;
import org.bukkit.World;

/**
 *
 * @author Mr
 */
public class MoonPhaseChangedEvent extends MoonPhaseEvent
{
    public MoonPhaseChangedEvent(MoonPhase moonPhase, World world)
    {
        super(moonPhase, world);
    }
}
