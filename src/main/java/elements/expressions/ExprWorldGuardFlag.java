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

    static
    {
        //register expression string = flag region = region
        Skript.registerExpression(ExprWorldGuardFlag.class, String.class, ExpressionType.SIMPLE, "get %string% of %region%");
    }

    @Override
    public Class<? extends String> getReturnType()
    {
        return String.class;
    }

    @Override
    public boolean isSingle()
    {
        return true;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser)
    {
        //remove "" from incoming flag string and put it on string flag
        flag = exprs[0].toString().replace("\"", "");
        //put the incoming region on Expression region
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
        return new String[]{getFlagValue(event)};
    }

    @SuppressWarnings({"unchecked", "null"})
    public String getFlagValue(Event eve)
    {
        try
        {
            //Get the Expression region from the region
            Region reg = region.getSingle(eve);
            //Get the worldguard regioncontainer
            RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
            //Get the worldguard region through the RegionContainer and BukkitAdapter
            RegionManager regions = container.get(BukkitAdapter.adapt(Bukkit.getServer().getWorld(reg.toString().split(" ")[reg.toString().split(" ").length - 1])));

            //check if flag has valid name
            if (StateFlag.isValidName(flag))
            {
                try
                {
                    //get the value of the flag in this region
                    return regions.getRegion(reg.toString().split(" in ")[0]).getFlag(WorldGuard.getInstance().getFlagRegistry().get(flag)).toString();
                }
                catch (Exception ex)
                {
                    //return this if the flag in try was not found
                    return "flag not found";
                }

            }
            //return this if the flag has no valid name
            return "flag not found";
        }
        catch (Exception ex)
        {
            //return this if you made something wrong
            return "you made something wrong";
        }

    }
}
