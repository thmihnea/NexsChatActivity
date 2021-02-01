package by.thmihnea.object;

import by.thmihnea.runnable.DataUploadTask;
import org.bukkit.entity.Player;

public class MonitoredPlayer {

    /**
     * The player who we've generated this
     * object for.
     */
    private Player player;

    /**
     * The amount of current cached messages the player
     * has before the next upload to the database.
     */
    private int cachedMessages;

    /**
     * Constructor for the {@link MonitoredPlayer}
     * class object.
     * @param player The player we're constructing the
     *               object for.
     */
    public MonitoredPlayer(Player player) {
        this.player = player;
        this.cachedMessages = 0;
        MonitoredPlayerManager.addMonitoredPlayer(this);
        new DataUploadTask(this.player);
    }

    /**
     * Returns the player who we've constructed
     * the object for.
     * @return {@link Player}
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the amount of cached messages
     * a player currently has right before
     * the upload to the database.
     * @return {@link Integer}
     */
    public int getCachedMessages() {
        return this.cachedMessages;
    }

    /**
     * Sets the amount of cached messages
     * a player has
     *
     * @param cachedMessages Number to set the {@link MonitoredPlayer#cachedMessages}
     *                       to.
     */
    public void setCachedMessages(int cachedMessages) {
        this.cachedMessages = cachedMessages;
    }
}
