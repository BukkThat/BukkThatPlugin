package team.bukkthat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class TPAcceptCommand implements CommandExecutor {

    private final Main plugin;

    public TPAcceptCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length != 0) {
                player.sendMessage(ChatColor.RED + "Usage: /tpaccept");
                return true;
            }
            if (this.plugin.getTpaRef().containsKey(player.getName())) {
                final Player target = Bukkit.getPlayer(this.plugin.getTpaRef().get(player.getName()));
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Sorry, that player has since logged out.");
                    this.plugin.getTpaRef().remove(player.getName());
                    return true;
                }
                final long time = System.currentTimeMillis();
                final Integer timeout = 30;
                final long oldTime = this.plugin.getTpaTimes().get(player.getName());
                if ((time - oldTime) > new Long(timeout * 1000)) {
                    player.sendMessage(ChatColor.RED + "Request timed out!");
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + "Teleporting!");
                player.teleport(target);
                this.plugin.getTpaRef().remove(player.getName());
                this.plugin.getTpaTimes().remove(player.getName());
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You have no pending requests.");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You need to be a player to do that!");
            return true;
        }
    }
}