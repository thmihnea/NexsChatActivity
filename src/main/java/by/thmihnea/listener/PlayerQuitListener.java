package by.thmihnea.listener;

import by.thmihnea.object.MonitoredPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    /**
     * Method called whenever a {@link Player}
     * leaves the server.
     * @param e The event instance itself.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!MonitoredPlayerManager.isInCache(player)) return;
        MonitoredPlayerManager.removeMonitoredPlayer(player);
    }
}
