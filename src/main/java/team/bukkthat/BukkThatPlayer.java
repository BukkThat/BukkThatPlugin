package team.bukkthat;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;

public class BukkThatPlayer {
    @Getter @Setter
    private Location home;
    private final PlayerManager manager;
    @Getter
    private final String name;
    @Getter @Setter
    private boolean pvpOptOut;

    public BukkThatPlayer(PlayerManager manager, String name) {
        this.manager = manager;
        this.name = name;
    }

    public BukkThatPlayer(PlayerManager manager, String name, Location home, boolean pVPOptOut) {
        this.manager = manager;
        this.name = name;
        this.home = home;
        this.pvpOptOut = pVPOptOut;
    }

    private void queueUpdate() {
        this.manager.queueUpdate(this);
    }
}