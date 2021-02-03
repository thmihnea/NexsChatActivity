package by.thmihnea;

import by.thmihnea.listener.PlayerChatListener;
import by.thmihnea.listener.PlayerJoinListener;
import by.thmihnea.listener.PlayerQuitListener;
import by.thmihnea.runnable.DateRefreshTask;
import by.thmihnea.sql.SQLConnection;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AbstractPlugin {

    /**
     * Time at which the plugin is enabled
     */
    private long start;

    /**
     * The day which will be constantly updated
     * and used for the plugin.
     */
    private int day;

    /**
     * Main class instance. Correlates to AbstractPlugin's instance.
     * {@link AbstractPlugin#getInstance()}
     */
    private static ChatActivity instance;

    /**
     * SQLConnection object so that we can connect to the database
     * {@link SQLConnection}
     */
    private SQLConnection con;

    /**
     * DateRefreshTask object so that we can constantly refresh
     * the date in case we're changing dates.
     * {@link DateRefreshTask}
     */
    private DateRefreshTask dateRefreshTask;

    /**
     * A list of all the listeners that have to be registered by our plugin
     */
    private final List<Listener> listeners = Arrays.asList(
        new PlayerJoinListener(),
        new PlayerQuitListener(),
        new PlayerChatListener()
    );

    /**
     * Accesses the instance of AbstractPlugin via EFTLobby.
     *
     * @return AbstractPlugin instance {@link AbstractPlugin#getInstance()}
     */
    public static ChatActivity getInstance() {
        return instance;
    }

    /**
     * Accesses the instance of the Connection established via MySQL
     *
     * @return SQLConnection object {@link SQLConnection#getConnection()}
     */
    public SQLConnection getCon() {
        return this.con;
    }

    /**
     * Start method that replaced onEnable.
     * Check {@link AbstractPlugin#start()}
     */
    @Override
    protected void start() {
        this.setupInstance();
        this.setupTime();
        this.registerEvents(this.listeners);
        this.setupFiles();
        this.refreshDate();
        this.con = new SQLConnection();
        this.dateRefreshTask = new DateRefreshTask();
        this.logInfo("Plugin has been successfully enabled! Process took: " + (System.currentTimeMillis() - this.start) + "ms");
    }

    /**
     * Stop method that replaced onDisable.
     * Check {@link AbstractPlugin#stop()}
     */
    @Override
    protected void stop() {
        this.logInfo("Plugin has been successfully disabled!");
    }

    /**
     * Sets up the instance for the
     * {@link ChatActivity} class.
     */
    private void setupInstance() {
        instance = this;
    }

    /**
     * Sets up the time at which
     * the plugin is successfully enabled.
     */
    protected void setupTime() {
        this.start = System.currentTimeMillis();
    }

    /**
     * Sets up the date object.
     * It is also used by the {@link Runnable} which
     * is responsible for constantly updating it.
     */
    public void refreshDate() {
        Date date = new Date();
        this.day = date.getDay() % 30;
    }

    /**
     * Getter method to return the current day
     * modulo 30 which we're in.
     * @return {@link Integer}
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Returns the formatted field using the current
     * day so that we know where to add cached messages.
     * @return {@link String}
     */
    public String getField() {
        return "DAY_" + this.day;
    }
}
