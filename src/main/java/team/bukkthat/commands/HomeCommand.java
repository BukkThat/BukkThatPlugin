package team.bukkthat.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class HomeCommand implements CommandExecutor {

	Main plugin;
	YamlConfiguration conf;
	File f;
	
	public HomeCommand(Main m) {
		this.plugin = m;
		f = new File(plugin.getDataFolder(), "homes.yml");
		if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		this.conf=YamlConfiguration.loadConfiguration(f);
	}

	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof Player) {
			Player p = (Player) cs;
			if(cmd.getName().equalsIgnoreCase("home")) {
				if(conf.contains(p.getName())) {
                    p.sendMessage(ChatColor.RED+"No Home Set!");
                    return true;
                }
				String world = this.conf.getString(p.getName()+".world");
				double x = this.conf.getDouble(p.getName()+".x");
				double y = this.conf.getDouble(p.getName()+".y");
				double z = this.conf.getDouble(p.getName()+".z");
				Location l = new Location(Bukkit.getWorld(world), x, y, z);
				p.teleport(l);
				return true;
			}
			else if(cmd.getName().equalsIgnoreCase("sethome")){
				Location l = p.getLocation();
				this.conf.set(p.getName()+".world", l.getWorld().getName());
				this.conf.set(p.getName()+".x", l.getBlockX());
				this.conf.set(p.getName()+".y", l.getBlockY());
				this.conf.set(p.getName()+".z", l.getBlockZ());
				try {
					this.conf.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
				p.sendMessage(ChatColor.GREEN+"Home Set!");
				return true;
			}
		}
		else {
			cs.sendMessage(ChatColor.RED+"Only a player can do that!");
			return true;
		}
		return true;
	}
	
}
