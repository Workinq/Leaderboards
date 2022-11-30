package kr.kieran.leaderboards.model;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class PlayerLocation
{

    private final World world;
    public World getWorld() { return world; }

    private final int x;
    public int getX() { return x; }

    private final int y;
    public int getY() { return y; }

    private final int z;
    public int getZ() { return z; }

    public PlayerLocation(World world, int x, int y, int z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        // Check if the object we're comparing against is a bukkit location
        if (o instanceof Location)
        {
            Location location = (Location) o;
            if (this.x != location.getBlockX()) return false;
            if (this.y != location.getBlockY()) return false;
            if (this.z != location.getBlockZ()) return false;
            return Objects.equals(world, location.getWorld());
        }

        // The object we're comparing against has to be another one if this
        if (o == null || this.getClass() != o.getClass()) return false;
        PlayerLocation that = (PlayerLocation) o;
        if (this.x != that.x) return false;
        if (this.y != that.y) return false;
        if (this.z != that.z) return false;
        return Objects.equals(this.world, that.world);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(world, x, y, z);
    }

}
