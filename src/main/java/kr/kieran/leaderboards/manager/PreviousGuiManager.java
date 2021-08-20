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
    public Stack<PopulateGui> getStackBy(Player player) { return this.guiCache.getOrDefault(player, new Stack<>()); }

    public void add(Player player, PopulateGui gui)
    {
        Stack<PopulateGui> stack = this.guiCache.getOrDefault(player, new Stack<>());
        stack.push(gui);
        this.guiCache.put(player, stack);
    }

    public PopulateGui get(Player player)
    {
        Stack<PopulateGui> stack = this.guiCache.getOrDefault(player, null);
        if (stack == null) return null;
        if (stack.isEmpty()) return null;
        return stack.pop();
    }

    public void disable()
    {
        Iterator<Map.Entry<Player, Stack<PopulateGui>>> iterator = this.guiCache.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Player, Stack<PopulateGui>> entry = iterator.next();
            entry.getValue().clear();
            iterator.remove();
        }
        this.guiCache.clear();
    }

}
