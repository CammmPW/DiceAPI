package org.dicemc.diceapi.json;

import java.util.Iterator;

public class XML {
    public static final Character AMP = '&';

    public static final Character APOS = '\'';

    public static final Character BANG = '!';

    public static final Character EQ = '=';

    public static final Character GT = '>';

    public static final Character LT = '<';

    public static final Character QUEST = '?';

    public static final Character QUOT = '"';

    public static final Character SLASH = '/';

    public static String escape(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = string.length(); i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static void noSpace(String string) throws JSONException {
        int length = string.length();
        if (length == 0)
            throw new JSONException("Empty string.");
        for (int i = 0; i < length; i++) {
            if (Character.isWhitespace(string.charAt(i)))
                throw new JSONException("'" + string +
                        "' contains a space character.");
        }
    }

    private static boolean parse(XMLTokener x, JSONObject context, String name) throws JSONException {
        JSONObject jsonobject;
        Object token = x.nextToken();
        if (token == BANG) {
            char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                token = x.nextToken();
                if ("CDATA".equals(token) &&
                        x.next() == '[') {
                    String string = x.nextCDATA();
                    if (string.length() > 0)
                        context.accumulate("content", string);
                    return false;
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            int i = 1;
            do {
                token = x.nextMeta();
                if (token == null)
                    throw x.syntaxError("Missing '>' after '<!'.");
                if (token == LT) {
                    i++;
                } else if (token == GT) {
                    i--;
                }
            } while (i > 0);
            return false;
        }
        if (token == QUEST) {
            x.skipPast("?>");
            return false;
        }
        if (token == SLASH) {
            token = x.nextToken();
            if (name == null)
                throw x.syntaxError("Mismatched close tag " + token);
            if (!token.equals(name))
                throw x.syntaxError("Mismatched " + name + " and " + token);
            if (x.nextToken() != GT)
                throw x.syntaxError("Misshaped close tag");
            return true;
        }
        if (token instanceof Character)
            throw x.syntaxError("Misshaped tag");
        String tagName = (String) token;
        token = null;
        jsonobject = new JSONObject();
        while (true) {
            if (token == null)
                token = x.nextToken();
            if (token instanceof String) {
                String string = (String) token;
                token = x.nextToken();
                if (token == EQ) {
                    token = x.nextToken();
                    if (!(token instanceof String))
                        throw x.syntaxError("Missing value");
                    jsonobject.accumulate(string,
                            stringToValue((String) token));
                    token = null;
                    continue;
                }
                jsonobject.accumulate(string, "");
                continue;
            }
            break;
        }
        if (token == SLASH) {
            if (x.nextToken() != GT)
                throw x.syntaxError("Misshaped tag");
            if (jsonobject.length() > 0) {
                context.accumulate(tagName, jsonobject);
            } else {
                context.accumulate(tagName, "");
            }
            return false;
        }
        if (token == GT)
            while (true) {
                token = x.nextContent();
                if (token == null) {
                    if (tagName != null)
                        throw x.syntaxError("Unclosed tag " + tagName);
                    return false;
                }
                if (token instanceof String) {
                    String string = (String) token;
                    if (string.length() > 0)
                        jsonobject.accumulate("content",
                                stringToValue(string));
                    continue;
                }
                if (token == LT &&
                        parse(x, jsonobject, tagName)) {
                    if (jsonobject.length() == 0) {
                        context.accumulate(tagName, "");
                    } else if (jsonobject.length() == 1 &&
                            jsonobject.opt("content") != null) {
                        context.accumulate(tagName,
                                jsonobject.opt("content"));
                    } else {
                        context.accumulate(tagName, jsonobject);
                    }
                    return false;
                }
            }
        throw x.syntaxError("Misshaped tag");
    }

    public static Object stringToValue(String string) {
        if ("".equals(string))
            return string;
        if ("true".equalsIgnoreCase(string))
            return Boolean.TRUE;
        if ("false".equalsIgnoreCase(string))
            return Boolean.FALSE;
        if ("null".equalsIgnoreCase(string))
            return JSONObject.NULL;
        if ("0".equals(string))
            return 0;
        try {
            char initial = string.charAt(0);
            boolean negative = false;
            if (initial == '-') {
                initial = string.charAt(1);
                negative = true;
            }
            if (initial == '0' && string.charAt(negative ? 2 : 1) == '0')
                return string;
            if (initial >= '0' && initial <= '9') {
                if (string.indexOf('.') >= 0)
                    return Double.valueOf(string);
                if (string.indexOf('e') < 0 && string.indexOf('E') < 0) {
                    Long myLong = new Long(string);
                    if (myLong == myLong.intValue())
                        return myLong.intValue();
                    return myLong;
                }
            }
        } catch (Exception exception) {
        }
        return string;
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        while (x.more() && x.skipPast("<"))
            parse(x, jo, null);
        return jo;
    }

    public static String toString(Object object) throws JSONException {
        return toString(object, null);
    }

    public static String toString(Object object, String tagName) throws JSONException {
        StringBuilder sb = new StringBuilder();
        if (object instanceof JSONObject) {
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }
            JSONObject jo = (JSONObject) object;
            Iterator keys = jo.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                Object value = jo.opt(key);
                if (value == null)
                    value = "";
                if (value instanceof String) {
                    String str = (String) value;
                } else {
                    String str = null;
                }
                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        JSONArray ja = (JSONArray) value;
                        int length = ja.length();
                        for (int i = 0; i < length; i++) {
                            if (i > 0)
                                sb.append('\n');
                            sb.append(escape(ja.get(i).toString()));
                        }
                        continue;
                    }
                    sb.append(escape(value.toString()));
                    continue;
                }
                if (value instanceof JSONArray) {
                    JSONArray ja = (JSONArray) value;
                    int length = ja.length();
                    for (int i = 0; i < length; i++) {
                        value = ja.get(i);
                        if (value instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(toString(value));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                        } else {
                            sb.append(toString(value, key));
                        }
                    }
                    continue;
                }
                if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");
                    continue;
                }
                sb.append(toString(value, key));
            }
            if (tagName != null) {
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();
        }
        if (object.getClass().isArray())
            object = new JSONArray(object);
        if (object instanceof JSONArray) {
            JSONArray ja = (JSONArray) object;
            int length = ja.length();
            for (int i = 0; i < length; i++)
                sb.append(toString(ja.opt(i), (tagName == null) ? "array" : tagName));
            return sb.toString();
        }
        String string = escape(object.toString());
        return (tagName == null) ? ("\"" + string + "\"") : (
                (string.length() == 0) ? ("<" + tagName + "/>") : (
                        "<" + tagName + ">" + string + "</" + tagName + ">"));
    }
}
