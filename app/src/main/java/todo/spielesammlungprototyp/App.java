package todo.spielesammlungprototyp;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class App extends Application {

    // WeakReference can be garbage collected
    private static WeakReference<Context> mContext;

    public static Context getContext() {
        return mContext.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<Context>(this);
    }
}
