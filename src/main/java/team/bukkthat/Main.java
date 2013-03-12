package team.bukkthat;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import team.bukkthat.commands.HomeCommand;
import team.bukkthat.commands.PVPOptCommand;
import team.bukkthat.commands.TPACommand;
import team.bukkthat.commands.TPAcceptCommand;
import team.bukkthat.commands.TPCommand;
import team.bukkthat.commands.TPDenyCommand;
import team.bukkthat.commands.VoteCommand;
import team.bukkthat.commands.VoteforCommand;
import team.bukkthat.listeners.PlayerListener;

public class Main extends JavaPlugin {

    @Getter
    private final HashMap<String, String> tpaRef = new HashMap<String, String>();
    @Getter
    private final HashMap<String, Long> tpaTimes = new HashMap<String, Long>();
    @Getter
    private MySQLConnectionPool SQL;
    @Getter
    private final PlayerManager playerManager = new PlayerManager(this);
    @Getter
    private String welcomeMessage;

    @Override
    public void onEnable() {
        try {
            this.loadConfig();
        } catch (ClassNotFoundException | SQLException e) {
            this.getLogger().log(Level.SEVERE, "Failed to start up: ", e);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

            @Override
            public void run() {
                Main.this.playerManager.processQueue();
            }

        }, 30 * 20, 30 * 20);

        // Load players
        for (final Player player : this.getServer().getOnlinePlayers()) {
            if (!this.playerManager.loadPlayer(player.getName())) {
                player.kickPlayer("SQL Error in your favor:\nCollect Kick");
            }
        }

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        this.getCommand("pvpopt").setExecutor(new PVPOptCommand(this));
        this.getCommand("tp").setExecutor(new TPCommand());
        this.getCommand("tpa").setExecutor(new TPACommand(this));
        this.getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
        this.getCommand("tpdeny").setExecutor(new TPDenyCommand(this));
        final HomeCommand homeCommand = new HomeCommand(this);
        this.getCommand("home").setExecutor(homeCommand);
        this.getCommand("sethome").setExecutor(homeCommand);
        this.getCommand("vote").setExecutor(new VoteCommand(this));
        this.getCommand("votefor").setExecutor(new VoteforCommand(this));

        final ItemStack orb = new ItemStack(Material.EYE_OF_ENDER, 2);
        final ItemMeta im = orb.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "TP Orb");
        orb.setItemMeta(im);

        final ShapedRecipe recipe = new ShapedRecipe(orb);
        recipe.shape("ABA", "CDC", "ABA").setIngredient('A', Material.BLAZE_ROD).setIngredient('B', Material.DIAMOND).setIngredient('C', Material.MAGMA_CREAM).setIngredient('D', Material.EYE_OF_ENDER);

        this.getServer().addRecipe(recipe);
    }

    private void loadConfig() throws ClassNotFoundException, SQLException {
        this.saveDefaultConfig();
        this.welcomeMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.welcome", "Welcome, <player>!"));
        final String hostname = this.getConfig().getString("mysql.host");
        final int port = this.getConfig().getInt("mysql.port");
        final String database = this.getConfig().getString("mysql.database");
        final String username = this.getConfig().getString("mysql.user");
        final String password = this.getConfig().getString("mysql.pass");
        this.SQL = new MySQLConnectionPool("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true&user=" + username + "&password=" + password);
    }
}