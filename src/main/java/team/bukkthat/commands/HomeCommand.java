package team.bukkthat.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class HomeCommand implements CommandExecutor {

    private final Main plugin;

    public HomeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("home")) {
                final Location home = this.plugin.getPlayerManager().getPlayer(player).getHome();
                if (home == null) {
                    player.sendMessage(ChatColor.RED + "No Home Set!");
                    return true;
                }
                player.teleport(home);
                return true;
            } else if (command.getName().equalsIgnoreCase("sethome")) {
                final Location l = player.getLocation();
                this.plugin.getPlayerManager().getPlayer(player).setHome(l);
                player.sendMessage(ChatColor.GREEN + "Home Set!");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only a player can do that!");
            return true;
        }
        return true;
    }
}