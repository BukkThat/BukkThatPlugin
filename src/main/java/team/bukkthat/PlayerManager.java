package team.bukkthat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerManager {
    private final Map<String, BukkThatPlayer> players = new ConcurrentHashMap<String, BukkThatPlayer>();
    private final Main plugin;
    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Get a player
     * 
     * @param player
     * @return
     */
    public BukkThatPlayer getPlayer(Player player) {
        return this.getPlayer(player.getName());
    }

    /**
     * Loads a player into the manager.
     * ONLY CALL ME ASYNC.
     * 
     * @param name
     * @return false if failure.
     */
    public boolean loadPlayer(String name) {
        return this.loadPlayer(name, true);
    }

    private BukkThatPlayer getPlayer(String name) {
        return this.players.get(name);
    }

    private boolean loadPlayer(String name, boolean retry) {
        BukkThatPlayer player = null;
        boolean needsRefresh = false;
        try {
            final Connection connection = this.plugin.getSQL().getConnection();
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `players` WHERE `username` = ?");
            statement.setString(1, name);
            final ResultSet result = statement.executeQuery();

            if (result.first()) {
                final String homeWorldString = result.getString("home-world");
                final double homeX = result.getDouble("home-x");
                final double homeY = result.getDouble("home-y");
                final double homeZ = result.getDouble("home-z");
                final float homeYaw = result.getFloat("home-yaw");
                final float homePitch = result.getFloat("home-pitch");
                final World homeWorld = homeWorldString != null ? this.plugin.getServer().getWorld(homeWorldString) : null;
                Location home = null;
                if (homeWorld != null) {
                    home = new Location(homeWorld, homeX, homeY, homeZ, homeYaw, homePitch);
                } else {
                    needsRefresh = true;
                }
                final boolean pVPOptOut = result.getBoolean("pvp-optout");
                player = new BukkThatPlayer(this, name, home, pVPOptOut);
            }

        } catch (final SQLException e) {
            this.plugin.getLogger().warning("Failed to load player from SQL" + (retry ? " (will retry)" : " (giving up)") + ": " + e.getMessage());
            return this.loadPlayer(name, false);
        }
        if (player == null) {
            player = new BukkThatPlayer(this, name);
            needsRefresh = true;
        }
        this.players.put(name, player);
        if (needsRefresh) {
            this.queueUpdate(player);
        }
        return true;
    }

    void processQueue() {
        if (this.queue.peek() == null) {
            return;
        }
        try {
            final Set<String> processed = new HashSet<String>();
            final Connection connection = this.plugin.getSQL().getConnection();
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`username`,`pvp-optout`,`home-world`,`home-x`,`home-y`,`home-z`,`home-yaw`,`home-pitch`) VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `pvp-optout` = ? `home-world` = ? `home-x` = ? `home-y` = ? `home-z` = ? `home-yaw` = ? `home-pitch` = ?");
            String next = null;
            int count = 0;
            while ((next = this.queue.peek()) != null) {
                if (processed.contains(next)) {
                    this.queue.poll();
                    continue;
                }
                final BukkThatPlayer player = this.getPlayer(next);
                statement.setString(1, player.getName());
                statement.setBoolean(2, player.isPVPOptOut());
                statement.setBoolean(9, player.isPVPOptOut());
                double x = 0, y = 0, z = 0;
                String worldName = null;
                float yaw = 0, pitch = 0;
                final Location location = player.getHome();
                if (location != null) {
                    worldName = location.getWorld().getName();
                    x = location.getX();
                    y = location.getY();
                    z = location.getZ();
                    yaw = location.getYaw();
                    pitch = location.getPitch();
                }
                statement.setString(3, worldName);
                statement.setString(10, worldName);
                statement.setDouble(4, x);
                statement.setDouble(11, x);
                statement.setDouble(5, y);
                statement.setDouble(12, y);
                statement.setDouble(6, z);
                statement.setDouble(13, z);
                statement.setFloat(7, yaw);
                statement.setFloat(14, yaw);
                statement.setFloat(8, pitch);
                statement.setFloat(15, pitch);
                statement.addBatch();
                count++;
                if (count == 9) {
                    statement.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                statement.executeBatch();
            }
        } catch (final SQLException e) {
            this.plugin.getLogger().warning("Failure to save queued data: " + e.getMessage());
        }
    }

    void queueUpdate(BukkThatPlayer player) {
        this.queue.add(player.getName());
    }
}