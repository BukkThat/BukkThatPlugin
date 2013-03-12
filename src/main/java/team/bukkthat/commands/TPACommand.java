package team.bukkthat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class TPACommand implements CommandExecutor {

    private final Main plugin;

    public TPACommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            switch (args.length) {
                case 1:
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    }
                    this.plugin.getTpaRef().put(target.getName(), player.getName());
                    this.plugin.getTpaTimes().put(target.getName(), System.currentTimeMillis());
                    player.sendMessage(ChatColor.GREEN + "Request sent!");
                    final int timeout = 30;
                    target.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.BLUE + " has sent a tp request to tp to you. You have " + timeout + " seconds until this request times out.");
                    return true;

                default:
                    player.sendMessage(ChatColor.RED + "Usage: /<command> <player>");
                    return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You need to be a player to do that!");
            return true;
        }
    }
}