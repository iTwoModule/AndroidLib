package ink.itwo.sku.entity;

/**
 Created by wangtaian on 2019/4/4. */
public class TagType {
    protected String type;
    protected int sort;

    public String getType() {
        return type;
    }

    public TagType setType(String type) {
        this.type = type;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public TagType setSort(int sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TagType && ((TagType) obj).getType() != null && ((TagType) obj).getType().equals(type);
    }

//    @Override
//    public int hashCode() {
//        return (type + String.valueOf(sort)).hashCode();
//    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
