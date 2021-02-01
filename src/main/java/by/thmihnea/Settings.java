package by.thmihnea;

public class Settings {

    public static String SQL_HOST = ChatActivity.getCfg().getString("MySQL.host");
    public static Integer SQL_PORT = ChatActivity.getCfg().getInt("MySQL.port");
    public static String SQL_DATABASE = ChatActivity.getCfg().getString("MySQL.database");
    public static String SQL_USERNAME = ChatActivity.getCfg().getString("MySQL.username");
    public static String SQL_PASSWORD = ChatActivity.getCfg().getString("MySQL.password");
    public static String PERMISSION = ChatActivity.getCfg().getString("permission");

    public static int MINIMUM_CHARACTERS = ChatActivity.getCfg().getInt("minimum-characters");

    public static long SECOND = 20L;
    public static long MINUTE = 60 * SECOND;
    public static long HOUR = 60 * MINUTE;
    public static long DAY = 24 * HOUR;
}
