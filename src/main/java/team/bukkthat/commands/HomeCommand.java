package team.bukkthat.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class HomeCommand implements CommandExecutor {

    private final Main plugin;
    private final YamlConfiguration conf;
    private final File confFile;

    public HomeCommand(Main m) {
        this.plugin = m;
        this.confFile = new File(this.plugin.getDataFolder(), "homes.yml");
        if (!this.confFile.exists()) {
            try {
                this.confFile.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        this.conf = YamlConfiguration.loadConfiguration(this.confFile);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cs instanceof Player) {
            final Player p = (Player) cs;
            if (cmd.getName().equalsIgnoreCase("home")) {
                if (this.conf.contains(p.getName())) {
                    p.sendMessage(ChatColor.RED + "No Home Set!");
                    return true;
                }
                final String playerName = p.getName();
                final String worldName = this.conf.getString(playerName + ".world");
                final double x = this.conf.getDouble(playerName + ".x");
                final double y = this.conf.getDouble(playerName + ".y");
                final double z = this.conf.getDouble(playerName + ".z");
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    p.teleport(new Location(world, x, y, z));
                } else {
                    this.conf.set(playerName, null);
                    this.saveHomes();
                    p.sendMessage(ChatColor.RED + "Your home no longer exists!");
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase("sethome")) {
                final Location l = p.getLocation();
                this.conf.set(p.getName() + ".world", l.getWorld().getName());
                this.conf.set(p.getName() + ".x", l.getBlockX());
                this.conf.set(p.getName() + ".y", l.getBlockY());
                this.conf.set(p.getName() + ".z", l.getBlockZ());
                this.saveHomes();
                p.sendMessage(ChatColor.GREEN + "Home Set!");
                return true;
            }
        } else {
            cs.sendMessage(ChatColor.RED + "Only a player can do that!");
            return true;
        }
        return true;
    }

    private void saveHomes() {
        try {
            this.conf.save(this.confFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
