package team.bukkthat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import team.bukkthat.Main;

public class TPDenyCommand implements CommandExecutor {

    private final Main plugin;

    public TPDenyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //I will add stuff when I have time.
        return true;
    }
}