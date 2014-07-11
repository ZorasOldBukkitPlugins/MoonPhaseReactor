package com.lagopusempire.moonphasereactor;

import com.lagopusempire.moonphasereactor.events.MoonPhaseEventTriggerer;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MoonPhaseReactor extends JavaPlugin
{
    private static final int MOON_PHASE_CHECK_INTERVAL = 20;//every 10 seconds
    
    private PlayerManager playerManager;
    
    @Override
    public void onEnable()
    {
        playerManager = new PlayerManager();
        
        getServer().getPluginManager().registerEvents(playerManager, this);
        
        getServer().getScheduler().runTaskTimer(this, new MoonPhaseEventTriggerer(), 0, MOON_PHASE_CHECK_INTERVAL);
    }
}
