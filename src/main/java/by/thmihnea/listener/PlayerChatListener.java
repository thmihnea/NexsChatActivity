package by.thmihnea.listener;

import by.thmihnea.Settings;
import by.thmihnea.object.MonitoredPlayer;
import by.thmihnea.object.MonitoredPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    /**
     * Method used to increment the amount of
     * cached messages a player has.
     * @param e The event itself.
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (!(player.hasPermission(Settings.PERMISSION))) return;
        String message = e.getMessage();
        if (message.length() < Settings.MINIMUM_CHARACTERS) return;
        MonitoredPlayer monitoredPlayer = MonitoredPlayerManager.getMonitoredPlayer(player);
        int cachedMessages = monitoredPlayer.getCachedMessages();
        final int messages = cachedMessages + 1;
        monitoredPlayer.setCachedMessages(messages);
    }

}
