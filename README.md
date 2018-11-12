# MultiThreadDownload
多线程 断点续传 并支持 http和https好用的下载框架  
## 先来个效果图  
[效果图]()
## 使用前的准备
1、 Add the JitPack repository to your build file
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```  
2、 Add the dependency  
```
dependencies {
	        implementation 'com.github.luchongbin:MultiThreadBrokenPoinDownload:V1.0.0'
	}
```
3、在Manifest中添加需要的权限  
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```  
4、为了高版本兼容，需要在你需要的Activity或者Fragment中动态申请权限  
```
if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
```  
5、在你的Application中添加线程数的申请
```
 DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
```
## 如何使用  
1、在你使用的地方，使用下面代码进行下载
```
 DownloadRequest request = new DownloadRequest.Builder()
                .setName(name)//保存的文件名
                .setUri(url)//下载文件的网络路径
                .setFolder(File file)//比如：new File(Environment.getExternalStorageDirectory(), "Download")
                .build();
        DownloadManager.getInstance().download(request, tag, new DownloadCallBack());//可以使用URL作为TAG
```
2、DownloadCallBack类为：
```
public  class DownloadCallBack implements CallBack {
        public DownloadCallBack(l) {
            
        }

        @Override
        public void onStarted() {//开始

        }

        @Override
        public void onConnecting() {//连接中
            
        }

        @Override
        public void onConnected(long total, boolean isRangeSupport) {//连接完成
    
        }

        @Override
        public void onProgress(long finished, long total, int progress) {//下载精度
           
        }

        @Override
        public void onCompleted() {//下载完成
          
        }

        @Override
        public void onDownloadPaused() {//暂停下载
         

        }

        @Override
        public void onDownloadCanceled() {//取消下载
          

        }

        @Override
        public void onFailed(DownloadException e) {//下载完成
           
        }
```

## License  
```
MIT License

Copyright (c) 2018 卢崇斌

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
