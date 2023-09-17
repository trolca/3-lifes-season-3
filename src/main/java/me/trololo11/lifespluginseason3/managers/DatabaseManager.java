package me.trololo11.lifespluginseason3.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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

        statement.execute("CREATE TABLE IF NOT EXISTS player_lifes(uuid varchar(36) primary key not null, lifes tinyint not null, is_revived bool not null)");
        statement.execute("CREATE TABLE IF NOT EXISTS quests_awards_data(uuid varchar(36) primary key, daily_quests tinyint not null, weekly_quests tinyint not null, card_quests tinyint not null)");

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

    /**
     * It creates a new quest table that will store all of the quests progress of players.
     * This table will have columns which represent every quest.
     * @param questType The quest type to create the table for
     * @param quests The quests to add to this table
     */
    public void createQuestTable(QuestType questType, ArrayList<Quest> quests) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

        sqlBuilder.append(getQuestTableName(questType)).append("(uuid varchar(36) primary key not null,");

        for(Quest quest : quests){
            sqlBuilder.append(" ").append(quest.getDatabaseName()).append(" int not null,");
        }

        String sql = sqlBuilder.toString().substring(0, sqlBuilder.length()-1) + ")";
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public HashMap<String, Integer> getQuestsDataFromTable(QuestType questType, UUID uuid, QuestManager questManager) throws SQLException{
        String sql = "SELECT * FROM "+getQuestTableName(questType) + " WHERE uuid = ?";
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());

        ArrayList<Quest> activeQuests = questManager.getCorrespondingQuestArray(questType);
        HashMap<String, Integer> questsData = new HashMap<>();

        ResultSet resultSet = statement.executeQuery();

        if(!resultSet.next()) return null;

        for(Quest quest : activeQuests){

            try{
                int progress = resultSet.getInt(quest.getDatabaseName());
                questsData.put(quest.getDatabaseName(), progress);
            }catch (SQLException e){
                break;
            }

        }

        statement.close();
        connection.close();
        return questsData;
    }

    public void addQuestDataForPlayer(QuestType questType, Player player, QuestManager questManager) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO "+getQuestTableName(questType) + "(uuid, ");
        StringBuilder endSql  = new StringBuilder("( ?, ");

        ArrayList<Quest> activeQuests = questManager.getCorrespondingQuestArray(questType);

        for(Quest quest : activeQuests){
            sql.append(quest.getDatabaseName() + ", ");
            endSql.append("?, ");
        }

        sql.delete(sql.length()-2, sql.length());
        endSql.delete(endSql.length()-2, endSql.length());
        sql.append(") VALUES ");
        sql.append(endSql);
        sql.append(")");


        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(sql.toString());

        statement.setString(1, player.getUniqueId().toString());

        for(int i=0; i < activeQuests.size(); i++){
            statement.setInt( i+2, activeQuests.get(i).getPlayerProgress(player));
        }

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public void updateQuestDataForPlayer(QuestType questType, Player player, QuestManager questManager) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(getQuestTableName(questType)).append(" SET ");

        ArrayList<Quest> allActiveQuests = questManager.getCorrespondingQuestArray(questType);
        for(Quest quest : allActiveQuests){
            sql.append(quest.getDatabaseName() + " = ?, ");
        }


        sql.delete(sql.length()-2, sql.length());
        sql.append(" WHERE uuid = ?");


        String sqlString = sql.toString();

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for(int i=0; i < allActiveQuests.size(); i++){
            Quest quest = allActiveQuests.get(i);
            statement.setInt(i+1, quest.getPlayerProgress(player));
        }

        statement.setString(allActiveQuests.size()+1, player.getUniqueId().toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * It removes the specified quest table
     * @param questType The quest type so it can know which table to remove
     */
    public void removeQuestTable(QuestType questType) throws SQLException {
        String sql = "DROP TABLE IF EXISTS "+getQuestTableName(questType);
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public void updatePlayerTakenAwards(UUID uuid, byte dailyNum, byte weeklyNum, byte cardNum) throws SQLException {
        String sql = "UPDATE quests_awards_data SET daily_quests = ?, weekly_quests = ?, card_quests = ? WHERE uuid = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setByte(1, dailyNum);
        statement.setByte(2, weeklyNum);
        statement.setByte(3, cardNum);
        statement.setString(4, uuid.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();

    }

    public void addPlayerTakenAwards(UUID uuid) throws SQLException {
        String sql = "INSERT INTO quests_awards_data(uuid, daily_quests, weekly_quests, card_quests) VALUES (?, ?, ?, ?)";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());
        statement.setByte(2, (byte) 0);
        statement.setByte(3, (byte) 0);
        statement.setByte(4, (byte) 0);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public ArrayList<Byte> getPlayerTakenAwards(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM quests_awards_data WHERE uuid = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());

        ResultSet results = statement.executeQuery();
        ArrayList<Byte> awards = new ArrayList<>();
        if(results.next()){
            awards.add(results.getByte("daily_quests"));
            awards.add(results.getByte("weekly_quests"));
            awards.add(results.getByte("card_quests"));
        }

        statement.close();
        connection.close();


        return awards;
    }

    private String getQuestTableName(QuestType questTableType){
        switch (questTableType){
            case DAILY -> {
                return  "daily_quests";
            }
            case WEEKLY -> {
                return  "weekly_quests";
            }
            case CARD -> {
                return  "card_quests";
            }
            default -> {
                return  "generic_quests";
            }
        }
    }


}


