package by.thmihnea.sql;

import by.thmihnea.ChatActivity;
import by.thmihnea.object.MonitoredPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLUtil {

    /**
     * Returns whether or not an UUID exists in our database records
     *
     * @param uuid - UUID that has to be check
     * @return false/true depending if the said UUID exists or not in db records
     */
    public static boolean playerExists(UUID uuid) {
        PreparedStatement ps = null;
        try {
            ps = ChatActivity.getInstance().getCon().getConnection().prepareStatement("SELECT * FROM `player_data` WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            ChatActivity.getInstance().logSevere("An error occured while checking wether or not the player with UUID " + uuid + " exists in our database records. Please, report this stacktrace to thmihnea!");
            return false;
        }
    }

    /**
     * Adds a player to our database records. Uses {@link #setup(Player)}
     *
     * @param player - player to be added to database
     */
    private static void addPlayer(Player player) {
        try {
            PreparedStatement ps = ChatActivity.getInstance().getCon().getConnection().prepareStatement("SELECT * FROM `player_data` WHERE UUID = ?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (!(playerExists(player.getUniqueId())))
                setup(player);
            ChatActivity.getInstance().logInfo("Successfully managed to add player " + player.getName() + " (UUID: " + player.getUniqueId() + ") to our database records.");
        } catch (SQLException e) {
            e.printStackTrace();
            ChatActivity.getInstance().logSevere("An error occurred while attempting to add player with name " + player.getName() + " to our database records. Please, report this stacktrace to thmihnea!");
        }
    }

    /**
     * Sets up the tables for the specific player. Is used in {@link #addPlayer(Player)}
     *
     * @param player - player to have his tables set up
     */
    private static void setup(Player player) {
        try {
            PreparedStatement ps1 = getPreparedStatementPlayerData(player);
            PreparedStatement ps2 = getPreparedStatementDataMessages(player);
            ps1.execute();
            ps2.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            ChatActivity.getInstance().logSevere("An error occurred while attempting to add player with name " + player.getName() + " to our database records. Please, report this stacktrace to thmihnea!");
        }
    }

    /**
     * Extracted method so that we can make
     * use of it in the {@link #setup(Player)}
     * method.
     *
     * @param player Player to setup.
     * @return {@link PreparedStatement}
     * @throws SQLException
     */
    private static PreparedStatement getPreparedStatementPlayerData(Player player) throws SQLException {
        PreparedStatement ps1 = ChatActivity.getInstance().getCon().getConnection()
                .prepareStatement("INSERT INTO `player_data` (UUID, NAME) VALUE (?, ?)");
        ps1.setString(1, player.getUniqueId().toString());
        ps1.setString(2, player.getName());
        return ps1;
    }

    /**
     * Extracted method so that we can make
     * use of it in the {@link #setup(Player)}
     * method.
     *
     * @param player Player to setup.
     * @return {@link PreparedStatement}
     * @throws SQLException
     */
    private static PreparedStatement getPreparedStatementDataMessages(Player player) throws SQLException {
        StringBuilder dataMessages = getStringBuilder();
        PreparedStatement ps = ChatActivity.getInstance().getCon().getConnection()
                .prepareStatement(dataMessages.toString());
        ps.setString(1, player.getUniqueId().toString());
        for (int i = 2; i <= 31; i++)
            ps.setInt(i, 0);
        return ps;
    }

    private static StringBuilder getStringBuilder() {
        StringBuilder dataMessages = new StringBuilder().append("INSERT INTO `data_messages` (UUID, ");
        for (int i = 1; i <= 29; i++)
            dataMessages.append("DAY_").append(i).append(", ");
        dataMessages.append("DAY_30) VALUES (");
        for (int i = 1; i <= 30; i++)
            dataMessages.append("?, ");
        dataMessages.append("?)");
        return dataMessages;
    }

    /**
     * Method to be used in outer classes when we want to initialize a Player.
     * Uses {@link #setup(Player)} & {@link #addPlayer(Player)}
     *
     * @param player - player to be initialized
     */
    public static void init(Player player) {
        if (!(playerExists(player.getUniqueId()))) {
            ChatActivity.getInstance().logInfo("Player " + player.getName() + " (UUID: " + player.getUniqueId() + ") was not found in our database records.");
            ChatActivity.getInstance().logInfo("Attempting to initiate setup for this player.");
            addPlayer(player);
        }
    }

    /**
     * Method used to pull integer values from our database records using the field and the UUID we want to lookup
     *
     * @param UUID      UUID we're searching with
     * @param tableType TableType we're looking for
     * @return a value from database
     */
    public static int getValue(TableType tableType, String field, String UUID) {
        String statement = "SELECT * FROM `" + tableType.getName() + "` WHERE UUID = ?";
        try {
            PreparedStatement ps = ChatActivity.getInstance().getCon().getConnection().prepareStatement(statement);
            ps.setString(1, UUID);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(field);
            else
                return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            ChatActivity.getInstance().logInfo("An error occurred while attempting to retrieve data from our database.");
            return -1;
        }
    }

    /**
     * Method used to set a certain value in the database.
     *
     * @param tableType TableType to set a value into
     * @param field     Field to alter/update
     * @param UUID      UUID of the player we're
     *                  looking after.
     * @param value     Value to set.
     */
    public static void setValue(TableType tableType, String field, String UUID, int value) {
        String statement = "UPDATE " + tableType.getName() + " SET " + field + " = ? WHERE UUID = ?";
        PreparedStatement ps = null;
        try {
            ps = ChatActivity.getInstance().getCon().getConnection().prepareStatement(statement);
            ps.setInt(1, value);
            ps.setString(2, UUID);
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Method used to upload the new cached messages
     * a player has to our SQL server.
     * @param monitoredPlayer Monitored Player to upload
     *                        data for.
     */
    public static void uploadData(MonitoredPlayer monitoredPlayer) {
        long start = System.currentTimeMillis();
        int cachedMessages = monitoredPlayer.getCachedMessages();
        Player player = monitoredPlayer.getPlayer();
        ChatActivity.getInstance().logInfo("Found a total of " + cachedMessages + " cached messages for player " + player.getName() + ".");
        String UUID = monitoredPlayer.getPlayer().getUniqueId().toString();
        String field = ChatActivity.getInstance().getField();
        int alreadyStored = getValue(TableType.DATA_MESSAGES, field, UUID);
        final int messages = cachedMessages + alreadyStored;
        setValue(TableType.DATA_MESSAGES, field, UUID, messages);
        monitoredPlayer.setCachedMessages(0);
        ChatActivity.getInstance().logInfo("Successfully uploaded " + monitoredPlayer.getPlayer().getName() + "'s data to our database records. Process took: " + (System.currentTimeMillis() - start) + "ms!");
    }

    /**
     * Returns the amount of messages that
     * were sent by the player over the last
     * X days.
     * @param player Player to lookup for.
     * @param maxDays The amount of days which we can
     *                go max back into the past.
     * @return {@link Integer}
     */
    public static int getMessages(Player player, int maxDays) {
        int day = ChatActivity.getInstance().getDay(), i = 0, messages = 0;
        while (i <= maxDays) {
            if (day == 0) day = 30;
            String field = "DAY_" + day;
            int amount = getValue(TableType.DATA_MESSAGES, field, player.getUniqueId().toString());
            messages += amount;
            day--; i++;
        }
        return messages;
    }
}
