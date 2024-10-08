package me.trololo11.lifespluginseason3.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.PlayerStats;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class manages all data saving and getting from the SQL database.
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
        String databaseName = plugin.getConfig().getString("database-name");

        if(databaseName == null || databaseName.isEmpty()){
            databaseName = "lifes3_database";
        }


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
     * @throws SQLException If there was an error while connecting to the sql database
     */
    public void initialize() throws SQLException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS player_lifes(uuid varchar(36) primary key not null, lifes tinyint not null, is_revived bool not null)");
        statement.execute("CREATE TABLE IF NOT EXISTS quests_awards_data(uuid varchar(36) primary key, daily_quests tinyint not null, weekly_quests tinyint not null, card_quests tinyint not null, weekly_card_taken bool not null)");
        statement.execute("CREATE TABLE IF NOT EXISTS skipped_quests(quest_name varchar(100), quest_type varchar(100))");
        statement.execute("CREATE TABLE IF NOT EXISTS requ_quests(quest_name varchar(100), quest_type varchar(100))");
        statement.execute("CREATE TABLE IF NOT EXISTS player_stats(uuid varchar(36) primary key, kills int, lifes_crafted int, " +
                "revives_crafted int, revived_someone int, all_quest_completed int, daily_quest_completed int, weekly_quest_completed int, card_quest_completed int," +
                "gold_lifes_used int, cards_used int, daily_shards_reedemed int, weekly_shards_reedemed int)");

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
     * Updates already existing row of player lifes with the specified params
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

    /**
     * Returns a list of all the players that have died.
     * @return A list of {@link OfflinePlayer}s that have died in this server.
     * @throws SQLException On database connection error.
     */
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

    /**
     * Sets an is revived value in the database to your choosing. <br>
     * This value represents if the player has been revived by another player
     * and if true it should revive them on join.
     * @param uuid The uuid of the player to set the value for
     * @param isRevived The value to set
     * @throws SQLException On database connection error
     */
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

    /**
     * Gets the is_revived value from the database. <br>
     * If true the player is going to be revived on join.
     * @param uuid The uuid of the player to get the value from.
     * @return The is_revived value.
     * @throws SQLException On database connection error
     */
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

        connection.close();

        return false;
    }

    /**
     * It creates a new quest table that will store all the quests progress of players.
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

        String sql = sqlBuilder.substring(0, sqlBuilder.length()-1) + ")";
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Gets all the quests progress for the specified player.
     * @param questType The type of quests to get the progress for.
     * @param uuid The uuid of the player to get the progress
     * @param questManager An instance of the {@link QuestManager} class
     * @return An HashMap where the key is the database name of the quest
     *         and the value is the quest progress for the player.
     * @throws SQLException On database connection error
     */
    public HashMap<String, Integer> getQuestsDataFromTable(QuestType questType, UUID uuid, QuestManager questManager) throws SQLException{
        String sql = "SELECT * FROM "+getQuestTableName(questType) + " WHERE uuid = ?";
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());

        ArrayList<Quest> activeQuests = questManager.getCorrespondingQuestArray(questType);
        HashMap<String, Integer> questsData = new HashMap<>();

        ResultSet resultSet = statement.executeQuery();

        if(!resultSet.next()){
            connection.close();
            return null;
        }

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

    /**
     * Adds a new record of the player quest progress to the database. <br>
     * The data is read from the {@link QuestManager} class provided and added to the
     * database.
     * @param questType The type of quests to add the progress to.
     * @param player The player to add the quest data for.
     * @param questManager A {@link QuestManager} instance to get the quest data from.
     * @throws SQLException On database connection error
     */
    public void addQuestDataForPlayer(QuestType questType, Player player, QuestManager questManager) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO "+getQuestTableName(questType) + "(uuid, ");
        StringBuilder endSql  = new StringBuilder("( ?, ");

        ArrayList<Quest> activeQuests = questManager.getCorrespondingQuestArray(questType);

        for(Quest quest : activeQuests){
            sql.append(quest.getDatabaseName()).append(", ");
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

    /**
     * This function returns the progress for a specific quests for all the
     * offline players
     * @param quest The quest to get the progress for.
     * @return A hash map of all the player's progress
     */
    public HashMap<OfflinePlayer, Integer> getProgressOfAllOfflinePlayers(Quest quest) throws SQLException {
        StringBuilder notInStringBuilder = new StringBuilder("(");
        HashMap<OfflinePlayer, Integer> offlineProgressHashMap = new HashMap<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            notInStringBuilder.append("'").append(player.getUniqueId()).append("', ");
        }

        String notInString = notInStringBuilder.substring(0, notInStringBuilder.length() - 2);

        notInStringBuilder.append(")");

        String sql = "SELECT uuid," + quest.getDatabaseName() + " FROM " + getQuestTableName(quest.getQuestType()) + " WHERE uuid NOT IN " + notInString + ")";


        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();

        while (results.next()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(results.getString("uuid")));

            offlineProgressHashMap.put(player, results.getInt(quest.getDatabaseName()));
        }

        statement.close();
        connection.close();

        return offlineProgressHashMap;
    }

    /**
     * Updates the quest progress data for an existing player in the database. <br>
     * The data is read from the {@link QuestManager} instance which is provided.
     * @param questType The quest type to update the data for
     * @param player The player to update the data. <b>The player
     *               has to have a record in the database.</b>
     * @param questManager An instance of a {@link QuestManager} to read the data from
     * @throws SQLException On database connection error
     */
    public void updateQuestDataForPlayer(QuestType questType, Player player, QuestManager questManager) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(getQuestTableName(questType)).append(" SET ");

        ArrayList<Quest> allActiveQuests = questManager.getCorrespondingQuestArray(questType);
        for(Quest quest : allActiveQuests){
            sql.append(quest.getDatabaseName()).append(" = ?, ");
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
     * Removes the table where the quest progress is defined.
     * @param questType The quest type to delete the table of.
     */
    public void removeQuestTable(QuestType questType) throws SQLException {
        String sql = "DROP TABLE IF EXISTS "+getQuestTableName(questType);
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Updates an existing player record with the new taken awards data.
     * @param uuid The uuid of the player to update the record. <b>They have to be in the databse</b>
     * @param dailyNum The new daily number of the taken awards.
     * @param weeklyNum The new weekly number of the taken awards.
     * @param cardNum The new card number of taken awards.
     * @throws SQLException On database connection error
     * @see QuestsAwardsManager
     */
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

    /**
     * Sets the specified quest taken awards for the player to 0.
     * @param questType The quest type to set the taken awards
     * @throws SQLException On database connection error
     * @see QuestsAwardsManager
     */
    public void resetPlayerTakenAwards(QuestType questType) throws SQLException {
        String sql = "UPDATE quests_awards_data SET " + getQuestTableName(questType) + " = 0";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    /**
     * Inserts an new record for the player specified with all of the taken values set to 0.
     * @param uuid The uuid of the player to add the record for.
     * @throws SQLException On database connection error
     * @see  QuestsAwardsManager
     */
    public void addPlayerTakenAwards(UUID uuid) throws SQLException {
        String sql = "INSERT INTO quests_awards_data(uuid, daily_quests, weekly_quests, card_quests, weekly_card_taken) VALUES (?, ?, ?, ?, ?)";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());
        statement.setByte(2, (byte) 0);
        statement.setByte(3, (byte) 0);
        statement.setByte(4, (byte) 0);
        statement.setBoolean(5, false);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Sets the value that shows if a player has taken it's weekly random card in the database.
     * @param uuid The uuid of the player to set the value to
     * @param taken If the player taken the weekly random card
     * @throws SQLException On database connection error
     */
    public void setTakenWeeklyCard(UUID uuid, boolean taken) throws SQLException{
        String sql = "UPDATE quests_awards_data SET weekly_card_taken = ? WHERE uuid = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setBoolean(1, taken);
        statement.setString(2, uuid.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Sets the value if players taken their weekly random card to false
     * @throws SQLException On database connection error
     */
    public void resetTakenWeeklyCardForAll() throws SQLException{
        String sql = "UPDATE quests_awards_data SET weekly_card_taken = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setBoolean(1, false);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Gets if the player taken their weekly random card from the database.
     * @param uuid The uuid of the player to get the value from.
     * @return If the player has taken their weekly random card.
     * @throws SQLException On database connection error
     */
    public boolean hasTakenWeeklyCard(UUID uuid) throws SQLException {
        String sql = "SELECT weekly_card_taken FROM quests_awards_data WHERE uuid = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid.toString());

        ResultSet results = statement.executeQuery();

        if(results.next()){
            boolean hasTaken = results.getBoolean("weekly_card_taken");

            connection.close();
            statement.close();

            return hasTaken;
        }

        connection.close();
        statement.close();

        return false;
    }

    /**
     * Gets the values of how many awards has the player taken
     * @param uuid The uuid of the player to get the info from.
     * @return An array of the amount of awards the player has taken where the indexes mean: <br>
     *         0 - amount of awards taken from daily quests <br>
     *         1 - amount of awards taken from weekly quests <br>
     *         2 - amount of awards taken from card quests
     * @throws SQLException On database connection error
     */
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

    /**
     * Sets a specified progress for the specified quest for all of the player in the database.
     * @param quest The quest to set progress to.
     * @param newProgress The new progress to give all the players.
     * @throws SQLException On database connection error
     */
    public void setQuestProgressForAll(Quest quest, int newProgress) throws SQLException {
        String sql = "UPDATE "+getQuestTableName(quest.getQuestType()) + " SET "+quest.getDatabaseName()+" = ?";

        Connection connection = getConnection();
        PreparedStatement statement =  connection.prepareStatement(sql);

        statement.setInt(1, newProgress);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Adds a database name of quest to the skipped quests table
     * which represents all the quests that are currently skipped for everyone.
     * @param quest The quest to add to the skipped quests
     */
    public void addSkippedQuest(Quest quest) throws SQLException {
        String sql = "INSERT INTO skipped_quests(quest_name, quest_type) VALUES (?, ?)";

        Connection connection = getConnection();
        PreparedStatement statement =  connection.prepareStatement(sql);

        statement.setString(1, quest.getDatabaseName());
        statement.setString(2, quest.getQuestType().toString());

        statement.executeUpdate();

        plugin.addSkippedQuest(quest);

        statement.close();
        connection.close();
    }

    /**
     * Gets all of the quests that should be skipped for everyone
     * @param questManager A instance of {@link QuestManager}
     * @return An array list of all quest that are skipped
     */
    public ArrayList<Quest> getAllSkippedQuests(QuestManager questManager) throws SQLException {
        String sql = "SELECT * FROM skipped_quests";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        ArrayList<Quest> allSkippedQuests = new ArrayList<>();
        ResultSet results = statement.executeQuery();

        while(results.next()){
            QuestType questType = QuestType.valueOf(results.getString("quest_type"));
            Quest quest = questManager.getQuestByDatabaseName(questType,results.getString("quest_name"));

            if(quest != null) allSkippedQuests.add(quest);
        }

        connection.close();

        return allSkippedQuests;
    }

    /**
     * Removes every skipped quest saved in this table that have the
     * specified {@link QuestType}.
     * @param questType The quest type to delete
     */
    public void removeAllSkippedQuests(QuestType questType) throws SQLException {
        String sql = "DELETE FROM skipped_quests WHERE quest_type = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, questType.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Changes the name of a column in the corresponding quest database. <br>
     * It should be only used to change the name of a quest progress column
     * @param questType The {@link QuestType} of the table to modify
     * @param previousName The previous name of a quest
     * @param newName The name to change the column to
     * @throws SQLException On error with the database
     */
    public void changeNameOfQuestColumn(QuestType questType, String previousName, String newName) throws SQLException {
        String sql = "ALTER TABLE "+getQuestTableName(questType) + " CHANGE "+previousName + " " + newName + " int";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Gets all of the column names from the table which corresponds to the specified all of the {@link QuestType}
     * @param questType The type of quests to get name of the columns from
     * @return A list of the names of columns.
     * @throws SQLException
     */
    public ArrayList<String> getAllQuestColumnNames(QuestType questType) throws SQLException {
        String sql = "SHOW COLUMNS FROM "+getQuestTableName(questType);

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();
        ArrayList<String> columnNames = new ArrayList<>();
        results.next(); //We skip the first column bcs we know its gonna be the uuid column which is uselles info

        while (results.next()) {
            columnNames.add(results.getString(1));
        }

        statement.close();
        connection.close();

        return columnNames;
    }

    /**
     * Adds this quest as having halved requirements to the database
     * @param quest The quest to add
     * @throws SQLException On database connection error
     */
    public void addRequirementsQuests(Quest quest) throws SQLException {
        String sql = "INSERT INTO requ_quests(quest_name, quest_type) VALUES (?, ?)";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, quest.getDatabaseName());
        statement.setString(2, quest.getQuestType().toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Removes every occurrence of a quest which requirements where halved
     * of the specified quest type from the database
     * @param questType The quest type to check
     * @throws SQLException On database connection error
     */
    public void removeAllRequirementsType(QuestType questType) throws SQLException {
        String sql = "DELETE FROM requ_quests WHERE quest_type = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, questType.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }


    /**
     * Gets all the quests that have their max progress cut in half. <br>
     * @return A hashMap where the key is the quest type of the specified quests, and it's
     *         value is an array list of all the database names that have been halved
     * @throws SQLException On database connection error
     */
    public HashMap<QuestType, ArrayList<String>> getAllQuestHalfed() throws SQLException {
        String sql = "SELECT * FROM requ_quests";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();
        HashMap<QuestType, ArrayList<String>> databaseNamesMap = new HashMap<>();

        for(QuestType questType : QuestType.values()){
            databaseNamesMap.put(questType, new ArrayList<>());
        }

        while (results.next()){

            databaseNamesMap.get( QuestType.valueOf(results.getString("quest_type")) ).add( results.getString("quest_name") );

        }

        connection.close();

        return databaseNamesMap;
    }

    /**
     * Gets the player's stats that are saved in the database.
     * @param player The player to get the stats for.
     * @return A new instance of {@link PlayerStats} with the stats values from the database.
     * @throws SQLException On database connection error
     */
    public PlayerStats getPlayerStats(Player player) throws SQLException {
        String sql = "SELECT * FROM player_stats WHERE uuid = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, player.getUniqueId().toString());

        ResultSet results = statement.executeQuery();

        if(results.next()){

            PlayerStats playerStats = new PlayerStats(
                    player,
                    results.getInt("kills"),
                    results.getInt("lifes_crafted"),
                    results.getInt("revives_crafted"),
                    results.getInt("revived_someone"),
                    results.getInt("all_quest_completed"),
                    results.getInt("daily_quest_completed"),
                    results.getInt("weekly_quest_completed"),
                    results.getInt("card_quest_completed"),
                    results.getInt("gold_lifes_used"),
                    results.getInt("cards_used"),
                    results.getInt("daily_shards_reedemed"),
                    results.getInt("weekly_shards_reedemed")
            );

            connection.close();
            statement.close();

            return playerStats;

        }

        connection.close();
        statement.close();

        return null;

    }

    /**
     * Inserts a new record of player's stats to the database.<br>
     * Should be used if the player doesn't have a record in the database.
     * @param playerStats The player stats to save.
     * @throws SQLException On database connection error
     */
    public void addPlayerStats(PlayerStats playerStats) throws SQLException {
        String sql = "INSERT INTO player_stats VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, playerStats.owner.getUniqueId().toString());
        statement.setInt(2, playerStats.kills);
        statement.setInt(3, playerStats.lifesCrafted);
        statement.setInt(4, playerStats.revivesCrafted);
        statement.setInt(5, playerStats.revivedSomeone);
        statement.setInt(6, playerStats.allQuestCompleted);
        statement.setInt(7, playerStats.dailyQuestCompleted);
        statement.setInt(8, playerStats.weeklyQuestCompleted);
        statement.setInt(9, playerStats.cardQuestCompleted);
        statement.setInt(10, playerStats.goldLifesUsed);
        statement.setInt(11, playerStats.cardsUsed);
        statement.setInt(12, playerStats.dailyShardsRedeemed);
        statement.setInt(13, playerStats.weeklyShardRedeemed);

        statement.executeUpdate();

        connection.close();
        statement.close();

    }

    /**
     * Updates player's stats values in the database. <br>
     * Should be used only if the player has an record in the database.
     * @param playerStats The player stats to update to
     * @throws SQLException On database connection error
     */
    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        String sql = "UPDATE player_stats SET kills = ?, lifes_crafted = ?, revives_crafted = ?, revived_someone = ?," +
                "all_quest_completed = ?, daily_quest_completed = ?, weekly_quest_completed = ?, card_quest_completed = ?, gold_lifes_used = ?, cards_used = ?," +
                "daily_shards_reedemed = ?, weekly_shards_reedemed = ? WHERE uuid = ?";

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, playerStats.kills);
        statement.setInt(2, playerStats.lifesCrafted);
        statement.setInt(3, playerStats.revivesCrafted);
        statement.setInt(4, playerStats.revivedSomeone);
        statement.setInt(5, playerStats.allQuestCompleted);
        statement.setInt(6, playerStats.dailyQuestCompleted);
        statement.setInt(7, playerStats.weeklyQuestCompleted);
        statement.setInt(8, playerStats.cardQuestCompleted);
        statement.setInt(9, playerStats.goldLifesUsed);
        statement.setInt(10, playerStats.cardsUsed);
        statement.setInt(11, playerStats.dailyShardsRedeemed);
        statement.setInt(12, playerStats.weeklyShardRedeemed);
        statement.setString(13, playerStats.owner.getUniqueId().toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public void removeAllQuestValues(QuestType questType) throws SQLException {
        removeAllSkippedQuests(questType);
        removeAllRequirementsType(questType);
        removeQuestTable(questType);
    }

    /**
     * Returns the name of the table where the corresponding quests scores are stored
     * in the database.
     * @param questTableType The type of quest to get the table name for.
     * @return The name of the table.
     */
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