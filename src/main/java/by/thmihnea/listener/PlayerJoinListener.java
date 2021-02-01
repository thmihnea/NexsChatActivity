package by.thmihnea.listener;

import by.thmihnea.Settings;
import by.thmihnea.object.MonitoredPlayer;
import by.thmihnea.sql.SQLUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    /**
     * Listener used to generate a specific player's
     * database records.
     * @param e The event itself.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission(Settings.PERMISSION)) return;
        SQLUtil.init(player);
        new MonitoredPlayer(player);
    }
}
