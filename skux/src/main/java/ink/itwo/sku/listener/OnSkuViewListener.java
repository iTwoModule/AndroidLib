package ink.itwo.sku.listener;

import java.util.Map;

import ink.itwo.sku.entity.Sku;
import ink.itwo.sku.entity.TagType;
import ink.itwo.sku.entity.Tags;

/**
 @author wang on 2019-07-09  10:34 */
@FunctionalInterface
public interface OnSkuViewListener {
    /**
     @param tags      当前点击的 tag
     @param checkTags 已选中的 tag
     @param sku       选择出的 sku ,为 null 时表示还是 tag 未选择
     */
    void onTagsClick(Tags tags, Map<TagType, Tags> checkTags, Sku sku);
}
