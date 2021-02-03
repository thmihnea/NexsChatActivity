package by.thmihnea.runnable;

import by.thmihnea.ChatActivity;
import by.thmihnea.Settings;
import by.thmihnea.object.MonitoredPlayer;
import by.thmihnea.object.MonitoredPlayerManager;
import by.thmihnea.sql.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class DataUploadTask implements Runnable {

    /**
     * Player for which we're
     * generating the {@link DataUploadTask} object.
     */
    private Player player;

    /**
     * BukkitTask ID.
     * @see BukkitTask
     */
    private BukkitTask task;

    /**
     * Constructor for the data syncing task
     * to the database.
     * @param player Player to construct the object
     *               for.
     */
    public DataUploadTask(Player player) {
        this.player = player;
        this.task = Bukkit.getScheduler().runTaskTimer(ChatActivity.getInstance(), this, 0L, 5 * Settings.MINUTE);
    }

    /**
     * Overwritten method for the {@link Runnable}
     * designated to upload data for each player.
     */
    @Override
    public void run() {
        if (!this.player.isOnline()) {
            this.clear();
            return;
        }
        MonitoredPlayer monitoredPlayer = MonitoredPlayerManager.getMonitoredPlayer(this.player);
        if (monitoredPlayer.getCachedMessages() == 0) return;
        SQLUtil.uploadData(monitoredPlayer);
    }

    /**
     * Method used to clear the
     * instance so it gets collected by the
     * Garbage Collector.
     */
    public void clear() {
        if (this.task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            task = null;
        }
    }
}
