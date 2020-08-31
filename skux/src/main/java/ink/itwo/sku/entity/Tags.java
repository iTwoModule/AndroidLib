package ink.itwo.sku.entity;

/**
 Created by wangtaian on 2019/4/4. */
public class Tags {
    protected String text;
    protected TagType type;
    protected int sort;
    protected boolean isEnable = true;
    protected boolean isCheck = false;

    public int getSort() {
        return sort;
    }

    public Tags setSort(int sort) {
        this.sort = sort;
        return this;
    }

    public TagType getType() {
        return type;
    }

    public Tags setType(TagType type) {
        this.type = type;
        return this;
    }

    public String getText() {
        return text;
    }

    public Tags setText(String text) {
        this.text = text;
        return this;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public Tags setEnable(boolean enable) {
        isEnable = enable;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public Tags setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public void toggle() {
        isCheck = !isCheck;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Tags)) return false;
        if (text == null || type == null) return false;
        return text.equals(((Tags) obj).getText()) && type.equals(((Tags) obj).getType());
    }

//    @Override
//    public int hashCode() {
//        return (type.getType() + String.valueOf(type.getSort()) + text).hashCode();
//    }


    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
