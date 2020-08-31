package ink.itwo.sku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.widget.Checkable;

import androidx.appcompat.widget.AppCompatTextView;

import ink.itwo.sku.R;

/**
 Created by wangtaian on 2019/4/4. */
public class TagView extends AppCompatTextView implements Checkable {
    private boolean checked = false, clickEnable = true;
    private SparseIntArray tagStyles;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked == checked) {
            return;
        }
        this.checked = checked;
        switchStyle();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        switchStyle();
    }

    public void setClickEnable(boolean clickEnable) {
        this.clickEnable = clickEnable;
        switchStyle();
    }

    public void setState(boolean checked, boolean clickEnable) {
        this.checked = checked;
        this.clickEnable = clickEnable;
        switchStyle();
    }

    public void setTagStyles(SparseIntArray tagStyles) {
        this.tagStyles = tagStyles;
    }

    protected void switchStyle() {
        if (clickEnable) {
            setBackgroundResource(checked ? tagStyles.get(R.id.sku_tagBackgroundResource_check) : tagStyles.get(R.id.sku_tagBackgroundResource_normal));
            setTextColor(checked ? tagStyles.get(R.id.sku_tagTextColor_check) : tagStyles.get(R.id.sku_tagTextColor_normal));
        } else {
            setBackgroundResource(tagStyles.get(R.id.sku_tagBackgroundResource_disenable));
            setTextColor(tagStyles.get(R.id.sku_tagTextColor_disenable));
        }
    }
}
