package todo.spielesammlungprototyp.util;

import android.content.Context;

public final class AndroidResources {

    private AndroidResources() {
    }

    /**
     * Converts a resource from string-form to integer-form
     *
     * @param resourceStr The resource as string in the form of '@mimap/ic_launcher'
     * @return The resource in the form of R.mipmap.ic_launcher
     */
    public static int getResourceIDFromString(Context context, String resourceStr) {
        if (resourceStr.startsWith("@")) resourceStr = resourceStr.substring(1);
        String[] splitString = resourceStr.split("/");
        return context.getResources().getIdentifier(splitString[1], splitString[0], context.getPackageName());
    }

    /**
     * Looks up string resource from string
     *
     * @param context     Context
     * @param resourceStr The string in form of '@string/text'
     * @return The dereferenced string
     */
    public static String getResourceString(Context context, String resourceStr) {
        if (!resourceStr.startsWith("@string/")) return resourceStr;
        int identifier = getResourceIDFromString(context, resourceStr);
        return context.getString(identifier);
    }
}
