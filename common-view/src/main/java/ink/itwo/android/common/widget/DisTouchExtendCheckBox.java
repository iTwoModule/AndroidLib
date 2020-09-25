package ink.itwo.android.common.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wang on 2020/9/18.
 */
public class DisTouchExtendCheckBox  extends ExtendCheckBox{
    public DisTouchExtendCheckBox(Context context) {
        super(context);
    }

    public DisTouchExtendCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisTouchExtendCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void toggle() {
        //nothing
    }
    public void toggleDelegate() {
        super.toggle();
    }
}
