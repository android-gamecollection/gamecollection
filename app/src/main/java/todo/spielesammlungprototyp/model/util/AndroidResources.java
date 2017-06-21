package todo.spielesammlungprototyp.model.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import todo.spielesammlungprototyp.App;

public final class AndroidResources {

    private AndroidResources() {
    }

    /**
     * Converts a resource from string-form to integer-form
     *
     * @param resourceStr The resource as string in the form of '@mipmap/ic_launcher'
     * @return The resource in the form of R.mipmap.ic_launcher
     */
    public static int getResourceIDFromString(String resourceStr) {
        Context context = App.getContext();
        if (resourceStr.startsWith("@")) resourceStr = resourceStr.substring(1);
        String[] splitString = resourceStr.split("/");
        return context.getResources().getIdentifier(splitString[1], splitString[0], context.getPackageName());
    }

    /**
     * Looks up string resource from string
     *
     * @param resourceStr The string in form of '@string/text'
     * @return The dereferenced string
     */
    public static String getResourceString(String resourceStr) {
        Context context = App.getContext();
        if (!resourceStr.startsWith("@string/")) return resourceStr;
        int identifier = getResourceIDFromString(resourceStr);
        return context.getString(identifier);
    }

    public static int getColor(int color) {
        return ContextCompat.getColor(App.getContext(), color);
    }
}
