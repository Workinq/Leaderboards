package kr.kieran.leaderboards.database;

import com.zaxxer.hikari.HikariDataSource;
import kr.kieran.leaderboards.LeaderboardsPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Database
{

    // Constants
    private static final String DB_TABLES_FILE = "dbtables.sql";

    private final LeaderboardsPlugin plugin;
    private final HikariDataSource dataSource = new HikariDataSource();

    public Database(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
        this.registerProperties();
        this.setupTables();
    }

    /**
     * Register all necessary data source properties for Hikari
     * to connect to the database successfully and optimize settings
     * to allow for faster queries and updates.
     */
    private void registerProperties()
    {
        // Driver & pool size
        dataSource.setMaximumPoolSize(8);
        dataSource.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");

        // Credentials
        dataSource.addDataSourceProperty("serverName", plugin.getConfig().getString("mysql.host"));
        dataSource.addDataSourceProperty("port", plugin.getConfig().getString("mysql.port"));
        dataSource.addDataSourceProperty("databaseName", plugin.getConfig().getString("mysql.database"));
        dataSource.addDataSourceProperty("user", plugin.getConfig().getString("mysql.user"));
        dataSource.addDataSourceProperty("password", plugin.getConfig().getString("mysql.password"));

        // Properties
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);
        dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
    }

    /**
     * Setup the required tables synchronously to ensure they're
     * available before the plugin attempts to use them.
     */
    private void setupTables()
    {
        // Read queries file
        String setup;
        try (InputStream in = LeaderboardsPlugin.class.getClassLoader().getResourceAsStream(DB_TABLES_FILE))
        {
            if (in == null) throw new IOException("The input stream for the file " + DB_TABLES_FILE + " is null");
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        }
        catch (IOException e)
        {
            throw new BadTableFileException("Could not read the file '" + DB_TABLES_FILE + "'", e);
        }

        // Execute queries
        String[] queries = setup.split(";");
        for (String query : queries)
        {
            if (query.isEmpty()) continue;
            try (
                    Connection connection = this.getConnection();
                    PreparedStatement statement = connection.prepareStatement(query)
            )
            {
                statement.execute();
            }
            catch (SQLException e)
            {
                // This will prevent the plugin from enabling, so we don't need to call PluginManager#disablePlugin(Plugin)
                throw new FailedStatementException("Failed to setup the necessary tables for the plugin", e);
            }
        }

        // Log
        plugin.getLogger().log(Level.INFO, "Finished executing preliminary database queries.");
    }

    private static class BadTableFileException extends RuntimeException
    {

        public BadTableFileException(String message, Throwable throwable)
        {
            super(message, throwable);
        }

    }

    private static class FailedStatementException extends RuntimeException
    {

        public FailedStatementException(String message, Throwable throwable)
        {
            super(message, throwable);
        }

    }

    /**
     * Retrieve a connection from the Hikari connection pool
     *
     * @return a connection to execute queries
     * @throws SQLException if something went wrong throw an exception
     */
    public Connection getConnection() throws SQLException { return dataSource.getConnection(); }

    public void disable() { dataSource.close(); }

}
