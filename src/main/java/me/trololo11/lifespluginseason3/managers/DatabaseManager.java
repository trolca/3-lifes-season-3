package me.trololo11.lifespluginseason3.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

/**
 * This class manages all data saving and getting from the SQL databasel.
 */
public class DatabaseManager {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private HikariDataSource ds;

    /**
     * Gets the connection to the sql database. <br>
     * Also if the main {@link com.zaxxer.hikari.HikariDataSource} is null it creates a new one
     * @return The connection to the SQL database
     * @throws SQLException If there was an error to connect to the sql database
     */
    private Connection getConnection() throws SQLException {
        if (ds != null) return ds.getConnection();

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = "lifes3_database";


        Connection databaseCheck = DriverManager.getConnection(url, user, password);


        Statement databaseStatement = databaseCheck.createStatement();
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url + "/" + databaseName);
        config.setUsername(user);
        config.setPassword(password);
        config.setDataSourceProperties(plugin.globalDbProperties);
        ds = new HikariDataSource(config);

        databaseCheck.close();


        return ds.getConnection();
    }

    /**
     * Mostly initalizes the main data source.
     * @throws SQLException If there was an error to connect to the sql database
     */
    public void initialize() throws SQLException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS player_lifes(uuid varchar(36) primary key, lifes tinyint, is_revived bool)");

        statement.close();

        connection.close();
    }

    /**
     * Closes the connection to the database.<br>
     * <b>USE IT WHEN THE PLUGIN IS DISABLING TO PREVENT ERRORS</b>
     */
    public void turnOffDatabase(){
        ds.close();
    }

    /**
     * Gets how many lifes a player with the specific UUID have in the sql database.
     * @param uuid The UUID of the player
     * @return Amount of lifes that player has saved in the sql database
     * @throws SQLException If there was an error to connect to the sql database
     */
    public byte getPlayerLifes(UUID uuid) throws SQLException {
        Connection connection = getConnection();

        String sql = "SELECT * FROM player_lifes WHERE uuid = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());

        ResultSet results = statement.executeQuery();

        if(results.next()){
            byte lifes = results.getByte("lifes");
            connection.close();
            statement.close();

            return lifes;
        }

        statement.close();
        connection.close();

        return -11;
    }

    /**
     * If player doesn't have a row in the lifes table this function creates a new entry with the specified lifes
     * @param uuid The uuid of the player
     * @param lifes The lifes to save to the database
     * @throws SQLException If there was an error to connect to the sql database
     */
    public void addPlayerLifes(UUID uuid, byte lifes) throws SQLException {
        Connection connection = getConnection();

        String sql = "INSERT INTO player_lifes(uuid, lifes, is_revived) VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());
        statement.setByte(2, lifes);
        statement.setBoolean(3, false);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Updates already exisitng row of player lifes with the specified params
     * @param uuid The uuid of the player
     * @param lifes The new value of lifes
     * @throws SQLException If there was an error to connect to the sql database
     */
    public void updatePlayerLifes(UUID uuid, byte lifes) throws SQLException {
        Connection connection = getConnection();

        String sql = "UPDATE player_lifes SET lifes = ? WHERE uuid = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setByte(1, lifes);
        statement.setString(2, uuid.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public ArrayList<OfflinePlayer> getAllDeadPlayers() throws SQLException {
        Connection connection = getConnection();

        String sql = "SELECT * FROM player_lifes WHERE lifes = 0";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();

        ArrayList<OfflinePlayer> deadPlayers = new ArrayList<>();

        while(results.next()){
            UUID playerUUID = UUID.fromString(results.getString("uuid"));
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);

            deadPlayers.add(player);
        }

        statement.close();
        connection.close();
        return deadPlayers;

    }

    public void setIsRevived(UUID uuid, boolean isRevived) throws SQLException {
        Connection connection = getConnection();

        String sql = "UPDATE player_lifes SET is_revived = ? WHERE uuid = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setBoolean(1, isRevived);
        statement.setString(2, uuid.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public boolean getIsRevived(UUID uuid) throws SQLException {
        Connection connection = getConnection();

        String sql = "SELECT * FROM player_lifes WHERE uuid = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());

        ResultSet results = statement.executeQuery();

        if(results.next()){
            boolean isRevived = results.getBoolean("is_revived");

            statement.close();
            connection.close();
            return isRevived;
        }

        return false;
    }

}
