package main.java.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptCommand;
import ch.njol.skript.command.CommandEvent;
import ch.njol.skript.command.ScriptCommandEvent;
import ch.njol.skript.hooks.regions.RegionsPlugin;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.hooks.regions.events.RegionBorderEvent;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprWorldGuardFlag extends SimpleExpression<String>
{

    private static Expression<Region> region;

    static {
        Skript.registerExpression(ExprWorldGuardFlag.class, String.class, ExpressionType.COMBINED, "[the] pvpflag of %region%");
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        region = (Expression<Region>) exprs[0];
        return true;
    }

    public String toString(@Nullable Event event, boolean debug)
    {
        return "E: " + region.toString(event, debug);
    }

    @Override
    @Nullable
    protected String[] get(Event event)
    {
        return new String[] {canPvPHere(event)};
    }

    public static String canPvPHere(Event eve)
    {
        Region reg = region.getSingle(eve);
        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt( Bukkit.getServer().getWorld(reg.toString().split(" ")[reg.toString().split(" ").length-1])));
        return regions.getRegion(reg.toString().split(" in ")[0]).getFlag(Flags.PVP).toString();
    }
}
