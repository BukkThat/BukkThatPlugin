package team.bukkthat.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import team.bukkthat.Main;

public class PlayerListener implements Listener {

    private final Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //Make messages colorable
        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        final Entity damagerEnt = event.getDamager();
        if ((entity instanceof Player) && (damagerEnt instanceof Player)) {
            final Player damaged = (Player) entity;
            final Player damager = (Player) damagerEnt;
            if (this.plugin.getPlayerManager().getPlayer(damager).isPvpOptOut()) {
                damager.sendMessage(ChatColor.RED + "You are opted out of pvp. Use /pvpopt <in/out> to change this status.");
                event.setCancelled(true);
                return;
            }
            if (this.plugin.getPlayerManager().getPlayer(damaged).isPvpOptOut()) {
                damager.sendMessage(ChatColor.RED + "That player has pvp disabled!");
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(this.plugin.getWelcomeMessage().replaceAll("<player>", event.getPlayer().getName()));
    }

    /**
     * Here, we should be handling all decisions on players joining/not joining.
     * Check whitelist, etc.
     * LASTLY, if they are NOT rejected, load them.
     * 
     * @param event
     */
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        // Call me last
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            if (!this.plugin.getPlayerManager().loadPlayer(event.getName())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Database Error! Retry in 30 seconds\nOr alert server staff");
            }
        }
    }

}
