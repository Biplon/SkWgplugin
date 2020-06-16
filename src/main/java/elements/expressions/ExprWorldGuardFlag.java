package main.java.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ExprWorldGuardFlag extends SimpleExpression<String>
{
    @SuppressWarnings("null")
    private Expression<Region> region;
    private String flag;

    static {
        Skript.registerExpression(ExprWorldGuardFlag.class, String.class, ExpressionType.SIMPLE,"get %string% of %region%");
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        flag = exprs[0].toString().replace("\"","");
        region = (Expression<Region>) exprs[1];
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

    @SuppressWarnings({"unchecked", "null"})
    public String canPvPHere(Event eve)
    {
        try
        {
            Region reg = region.getSingle(eve);
            RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt( Bukkit.getServer().getWorld(reg.toString().split(" ")[reg.toString().split(" ").length-1])));

            if (StateFlag.isValidName(flag))
            {
                try
                {
                    return regions.getRegion(reg.toString().split(" in ")[0]).getFlag(getFlagValue(flag)).toString();
                }
                catch(Exception ex)
                {
                    return "flag not found";
                }

            }
            return "flag not found";
        }
        catch (Exception ex)
        {
            return "you did make something wrong";
        }

    }

    private Flag getFlagValue(String flagname)
    {
        Flag<?> flag = WorldGuard.getInstance().getFlagRegistry().get(flagname);
        return flag;
    }
}
