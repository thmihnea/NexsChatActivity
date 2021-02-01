package by.thmihnea.sql;

import by.thmihnea.ChatActivity;
import by.thmihnea.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SQLConnection {

    /**
     * Connection object to the database
     */
    public static Connection connection;

    /**
     * The host, database, username and password for the database
     */
    public static String host, database, username, password;

    /**
     * The port for the database
     */
    public static Integer port;

    /**
     * Default constructor for the
     * {@link SQLConnection} class.
     * Used in {@link ChatActivity}
     */
    public SQLConnection() {
        this.connect();
        this.setupDefaults();
    }

    /**
     * Returns the connection to the database
     *
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the connection to the MySQL server
     *
     * @param connection - connection to be set to
     */
    public void setConnection(Connection connection) {
        SQLConnection.connection = connection;
    }

    /**
     * Creates default plugin tables.
     * Default tables are the ones shown
     * in {@link TableType}.
     */
    public void setupDefaults() {
        ChatActivity.getInstance().logInfo("Attempting to create default tables in the database.");
        try {
            PreparedStatement ps1 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `player_data` (UUID varchar(256), NAME varchar(256))");
            StringBuilder dataMessages = new StringBuilder().append("(UUID varchar(256), ");
            for (int i = 1; i <= 29; i++) {
                dataMessages.append("DAY_").append(i).append(" varchar(256), ");
            }
            dataMessages.append("DAY_30 varchar(256))");
            PreparedStatement ps2 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `data_messages` " + dataMessages.toString());
            List<PreparedStatement> statements = Arrays.asList(ps1, ps2);
            statements.forEach(statement -> {
                try {
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
            ChatActivity.getInstance().logInfo("An error has occurred while setting up SQL drivers. Tables couldn't get created. (Check if your database has been set up correctly!)");
        }
    }

    /**
     * Connects to the SQL database.
     * Pulls up the Database details
     * from the config file of the plugin.
     */
    public void connect() {
        ChatActivity.getInstance().logInfo("Attempting to establish a connection to the database.");
        host = Settings.SQL_HOST;
        port = Settings.SQL_PORT;
        database = Settings.SQL_DATABASE;
        username = Settings.SQL_USERNAME;
        password = Settings.SQL_PASSWORD;
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) return;
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
            }
            List<String> conMsg = Arrays.asList("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-",
                    "Successfully connected to the database!",
                    "Connection details:",
                    "Host: " + host,
                    "Port: " + port,
                    "Database: " + database,
                    "Username: " + username,
                    "Password: " + password,
                    "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            conMsg.forEach(msg -> ChatActivity.getInstance().logInfo(msg));
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            ChatActivity.getInstance().logSevere("An error has occurred while trying to connect to the database (is it set up correctly?). Please, report the stacktrace above to thmihnea!");
        }
    }

}
