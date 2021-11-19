package mike.cn.ksbus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.RelativeLayout;

import com.just.library.AgentWeb;

import mike.cn.ksbus.config.Config;

public class MainActivity extends AppCompatActivity {

    private AgentWeb agentWeb;
    //private ValueCallback<Uri> mValueCallback;
    //private ValueCallback<Uri[]> mFilePathCallback;
    //private final static int RESULT_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        agentWeb = AgentWeb.with(this)
                           .setAgentWebParent(layout, new RelativeLayout.LayoutParams(-1, -1))
                           .useDefaultIndicator()
                           .defaultProgressBarColor()
                           //.setWebChromeClient(mWebChromeClient)
                           .setWebViewClient(mWebViewClient)
                           .createAgentWeb()
                           .go("http://mobile.nordlicht-tech.com");
    }

    //WebViewClient
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            agentWeb.getJsEntraceAccess().callJs(loadCss(Config.CSS_URL));
        }


        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
            agentWeb.getJsEntraceAccess().callJs(loadCss(Config.CSS_URL));
        }
    };

/*
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
        // 3.0以下版本
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            mValueCallback = valueCallback;
            openImageActivity();
        }
        //3.0以上版本
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            mValueCallback = valueCallback;
            openImageActivity();
        }
        //4.1以上版本
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType,  String capture) {
            mValueCallback = valueCallback;
            openImageActivity();
        }
        // 5.0以上版本  主要版本
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            mFilePathCallback = filePathCallback;
            openImageActivity();
            return true;
        }

    };
    private void openImageActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        //调起手机图库
        startActivityForResult(Intent.createChooser(i, "Image Chooser"),     RESULT_CODE);
    }

    //处理手机返回的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            if (null == mValueCallback && null == mFilePathCallback) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mFilePathCallback != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mValueCallback != null) {
                mValueCallback.onReceiveValue(result);
                mValueCallback = null;
            }
        }
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != RESULT_CODE || mFilePathCallback == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }
*/

    public static String loadCss(String url) {
        return "javascript:var d=document;" +
               "var s=d.createElement('link');" +
               "s.setAttribute('rel', 'stylesheet');" +
               "s.setAttribute('href', '" + url + "');" +
               "d.head.appendChild(s);";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (agentWeb.getWebCreator().get().canGoBack()) {
                agentWeb.getWebCreator().get().goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
