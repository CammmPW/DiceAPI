package org.dicemc.diceapi.api.serialization;

import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Serializer {
    public static String toString(JSONObject object) {
        return toString(object, true);
    }

    public static String toString(JSONObject object, boolean pretty) {
        return toString(object, pretty, 5);
    }

    public static String toString(JSONObject object, boolean pretty, int tabSize) {
        try {
            if (pretty)
                return object.toString(tabSize);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getObjectFromFile(File file) throws FileNotFoundException, JSONException {
        return getObjectFromStream(new FileInputStream(file));
    }

    public static JSONObject getObjectFromStream(InputStream stream) throws JSONException {
        return new JSONObject(getStringFromStream(stream));
    }

    public static String getStringFromFile(File file) throws FileNotFoundException {
        return getStringFromStream(new FileInputStream(file));
    }

    public static String getStringFromStream(InputStream stream) {
        Scanner x = new Scanner(stream);
        StringBuilder str = new StringBuilder();
        while (x.hasNextLine())
            str.append(x.nextLine()).append("\n");
        x.close();
        return str.toString().trim();
    }
}
