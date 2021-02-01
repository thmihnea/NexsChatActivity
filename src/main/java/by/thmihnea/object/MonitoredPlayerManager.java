package by.thmihnea.object;

import by.thmihnea.ChatActivity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MonitoredPlayerManager {

    /**
     * Our {@link Map} which is responsible
     * for keep safe all of our {@link MonitoredPlayer}
     * objects.
     */
    private static final Map<UUID, MonitoredPlayer> cachedMonitoredPlayers = new HashMap<>();

    /**
     * Returns an instance of {@link MonitoredPlayerManager#cachedMonitoredPlayers}
     * for general use.
     * @return {@link Map}
     */
    public static Map<UUID, MonitoredPlayer> getCachedMonitoredPlayers() {
        return cachedMonitoredPlayers;
    }

    /**
     * Adds a {@link MonitoredPlayer} object to
     * our HashMap.
     * @param monitoredPlayer Object which we're adding.
     */
    public static void addMonitoredPlayer(MonitoredPlayer monitoredPlayer) {
        cachedMonitoredPlayers.put(monitoredPlayer.getPlayer().getUniqueId(), monitoredPlayer);
        ChatActivity.getInstance().logInfo("Player " + monitoredPlayer.getPlayer().getName() + " has been added to cached data.");
    }

    /**
     * Method used for the removal
     * of a player from our cache. Generally
     * used whenever the player is leaving the server.
     * @param player Player to remove from the cache.
     */
    public static void removeMonitoredPlayer(Player player) {
        cachedMonitoredPlayers.remove(player.getUniqueId());
        ChatActivity.getInstance().logInfo("Player " + player.getName() + " has been removed from cached data.");
    }

    /**
     * Returns a {@link MonitoredPlayer} object
     * from the cached data structure.
     * @param player Player to look up for.
     * @return {@link MonitoredPlayer}
     */
    public static MonitoredPlayer getMonitoredPlayer(Player player) {
        return cachedMonitoredPlayers.get(player.getUniqueId());
    }

    /**
     * Returns whether or not a player is in
     * our cached data structure.
     * @param player Player to look up for.
     * @return {@link Boolean}
     */
    public static boolean isInCache(Player player) {
        return cachedMonitoredPlayers.containsKey(player.getUniqueId());
    }
}
