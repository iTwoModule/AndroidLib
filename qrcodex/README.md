### 一、框架中依赖的第三方 jar
```
api com.google.zxing:core:3.3.3
```
### 二、使用方式
####  1 在布局文件中引入
``` xml
<com.mylhyl.zxing.scanner.ScannerView
    android:layout_width="match_parent"
    android:id="@+id/scan_view"
    android:layout_height="match_parent" />
```
#### 2 在代码监听扫码结果
```
 scannerView?.setOnScannerCompletionListener { rawResult, parsedResult, barcode ->
            val nonceStr = rawResult.toString()
        }
```
#### 3 其他常用方法
```
//闪光灯
scannerView?.toggleLight(hasLight)

//重新扫码
scannerView?.restartPreviewAfterDelay(2000)
```