/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.moonphasereactor;

import org.bukkit.World;
import org.bukkit.World.Environment;

/**
 *
 * @author MrZoraman
 */
public enum MoonPhase
{
    DAYTIME,
    CLOUDY,
    NO_MOON,
    
    NEW_MOON,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS,
    FULL_MOON,
    WANING_GIBBOUS,
    LAST_QUARTER,
    WANING_CRESCENT;
    
    public static final long DAY_END = 12500L;
    public static final long DAY_START = 23500L;
    
    /**
     * Gets the current moon phase for a world
     * @param world The world to retrieve the moon phase from
     * @return The moon phase, or DAYTIME if it is day, or NO_MOON if the world is the nether or the end, or COUDY if it is raining
     */
    public static MoonPhase getMoonPhase(World world)
    {
        if(world.getEnvironment() != Environment.NORMAL)
        {
            return NO_MOON;
        }
        
        long time = world.getTime();
        if(!((time > DAY_START || time > 0) && time >  DAY_END))
        {
            return DAYTIME;
        }
        
        if(world.hasStorm() || world.isThundering())
        {
            return CLOUDY;
        }
        
        long days = world.getFullTime()/24000;
        int phase = (int) (days % 8);
        //phase == 0 -> full moon
        switch(phase)
        {
            
            case 0:
                return FULL_MOON;
            case 1:
                return WANING_GIBBOUS;
            case 2:
                return LAST_QUARTER;
            case 3:
                return WANING_CRESCENT;
            case 4:
                return NEW_MOON;
            case 5:
                return WAXING_CRESCENT;
            case 6:
                return FIRST_QUARTER;
            case 7:
                return WAXING_GIBBOUS;
            default:
                return DAYTIME;
        }
    }
    
    public static MoonPhase matchMoonPhase(String input)
    {
        input = input.toLowerCase();
        
        switch(input)
        {
            case "fullmoon":
            case "full_moon":
                return FULL_MOON;
            case "waninggibbous":
            case "waning_gibbous":
                return WANING_GIBBOUS;
            case "lastquarter":
            case "last_quarter":
                return LAST_QUARTER;
            case "wanincrescent":
            case "waning_crescent":
                return WANING_CRESCENT;
            case "newmoon":
            case "new_moon":
                return NEW_MOON;
            case "waxingcrescent":
            case "waxing_crescent":
                return WAXING_CRESCENT;
            case "firstquarter":
            case "first_quarter":
                return FIRST_QUARTER;
            case "waxinggibbous":
            case "waxing_gibbous":
                return WAXING_GIBBOUS;
            case "daytime":
            case "day":
                return DAYTIME;
            case "nomoon":
            case "no_moon":
                return NO_MOON;
            case "cloudy":
            case "clouds":
            case "stormy":
            case "storm":
            case "raining":
            case "rain":
            case "snowing":
            case "snow":
            case "precipitation":
                return CLOUDY;
            default:
                return null;
        }
    }
}
