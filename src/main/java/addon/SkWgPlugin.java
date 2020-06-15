package main.java.addon;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class SkWgPlugin extends JavaPlugin
{

    SkWgPlugin instance;
    SkriptAddon addon;

    public void onEnable()
    {
        instance = this;
        addon = Skript.registerAddon(this);
        try
        {
            addon.loadClasses("main.java", "elements");

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Bukkit.getLogger().info("[SkWgPlugin] has been enabled!");
    }

    public SkWgPlugin getInstance()
    {
        return instance;
    }

    public SkriptAddon getAddonInstance()
    {
        return addon;
    }
}
