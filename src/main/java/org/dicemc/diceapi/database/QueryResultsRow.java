package org.dicemc.diceapi.database;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class QueryResultsRow {
    private String[] columns;

    private Object[] data;

    public QueryResultsRow(String[] columns, Object[] data) {
        this.data = data;
        this.columns = columns;
    }

    public QueryResultsRow(ArrayList<String> columns, ArrayList<Object> data) {
        this.data = data.toArray();
        this.columns = columns.<String>toArray(new String[0]);
    }

    public boolean getBoolean(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Boolean)
            return ((Boolean)obj).booleanValue();
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Boolean");
    }

    public Date getDate(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Date)
            return (Date)obj;
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Date");
    }

    public double getDouble(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Double)
            return ((Double)obj).doubleValue();
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Double");
    }

    public float getFloat(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Float)
            return ((Float)obj).floatValue();
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Float");
    }

    public int getInteger(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Integer)
            return ((Integer)obj).intValue();
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Integer");
    }

    public long getLong(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Long)
            return ((Long)obj).longValue();
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Long");
    }

    public short getShort(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Short)
            return ((Short)obj).shortValue();
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Short");
    }

    public String getString(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof String)
            return (String)obj;
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof String");
    }

    public Timestamp getTimestamp(String columnName) throws DatabaseException {
        Object obj = getObject(columnName);
        if (obj instanceof Timestamp)
            return (Timestamp)obj;
        throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Timestamp");
    }

    public Object getObject(String columnName) throws DatabaseException {
        int col = getColumn(columnName);
        if (col == -1)
            throw new DatabaseException("Column with the name of '" + columnName + "' not found");
        return this.data[col];
    }

    private int getColumn(String name) {
        for (int i = 0; i < this.columns.length; i++) {
            if (this.columns[i].equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }
}
