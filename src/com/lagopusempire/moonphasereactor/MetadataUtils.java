package com.lagopusempire.moonphasereactor;

import java.util.List;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.java.JavaPlugin;

public class MetadataUtils
{
    private final JavaPlugin plugin;

    public MetadataUtils(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void setMetadata(Metadatable object, String key, Object value)
    {
        object.setMetadata(key, new FixedMetadataValue(plugin, value));
    }

    public Object getMetadata(Metadatable object, String key)
    {
        List<MetadataValue> values = object.getMetadata(key);
        for(MetadataValue value : values)
        {
            if(value.getOwningPlugin() == plugin)
            {
                return value.value();
            }
        }

        return null;
    }
}
