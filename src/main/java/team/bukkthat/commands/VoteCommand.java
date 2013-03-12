package team.bukkthat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import team.bukkthat.Main;

public class VoteCommand implements CommandExecutor {

    private final Main plugin;

    public VoteCommand(Main plugin) {
        this.plugin = plugin;
    }

    public void getVotes(CommandSender sender, int page) {
        /*try {
            final SortedMap<Integer, VoteInfo> map = new TreeMap<Integer, VoteInfo>(Collections.reverseOrder());
            final ResultSet rs = this.c.createStatement().executeQuery("SELECT * FROM `Applications` WHERE `Activated` = 1 AND `Approved` = 0 AND `Waiting` = 1");
            while (rs.next()) {
                final String name = rs.getString("Name");
                final int posts = rs.getInt("Posts");
                final int plugins = rs.getInt("Plugins");
                final int votes = rs.getInt("Votes");
                final int points = posts + (plugins * 100) + (votes * 200);
                map.put(points, new VoteInfo(name, votes, posts, plugins));
            }
            sender.sendMessage(ChatColor.BLUE + "Votees: Page (" + String.valueOf(page) + "/" + (((map.size() % 5) == 0) ? map.size() / 5 : (map.size() / 5) + 1) + ")");
            int i = 0, k = 0;
            page--;
            for (final Entry<Integer, VoteInfo> e : map.entrySet()) {
                k++;
                if ((((page * 5) + i + 1) == k) && (k != ((page * 5) + 6))) {
                    i++;
                    sender.sendMessage(ChatColor.GOLD + e.getValue().getName() + ChatColor.BLUE + " - " + ChatColor.GOLD + e.getValue().getVotes() + " votes");
                }
            }

        } catch (final SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                this.getVotes(sender, 1);
                break;
            case 1:
                try {
                    this.getVotes(sender, Integer.parseInt(args[0]));
                } catch (final NumberFormatException nfe) {
                    sender.sendMessage(ChatColor.RED + "The page must be a number!");
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: /vote <page>");
        }
        return true;
    }
}