package by.thmihnea.runnable;

import by.thmihnea.ChatActivity;
import by.thmihnea.Settings;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class DateRefreshTask implements Runnable {

    /**
     * Task ID for the {@link Runnable} object
     * itself.
     * @see {@link BukkitTask}
     */
    private BukkitTask task;

    /**
     * Default constructor of the {@link Runnable}
     * object. This class is used to periodically
     * make sure the date is today's date.
     */
    public DateRefreshTask() {
        this.task = Bukkit.getScheduler().runTaskTimer(ChatActivity.getInstance(), this, 0L, Settings.MINUTE);
    }

    /**
     * Overwritten method which is
     * inherited from the {@link Runnable}
     * interface.
     */
    @Override
    public void run() {
        ChatActivity.getInstance().refreshDate();
    }
}
