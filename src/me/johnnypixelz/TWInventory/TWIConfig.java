package me.johnnypixelz.TWInventory;

import java.util.List;
import java.util.ArrayList;

public class TWIconfig {
    
    public static double MAX_WEIGHT = 100;
    
    public static boolean USE_XP_BAR = false;
    
    public static boolean USE_ACTION_BAR = true;
    
    public static String PREFIX;
    
    public static String ACTION_BAR_MSG;
    
    public static String CMD_MSG;
    
    public static boolean DISABLE_MSG = true;
    
    public static String RELOAD_MSG;
    
    public static String NOPERM_MSG;
    
    public static String NODEBUG_MSG;
    
    public static List<String> WEIGHT_LEVELS = new ArrayList<String>();
    
    public static void readConfig(PlayerWeight main) {
        MAX_WEIGHT = main.getConfig().getDouble("Max Weight", 100.0);
        USE_XP_BAR = main.getConfig().getBoolean("Enable Xp Bar", false);
        USE_ACTION_BAR = main.getConfig().getBoolean("Enable Action Bar", true);
        PREFIX = main.getConfig().getString("Prefix");
        ACTION_BAR_MSG = main.getConfig().getString("ActionBar");
        CMD_MSG = main.getConfig().getString("WeightCommand");
        DISABLE_MSG = main.getConfig().getBoolean("Disable Messages", true);
        RELOAD_MSG = main.getConfig().getString("Reload Message");
        NOPERM_MSG = main.getConfig().getString("NoPerm Message");
        NODEBUG_MSG = main.getConfig().getString("DisabledDebug Message");
        WEIGHT_LEVELS = main.getConfig().getStringList("Weights");
    }
}
