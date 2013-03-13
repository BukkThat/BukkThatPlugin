package team.bukkthat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class PVPOptCommand implements CommandExecutor {

    private final Main plugin;

    public PVPOptCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "in":
                        this.plugin.getPlayerManager().getPlayer(player).setPvpOptOut(false);
                        player.sendMessage(ChatColor.GREEN + "You now can pvp or be pvp'ed");
                        return true;
                    case "out":
                        this.plugin.getPlayerManager().getPlayer(player).setPvpOptOut(true);
                        player.sendMessage(ChatColor.GREEN + "You no longer can pvp or be pvp'ed");
                        return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Usage: /pvpopt <in/out>");
        } else {
            sender.sendMessage(ChatColor.RED + "You need to be a player to do that!");
        }
        return true;
    }
}