package todo.spielesammlungprototyp.model.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Menu;

import todo.spielesammlungprototyp.App;
import todo.spielesammlungprototyp.R;

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
    public static String getResourceString(@Nullable String resourceStr) {
        if (resourceStr == null || !resourceStr.startsWith("@string/")) return resourceStr;
        Context context = App.getContext();
        int identifier = getResourceIDFromString(resourceStr);
        return context.getString(identifier);
    }

    /**
     * Provides access to getColor without worrying about the context
     *
     * @param color The id of the color
     * @return The color
     */
    public static int getColor(int color) {
        return ContextCompat.getColor(App.getContext(), color);
    }

    /**
     * Change action bar icon color based on text color of current action bar theme
     *
     * @param menu The menu
     */
    public static void colorMenuItems(Menu menu) {
        TypedArray typedArray = App.getContext().obtainStyledAttributes(
                R.style.AppTheme_AppBarOverlay, new int[]{android.R.attr.textColorPrimary});
        int themedColor = typedArray.getColor(0, Color.RED);

        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(themedColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        typedArray.recycle();
    }
}
