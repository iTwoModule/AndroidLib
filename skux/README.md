## 一、版本
![](https://img.shields.io/github/manifest-json/v/wangtaian/sku.svg)

```
implementation 'ink.itwo:sku:1.0.0'
```
## 二、项目引用的依赖包  
```
compileOnly 'com.android.support:recyclerview-v7:27.1.1'
compileOnly 'com.android.support:appcompat-v7:27.1.1'
implementation  'com.google.android:flexbox:1.0.0'
```
## 三、使用方式  
#### 1、布局  
```xml
<ink.itwo.sku.view.SkuView
    android:id="@+id/sku_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="15dp"
    android:minHeight="300dp"
    />
```
#### 2、数据格式  
一般后台数据格式如下：  

```json
[
"品牌:技嘉  型号:ATX  产地:北京  内存:2G price:100  count:20  ", 
"品牌:技嘉  型号:Mirco-ATX  产地:北京  内存:4G  price:100  count:20  ",
 "品牌:华硕  型号:ATX  产地:上海  内存:2G  price:100  count:20  "
 ]
```
转化成  List<Sku\> ，List 中数据如下格式：  

```json
[{
	"count": 69,
	"price": 427.11,
	"skuId": 0,
	"tags": [{
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "ATX",
		"type": {
			"sort": 1,
			"type": "型号"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "技嘉",
		"type": {
			"sort": 0,
			"type": "品牌"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "北京",
		"type": {
			"sort": 2,
			"type": "产地"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "2G",
		"type": {
			"sort": 3,
			"type": "内存"
		}
	}]
}, {
	"count": 64,
	"price": 561.09,
	"skuId": 1,
	"tags": [{
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "技嘉",
		"type": {
			"sort": 0,
			"type": "品牌"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "北京",
		"type": {
			"sort": 2,
			"type": "产地"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "Mirco-ATX",
		"type": {
			"sort": 1,
			"type": "型号"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "2G",
		"type": {
			"sort": 3,
			"type": "内存"
		}
	}]
}, {
	"count": 42,
	"price": 742.84,
	"skuId": 2,
	"tags": [{
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "技嘉",
		"type": {
			"sort": 0,
			"type": "品牌"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "北京",
		"type": {
			"sort": 2,
			"type": "产地"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "2G",
		"type": {
			"sort": 3,
			"type": "内存"
		}
	}, {
		"isCheck": false,
		"isEnable": true,
		"sort": 0,
		"text": "MINI-ITX",
		"type": {
			"sort": 1,
			"type": "型号"
		}
	}]
}]
```
#### 3、代码中调用
调用 SkuView 中的 setSkus方法，在 OnSkuViewListener 监听中获取tag的点击

```java
public interface OnSkuViewListener {
    /**
     @param tags      当前点击的 tag
     @param checkTags 已选中的 tag
     @param sku       选择出的 sku ,为 null 时表示还是 tag 未选择
     */
    void onTagsClick(Tags tags, Map<TagType, Tags> checkTags, Sku sku);
}
```
## 四、混淆
```
-keep class ink.itwo.sku.** { *; }
```