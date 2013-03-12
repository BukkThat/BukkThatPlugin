package team.bukkthat;

import lombok.Getter;

import org.bukkit.Location;

public class BukkThatPlayer {
    @Getter
    private Location home;
    private final PlayerManager manager;
    @Getter
    private final String name;
    @Getter
    private boolean pVPOptOut;

    public BukkThatPlayer(PlayerManager manager, String name) {
        this.manager = manager;
        this.name = name;
    }

    public BukkThatPlayer(PlayerManager manager, String name, Location home, boolean pVPOptOut) {
        this.manager = manager;
        this.name = name;
        this.home = home;
        this.pVPOptOut = pVPOptOut;
    }

    public void setHome(Location home) {
        this.home = home;
        this.queueUpdate();
    }

    public void setPVPOptOut(boolean b) {
        this.pVPOptOut = b;
        this.queueUpdate();
    }

    private void queueUpdate() {
        this.manager.queueUpdate(this);
    }
}