package kr.kieran.leaderboards.manager;

import kr.kieran.leaderboards.gui.type.PopulateGui;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

public class PreviousGuiManager
{

    private final Map<Player, Stack<PopulateGui>> guiCache = new WeakHashMap<>();

    public void add(Player player, PopulateGui gui)
    {
        Stack<PopulateGui> stack = guiCache.getOrDefault(player, new Stack<>());
        stack.push(gui);
        guiCache.put(player, stack);
    }

    public PopulateGui get(Player player)
    {
        Stack<PopulateGui> stack = guiCache.getOrDefault(player, null);
        if (stack == null) return null;
        if (stack.isEmpty()) return null;
        return stack.pop();
    }

    public void remove(Player player)
    {
        if (!guiCache.containsKey(player)) return;
        guiCache.get(player).clear();
        guiCache.remove(player);
    }

    public void disable()
    {
        Iterator<Map.Entry<Player, Stack<PopulateGui>>> iterator = guiCache.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Player, Stack<PopulateGui>> entry = iterator.next();
            entry.getValue().clear();
            iterator.remove();
        }
        guiCache.clear();
    }

}
