package todo.spielesammlungprototyp.model.util;

import android.view.animation.Animation;

public abstract class AnimationEndListener implements Animation.AnimationListener {

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public abstract void onAnimationEnd(Animation animation);

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
