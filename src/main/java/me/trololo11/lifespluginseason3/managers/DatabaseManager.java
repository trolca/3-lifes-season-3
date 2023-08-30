package me.trololo11.lifespluginseason3.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private HikariDataSource ds;

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

    public void initialize() throws SQLException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS player_lifes(uuid varchar(36) primary key, lifes tinyint)");

        statement.close();

        connection.close();
    }

    public void turnOffDatabase(){
        ds.close();
    }

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


    public void addPlayerLifes(UUID uuid, byte lifes) throws SQLException {
        Connection connection = getConnection();

        String sql = "INSERT INTO player_lifes(uuid, lifes) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());
        statement.setByte(2, lifes);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

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

}
