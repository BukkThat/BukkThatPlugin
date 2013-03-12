package team.bukkthat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import team.bukkthat.Main;

public class VoteforCommand implements CommandExecutor {

    private final Main plugin;

    public VoteforCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cs instanceof Player) {
            switch (args.length) {
                case 1:
                    this.voteFor(cs, args[0]);
                    break;
                default:
                    cs.sendMessage(ChatColor.RED + "Usage: /votefor <user>");
            }
        }
        return true;
    }

    public void voteFor(CommandSender cs, String name) {
        /*List<String> voted = plugin.getPlayersConfig().getPlayers().getStringList(cs.getName()+".voted");
        if(voted.contains(name.toLowerCase())) {
        	cs.sendMessage(ChatColor.RED+"You have already voted for this player.");
        	return;
        }
        try {
        	PreparedStatement ps = c.prepareStatement("SELECT * FROM `Applications` WHERE `Approved` = 0 AND `Activated` = 1 AND `Waiting` = 1 AND `Bukkit` = ? LIMIT 1;");
        	ps.setString(1, name);
        	ResultSet rs = ps.executeQuery();
        	if(!rs.last()) {
        		cs.sendMessage(ChatColor.RED+"That person has not applied/has not been activated/has already been approved.");
        		return;
        	}
        	rs.first();
        	int votes = rs.getInt("Votes");
        	votes++;
        	PreparedStatement update = c.prepareStatement("UPDATE `Applications` SET `Votes` = ? WHERE `Bukkit` = ? LIMIT 1;");
        	update.setInt(1, votes);
        	update.setString(2, name);
        	update.executeUpdate();
        	cs.sendMessage(ChatColor.GREEN+"Voted for "+name+"!");
        	plugin.getPlayersConfig().getPlayers().set(cs.getName()+".voted", voted.add(name.toLowerCase()));
        	plugin.getPlayersConfig().savePlayers();
        	return;
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        }*/// TODO
    }
}
