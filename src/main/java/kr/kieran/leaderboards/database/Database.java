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

    private final LeaderboardsPlugin plugin;
    private final HikariDataSource dataSource = new HikariDataSource();

    public Database(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
        this.registerProperties();
        try
        {
            this.setupTables();
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.INFO, "Failed to setup necessary tables for the plugin: " + e.getMessage());
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Register all necessary data source properties for Hikari
     * to connect to the database successfully and optimize settings
     * to allow for faster queries and updates.
     */
    private void registerProperties()
    {
        // Log
        plugin.getLogger().log(Level.INFO, "Setting up database environment...");

        // Driver & pool size
        this.dataSource.setMaximumPoolSize(10);
        this.dataSource.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");

        // Credentials
        this.dataSource.addDataSourceProperty("serverName", plugin.getConfig().getString("mysql.host"));
        this.dataSource.addDataSourceProperty("port", plugin.getConfig().getString("mysql.port"));
        this.dataSource.addDataSourceProperty("databaseName", plugin.getConfig().getString("mysql.database"));
        this.dataSource.addDataSourceProperty("user", plugin.getConfig().getString("mysql.user"));
        this.dataSource.addDataSourceProperty("password", plugin.getConfig().getString("mysql.password"));

        // Properties
        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);
        this.dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
    }

    /**
     * Setup the required tables synchronously to ensure they're
     * available before the plugin attempts to use them.
     */
    private void setupTables() throws SQLException
    {
        // Read queries file
        String setup;
        try (InputStream in = LeaderboardsPlugin.class.getClassLoader().getResourceAsStream("dbtables.sql"))
        {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        }
        catch (IOException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Could not read from 'dbtables.sql': " + e.getMessage());
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        // Execute queries
        String[] queries = setup.split(";");
        for (String query : queries)
        {
            if (query.isEmpty()) continue;
            try (Connection connection = this.getConnection(); PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.execute();
            }
        }

        // Log
        plugin.getLogger().log(Level.INFO, "Finished executing preliminary database queries.");
    }

    /**
     * Retrieve a connection from the Hikari connection pool
     *
     * @return a connection to execute queries
     * @throws SQLException if something went wrong throw an exception
     */
    public Connection getConnection() throws SQLException { return this.dataSource.getConnection(); }

    public void disable() { this.dataSource.close(); }

}
