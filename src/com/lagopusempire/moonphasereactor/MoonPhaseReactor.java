package com.lagopusempire.moonphasereactor;

import com.lagopusempire.moonphasereactor.events.MoonPhaseEventTriggerer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MoonPhaseReactor extends JavaPlugin
{
    private static final int MOON_PHASE_CHECK_INTERVAL = 20;//every 10 seconds
    
    private PlayerManager playerManager;
    private BlockManager blockManager;
    
    @Override
    public void onEnable()
    {
        setupDatabase();
        
        playerManager = new PlayerManager();
        getServer().getPluginManager().registerEvents(playerManager, this);
        
        blockManager = new BlockManager(getDatabase());
        getServer().getPluginManager().registerEvents(blockManager, this);
        
        getServer().getScheduler().runTaskTimer(this, new MoonPhaseEventTriggerer(), 0, MOON_PHASE_CHECK_INTERVAL);
    }
    
    private void setupDatabase()
    {
        try
        {
            getDatabase().find(BlockData.class).findRowCount();
        }
        catch (PersistenceException e)
        {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage.");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses()
    {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(BlockData.class);
        return list;
    }
}
