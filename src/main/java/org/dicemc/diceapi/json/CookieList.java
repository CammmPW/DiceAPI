package org.dicemc.diceapi.json;

import java.io.IOException;
import java.util.Iterator;

public class CookieList {
    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        JSONTokener x = new JSONTokener(string);
        while (x.more()) {
            String name = Cookie.unescape(x.nextTo('='));
            x.next('=');
            jo.put(name, Cookie.unescape(x.nextTo(';')));
            x.next();
        }
        return jo;
    }

    public static <E> String toString(JSONObject jo) throws JSONException, IOException {
        boolean b = false;
        Iterator<E> keys = jo.keys();
        StringBuffer sb = new StringBuffer();
        while (keys.hasNext()) {
            String string = keys.next().toString();
            if (!jo.isNull(string)) {
                if (b)
                    sb.append(';');
                sb.append(Cookie.escape(string));
                sb.append("=");
                sb.append(Cookie.escape(jo.getString(string)));
                b = true;
            }
        }
        return sb.toString();
    }
}
