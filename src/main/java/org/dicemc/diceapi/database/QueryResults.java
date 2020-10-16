package org.dicemc.diceapi.database;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

public class QueryResults implements Iterable<QueryResultsRow> {
    private final ArrayList<QueryResultsRow> values = new ArrayList<>();

    public QueryResults(ResultSet set) {
        ArrayList<String> columns = new ArrayList<>();
        try {
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++)
                columns.add(set.getMetaData().getColumnName(i));
            while (set.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (String column : columns)
                    row.add(set.getObject(column));
                this.values.add(new QueryResultsRow(columns, row));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getBoolean(columnName);
    }

    public double getDouble(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getDouble(columnName);
    }

    public float getFloat(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getFloat(columnName);
    }

    public int getInteger(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getInteger(columnName);
    }

    public long getLong(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getLong(columnName);
    }

    public Object getObject(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getObject(columnName);
    }

    public short getShort(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getShort(columnName);
    }

    public String getString(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getString(columnName);
    }

    public Timestamp getTimestamp(int index, String columnName) throws DatabaseException {
        return this.values.get(index).getTimestamp(columnName);
    }

    public boolean hasRows() {
        return (rowCount() > 0);
    }

    public int rowCount() {
        return this.values.size();
    }

    public @NotNull Iterator<QueryResultsRow> iterator() {
        return this.values.iterator();
    }
}
