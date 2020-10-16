package org.dicemc.diceapi.database;

import org.dicemc.diceapi.DiceAPI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private Connection conn;

    public Database() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception exception) {}
        connect();
    }

    private void connect() throws SQLException {
        this.conn = DriverManager.getConnection(getConnectionString());
    }

    private void ensureConnection() {
        try {
            if (!this.conn.isValid(5))
                connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getConnectionString() {
        return "jdbc:mysql://" + DiceAPI.config.getMySqlServerAddress() + ":" +
                DiceAPI.config.getMySqlServerPort() + "/" +
                DiceAPI.config.getDatabaseName() + "?user=" +
                DiceAPI.config.getDatabaseUsername() + "&password=" +
                DiceAPI.config.getDatabasePassword();
    }

    private PreparedStatement prepareStatement(String sql, Object[] params) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        int counter = 1;
        byte b;
        int i;
        Object[] arrayOfObject;
        for (i = (arrayOfObject = params).length, b = 0; b < i; ) {
            Object param = arrayOfObject[b];
            if (param instanceof Integer) {
                stmt.setInt(counter++, ((Integer)param).intValue());
            } else if (param instanceof Short) {
                stmt.setShort(counter++, ((Short)param).shortValue());
            } else if (param instanceof Long) {
                stmt.setLong(counter++, ((Long)param).longValue());
            } else if (param instanceof Double) {
                stmt.setDouble(counter++, ((Double)param).doubleValue());
            } else if (param instanceof String) {
                stmt.setString(counter++, (String)param);
            } else if (param == null) {
                stmt.setNull(counter++, 0);
            } else if (param instanceof Object) {
                stmt.setObject(counter++, param);
            } else {
                System.out.printf("Database -> Unsupported data type %s", new Object[] { param.getClass().getSimpleName() });
            }
            b++;
        }
        return stmt;
    }

    public QueryResults read(String sql, Object... params) {
        ensureConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        QueryResults results = null;
        try {
            stmt = prepareStatement(sql, params);
            rs = stmt.executeQuery();
            if (rs != null)
                results = new QueryResults(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return results;
    }

    public void write(String sql, Object... params) {
        try {
            ensureConnection();
            PreparedStatement stmt = prepareStatement(sql, params);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
