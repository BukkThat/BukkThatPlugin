package team.bukkthat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                if (sender instanceof Player) {
                    final Player player = (Player) sender;
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        player.teleport(target);
                        player.sendMessage(ChatColor.GREEN + "Teleported!");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can teleport!");
                }
            case 2:
                final Player from = Bukkit.getPlayer(args[0]);
                final Player to = Bukkit.getPlayer(args[1]);
                if (from == null) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
                    return true;
                }
                if (to == null) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not online!");
                    return true;
                }
                from.teleport(to);
                sender.sendMessage(ChatColor.GREEN + from.getName() + " was teleported to " + to.getName() + "!");
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: /<command> <player>");
                sender.sendMessage(ChatColor.RED + "Usage: /<command> <player> <player>");
                return true;
        }
    }
}