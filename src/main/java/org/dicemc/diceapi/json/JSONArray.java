package org.dicemc.diceapi.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JSONArray {
    private final ArrayList<Object> myArrayList;

    public JSONArray() {
        this.myArrayList = new ArrayList<>();
    }

    public JSONArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[')
            throw x.syntaxError("A JSONArray text must start with '['");
        if (x.nextClean() != ']') {
            x.back();
            while (true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.myArrayList.add(JSONObject.NULL);
                } else {
                    x.back();
                    this.myArrayList.add(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ',':
                    case ';':
                        if (x.nextClean() == ']')
                            return;
                        x.back();
                        continue;
                    case ']':
                        return;
                }
                break;
            }
            throw x.syntaxError("Expected a ',' or ']'");
        }
    }

    public JSONArray(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONArray(Collection collection) {
        this.myArrayList = new ArrayList();
        if (collection != null) {
            for (Object o : collection) this.myArrayList.add(JSONObject.wrap(o));
        }
    }

    public JSONArray(Object array) throws JSONException {
        this();
        if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++)
                put(JSONObject.wrap(Array.get(array, i)));
        } else {
            throw new JSONException(
                    "JSONArray initial value should be a string or collection or array.");
        }
    }

    public Object get(int index) throws JSONException {
        Object object = opt(index);
        if (object == null)
            throw new JSONException("JSONArray[" + index + "] not found.");
        return object;
    }

    public boolean getBoolean(int index) throws JSONException {
        Object object = get(index);
        if (object.equals(Boolean.FALSE) || (
                object instanceof String && (
                        (String)object).equalsIgnoreCase("false")))
            return false;
        if (object.equals(Boolean.TRUE) || (
                object instanceof String && (
                        (String)object).equalsIgnoreCase("true")))
            return true;
        throw new JSONException("JSONArray[" + index + "] is not a boolean.");
    }

    public double getDouble(int index) throws JSONException {
        Object object = get(index);
        try {
            return (object instanceof Number) ? (
                    (Number)object).doubleValue() :
                    Double.parseDouble((String)object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index +
                    "] is not a number.");
        }
    }

    public int getInt(int index) throws JSONException {
        Object object = get(index);
        try {
            return (object instanceof Number) ? (
                    (Number)object).intValue() :
                    Integer.parseInt((String)object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index +
                    "] is not a number.");
        }
    }

    public JSONArray getJSONArray(int index) throws JSONException {
        Object object = get(index);
        if (object instanceof JSONArray)
            return (JSONArray)object;
        throw new JSONException("JSONArray[" + index +
                "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(int index) throws JSONException {
        Object object = get(index);
        if (object instanceof JSONObject)
            return (JSONObject)object;
        throw new JSONException("JSONArray[" + index +
                "] is not a JSONObject.");
    }

    public long getLong(int index) throws JSONException {
        Object object = get(index);
        try {
            return (object instanceof Number) ? (
                    (Number)object).longValue() :
                    Long.parseLong((String)object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index +
                    "] is not a number.");
        }
    }

    public String getString(int index) throws JSONException {
        Object object = get(index);
        if (object instanceof String)
            return (String)object;
        throw new JSONException("JSONArray[" + index + "] not a string.");
    }

    public boolean isNull(int index) {
        return JSONObject.NULL.equals(opt(index));
    }

    public String join(String separator) throws JSONException, IOException {
        int len = length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 0)
                sb.append(separator);
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }

    public int length() {
        return this.myArrayList.size();
    }

    public Object opt(int index) {
        return (index < 0 || index >= length()) ?
                null :
                this.myArrayList.get(index);
    }

    public boolean optBoolean(int index) {
        return optBoolean(index, false);
    }

    public boolean optBoolean(int index, boolean defaultValue) {
        try {
            return getBoolean(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(int index) {
        return optDouble(index, Double.NaN);
    }

    public double optDouble(int index, double defaultValue) {
        try {
            return getDouble(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(int index) {
        return optInt(index, 0);
    }

    public int optInt(int index, int defaultValue) {
        try {
            return getInt(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(int index) {
        Object o = opt(index);
        return (o instanceof JSONArray) ? (JSONArray)o : null;
    }

    public JSONObject optJSONObject(int index) {
        Object o = opt(index);
        return (o instanceof JSONObject) ? (JSONObject)o : null;
    }

    public long optLong(int index) {
        return optLong(index, 0L);
    }

    public long optLong(int index, long defaultValue) {
        try {
            return getLong(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(int index) {
        return optString(index, "");
    }

    public String optString(int index, String defaultValue) {
        Object object = opt(index);
        return JSONObject.NULL.equals(object) ?
                defaultValue :
                object.toString();
    }

    public JSONArray put(boolean value) {
        put(value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(Collection value) {
        put(new JSONArray(value));
        return this;
    }

    public JSONArray put(double value) throws JSONException {
        Double d = value;
        JSONObject.testValidity(d);
        put(d);
        return this;
    }

    public JSONArray put(int value) {
        put(new Integer(value));
        return this;
    }

    public JSONArray put(long value) {
        put(new Long(value));
        return this;
    }

    public JSONArray put(Map value) {
        put(new JSONObject(value));
        return this;
    }

    public JSONArray put(Object value) {
        this.myArrayList.add(value);
        return this;
    }

    public JSONArray put(int index, boolean value) throws JSONException {
        put(index, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(int index, Collection value) throws JSONException {
        put(index, new JSONArray(value));
        return this;
    }

    public JSONArray put(int index, double value) throws JSONException {
        put(index, new Double(value));
        return this;
    }

    public JSONArray put(int index, int value) throws JSONException {
        put(index, new Integer(value));
        return this;
    }

    public JSONArray put(int index, long value) throws JSONException {
        put(index, new Long(value));
        return this;
    }

    public JSONArray put(int index, Map value) throws JSONException {
        put(index, new JSONObject(value));
        return this;
    }

    public JSONArray put(int index, Object value) throws JSONException {
        JSONObject.testValidity(value);
        if (index < 0)
            throw new JSONException("JSONArray[" + index + "] not found.");
        if (index < length()) {
            this.myArrayList.set(index, value);
        } else {
            while (index != length())
                put(JSONObject.NULL);
            put(value);
        }
        return this;
    }

    public Object remove(int index) {
        Object o = opt(index);
        this.myArrayList.remove(index);
        return o;
    }

    public JSONObject toJSONObject(JSONArray names) throws JSONException {
        if (names == null || names.length() == 0 || length() == 0)
            return null;
        JSONObject jo = new JSONObject();
        for (int i = 0; i < names.length(); i++)
            jo.put(names.getString(i), opt(i));
        return jo;
    }

    public String toString() {
        try {
            return toString(0);
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            return write(sw, indentFactor, 0).toString();
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return write(writer, 0, 0);
    }

    Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean commanate = false;
            int length = length();
            writer.write(91);
            if (length == 1) {
                JSONObject.writeValue(writer, this.myArrayList.get(0),
                        indentFactor, indent);
            } else if (length != 0) {
                int newindent = indent + indentFactor;
                for (int i = 0; i < length; i++) {
                    if (commanate)
                        writer.write(44);
                    if (indentFactor > 0)
                        writer.write(10);
                    JSONObject.indent(writer, newindent);
                    JSONObject.writeValue(writer, this.myArrayList.get(i),
                            indentFactor, newindent);
                    commanate = true;
                }
                if (indentFactor > 0)
                    writer.write(10);
                JSONObject.indent(writer, indent);
            }
            writer.write(93);
            return writer;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
}
