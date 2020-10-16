package org.dicemc.diceapi.database;

import org.dicemc.diceapi.DiceAPI;

import java.sql.*;

public class Database {
    private Connection conn;

    public Database() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception exception) {
        }
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
                stmt.setInt(counter++, (Integer) param);
            } else if (param instanceof Short) {
                stmt.setShort(counter++, (Short) param);
            } else if (param instanceof Long) {
                stmt.setLong(counter++, (Long) param);
            } else if (param instanceof Double) {
                stmt.setDouble(counter++, (Double) param);
            } else if (param instanceof String) {
                stmt.setString(counter++, (String) param);
            } else if (param == null) {
                stmt.setNull(counter++, 0);
            } else {
                stmt.setObject(counter++, param);
            }
            b++;
        }
        return stmt;
    }

    public QueryResults read(String sql, Object... params) {
        ensureConnection();
        QueryResults results = null;
        try (PreparedStatement ps = prepareStatement(sql, params)) {
            ResultSet rs = ps.executeQuery();
            if (rs != null)
                results = new QueryResults(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return results;
    }

    public void write(String sql, Object... params) {
        ensureConnection();
        try (PreparedStatement ps = prepareStatement(sql, params)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
