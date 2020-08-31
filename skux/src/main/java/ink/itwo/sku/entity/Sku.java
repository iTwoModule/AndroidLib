package ink.itwo.sku.entity;

import java.util.Set;

/**
 @author wang on 2019-07-09  10:37 */
public class Sku {
    protected Object skuId;
    protected double price;
    protected int count;
    protected Set<Tags> tags;

    public Object getSkuId() {
        return skuId;
    }

    public Sku setSkuId(Object skuId) {
        this.skuId = skuId;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Sku setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Sku setCount(int count) {
        this.count = count;
        return this;
    }

    public Set<Tags> getTags() {
        return tags;
    }

    public Sku setTags(Set<Tags> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sku)) {
            return false;
        }
        Sku sku = (Sku) obj;
        if (sku.getSkuId() != null && sku.getSkuId().equals(skuId)) {
            return true;
        }
        if (sku.getTags() != null && !sku.getTags().isEmpty()) {
            if (sku.getTags().size() != tags.size()) {
                return false;
            }
            return sku.tags.containsAll(tags);
        }
        return false;

    }
}