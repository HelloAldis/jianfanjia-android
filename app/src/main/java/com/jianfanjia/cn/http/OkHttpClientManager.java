package com.jianfanjia.cn.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.coreprogress.helper.ProgressHelper;
import com.jianfanjia.cn.http.coreprogress.listener.impl.UIProgressListener;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Description: com.jianfanjia.cn.http
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 15:17
 */
public class OkHttpClientManager {
    private static final String TAG = OkHttpClientManager.class.getName();
    public static final String SERVER_ERROR = "网络异常";
    public static final String NOT_NET_ERROR = "网络未连接";

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private HttpsDelegate mHttpsDelegate = new HttpsDelegate();
    private DownloadDelegate mDownloadDelegate = new DownloadDelegate();
    private DisplayImageDelegate mDisplayImageDelegate = new DisplayImageDelegate();
    private GetDelegate mGetDelegate = new GetDelegate();
    private UploadDelegate mUploadDelegate = new UploadDelegate();
    private PostDelegate mPostDelegate = new PostDelegate();

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        //cookie enabled
        mDelivery = new Handler(Looper.getMainLooper());

        /*just for test !!!*/
        /*mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });*/

    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public GetDelegate getGetDelegate() {
        return mGetDelegate;
    }

    public PostDelegate getPostDelegate() {
        return mPostDelegate;
    }

    private HttpsDelegate _getHttpsDelegate() {
        return mHttpsDelegate;
    }

    public DownloadDelegate _getDownloadDelegate() {
        return mDownloadDelegate;
    }

    private DisplayImageDelegate _getDisplayImageDelegate() {
        return mDisplayImageDelegate;
    }

    private UploadDelegate _getUploadDelegate() {
        return mUploadDelegate;
    }

    public static DownloadDelegate getDownloadDelegate() {
        return getInstance()._getDownloadDelegate();
    }

    public static DisplayImageDelegate getDisplayImageDelegate() {
        return getInstance()._getDisplayImageDelegate();
    }

    public static UploadDelegate getUploadDelegate() {
        return getInstance()._getUploadDelegate();
    }

    public static HttpsDelegate getHttpsDelegate() {
        return getInstance()._getHttpsDelegate();
    }

    private void deliveryResult(final ApiUiUpdateListener listener, final BaseRequest baseRequest) {
        //UI thread
        if (!NetTool.isNetworkAvailable(MyApplication.getInstance())) {
            sendFailedStringCallback(listener, NOT_NET_ERROR);
            return;
        }
        if (listener != null) {
            listener.preLoad();
        }
        LogTool.d(TAG, "preLoad :");
        baseRequest.pre();
        mOkHttpClient.newCall(baseRequest.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(listener, SERVER_ERROR);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    JSONObject responseString = new JSONObject(string);
                    if (responseString.has(Constant.DATA)
                            && responseString.get(Constant.DATA) != null) {
                        LogTool.d(TAG, "data :" + responseString.get(Constant.DATA).toString());
                        String data = responseString.get(Constant.DATA).toString();
                        baseRequest.onSuccess(data);
                        sendSuccessResultCallback(listener, data);
                    } else if (responseString.has(Constant.ERROR_MSG) && responseString.get(Constant.ERROR_MSG) != null) {
                        LogTool.d(TAG, "errormsg :" + responseString.get(
                                Constant.ERROR_MSG).toString());
                        String error_msg = responseString.get(
                                Constant.ERROR_MSG).toString();
                        baseRequest.onFailure(error_msg);
                        sendFailedStringCallback(listener, error_msg);
                    } else if (responseString.has(Constant.SUCCESS_MSG) && responseString.get(
                            Constant.SUCCESS_MSG) != null) {
                        LogTool.d(TAG, "msg :" + responseString.get(
                                Constant.SUCCESS_MSG).toString());
                        String msg = responseString.get(
                                Constant.SUCCESS_MSG).toString();
                        baseRequest.onSuccess(msg);
                        sendSuccessResultCallback(listener, msg);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(listener, SERVER_ERROR);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(listener, SERVER_ERROR);
                } catch (JSONException e) {
                    sendFailedStringCallback(listener, SERVER_ERROR);

                }
            }
        });
    }

    private void sendFailedStringCallback(final ApiUiUpdateListener listener, final String error_msg) {
        LogTool.d("onResponse ==", "loadFailture :" + error_msg);
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.loadFailture(error_msg);
                }
            }
        });
    }

    private void sendSuccessResultCallback(final ApiUiUpdateListener listener, final Object data) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.loadSuccess(data);
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    public static void cancelTag(Object tag) {
        getInstance()._cancelTag(tag);
    }

    private void _cancelTag(Object tag) {
        mOkHttpClient.cancel(tag);
    }

    public static OkHttpClient getClinet() {
        return getInstance().client();
    }

    public OkHttpClient client() {
        return mOkHttpClient;
    }

    //====================PostDelegate=======================
    public class PostDelegate {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
        private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
        private final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");

        /**
         * 直接将bodyStr以写入请求体
         */
        public void postAsyn(BaseRequest baseRequest, String bodyStr, final ApiUiUpdateListener apiUiUpdateListener) {
            postAsyn(baseRequest, bodyStr, apiUiUpdateListener, null);
        }

        public void postAsyn(BaseRequest baseRequest, String bodyStr, final ApiUiUpdateListener apiUiUpdateListener, Object tag) {
            postAsynWithMediaType(baseRequest, bodyStr, MEDIA_TYPE_JSON, apiUiUpdateListener, tag);
        }

        /**
         * 直接将bodyBytes以写入请求体
         */
        public void postAsyn(BaseRequest baseRequest, byte[] bodyBytes, final ApiUiUpdateListener apiUiUpdateListener) {
            postAsyn(baseRequest, bodyBytes, apiUiUpdateListener, null);
        }

        public void postAsyn(BaseRequest baseRequest, byte[] bodyBytes, final ApiUiUpdateListener apiUiUpdateListener, Object tag) {
            postAsynWithMediaType(baseRequest, bodyBytes, MEDIA_TYPE_IMAGE, apiUiUpdateListener, tag);
        }

        /**
         * 直接将bodyStr以写入请求体
         */
        public void postAsynWithMediaType(BaseRequest baseRequest, String bodyStr, MediaType type, final ApiUiUpdateListener listener, Object tag) {
            RequestBody body = RequestBody.create(type, bodyStr);
            baseRequest.setRequest(buildPostRequest(baseRequest.getUrl(), body, tag));
            deliveryResult(listener, baseRequest);
        }

        /**
         * 直接将bodyBytes以写入请求体
         */
        public void postAsynWithMediaType(BaseRequest baseRequest, byte[] bodyBytes, MediaType type, ApiUiUpdateListener listener, Object tag) {
            RequestBody body = RequestBody.create(type, bodyBytes);
            baseRequest.setRequest(buildPostRequest(baseRequest.getUrl(), body, tag));
            deliveryResult(listener, baseRequest);
        }

        /**
         * post构造Request的方法
         *
         * @param url
         * @param body
         * @return
         */
        private Request buildPostRequest(String url, RequestBody body, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(body);
            if (tag != null) {
                builder.tag(tag);
            }
            Request request = builder.build();
            return request;
        }


    }

    //====================GetDelegate=======================
    public class GetDelegate {

        private Request buildGetRequest(String url, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .url(url);

            if (tag != null) {
                builder.tag(tag);
            }

            return builder.build();
        }

        public Response get(String url, Object tag) throws IOException {
            final Request request = buildGetRequest(url, tag);
            return get(request);
        }

        /**
         * 通用的方法
         */
        public Response get(Request request) throws IOException {
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        /**
         * 通用的方法
         */
        private void getAsyn(BaseRequest baseRequest, final ApiUiUpdateListener listener) {
            deliveryResult(listener, baseRequest);
        }


        public void getAsyn(BaseRequest baseRequest, final ApiUiUpdateListener listener, Object tag) {
            baseRequest.setRequest(buildGetRequest(baseRequest.getUrl(), tag));
            getAsyn(baseRequest, listener);
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    //====================UploadDelegate=======================

    /**
     * 上传相关的模块
     */
    public class UploadDelegate {
        /**
         * 异步基于post的文件上传:主方法
         */
        public void postAsyn(BaseRequest baseRequest, String[] fileKeys, File[] files, Param[] params, ApiUiUpdateListener listener, Object tag) {
            baseRequest.setRequest(buildMultipartFormRequest(baseRequest.getUrl(), files, fileKeys, params, tag));
            deliveryResult(listener, baseRequest);
        }

        /**
         * 异步基于post的文件上传:单文件不带参数上传
         */
        public void postAsyn(BaseRequest baseRequest, String fileKey, File file, ApiUiUpdateListener listener, Object tag) throws IOException {
            postAsyn(baseRequest, new String[]{fileKey}, new File[]{file}, null, listener, tag);
        }

        /**
         * 异步基于post的文件上传，单文件且携带其他form参数上传
         */
        public void postAsyn(BaseRequest baseRequest, String fileKey, File file, Param[] params, ApiUiUpdateListener listener, Object tag) {
            postAsyn(baseRequest, new String[]{fileKey}, new File[]{file}, params, listener, tag);
        }

        private Request buildMultipartFormRequest(String url, File[] files,
                                                  String[] fileKeys, Param[] params, Object tag) {
            params = validateParam(params);

            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);

            for (Param param : params) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                        RequestBody.create(null, param.value));
            }
            if (files != null) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    String fileName = file.getName();
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                    //TODO 根据文件名设置contentType
                    builder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                            fileBody);
                }
            }

            RequestBody requestBody = builder.build();
            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();
        }

    }

    //====================DisplayImageDelegate=======================

    /**
     * 加载图片相关
     */
    public class DisplayImageDelegate {
        /**
         * 加载图片
         */
        public void displayImage(final ImageView view, final String url, final int errorResId, final Object tag) {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    setErrorResId(view, errorResId);
                }

                @Override
                public void onResponse(Response response) {
                    InputStream is = null;
                    try {
                        is = response.body().byteStream();
                        ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                        ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                        int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                        try {
                            is.reset();
                        } catch (IOException e) {
                            response = mGetDelegate.get(url, tag);
                            is = response.body().byteStream();
                        }

                        BitmapFactory.Options ops = new BitmapFactory.Options();
                        ops.inJustDecodeBounds = false;
                        ops.inSampleSize = inSampleSize;
                        final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setImageBitmap(bm);
                            }
                        });
                    } catch (Exception e) {
                        setErrorResId(view, errorResId);

                    } finally {
                        if (is != null) try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }

        public void displayImage(final ImageView view, String url) {
            displayImage(view, url, -1, null);
        }

        public void displayImage(final ImageView view, String url, Object tag) {
            displayImage(view, url, -1, tag);
        }

        private void setErrorResId(final ImageView view, final int errorResId) {
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    view.setImageResource(errorResId);
                }
            });
        }
    }


    //====================DownloadDelegate=======================

    /**
     * 下载相关的模块
     */
    public class DownloadDelegate {
        /**
         * 异步下载文件
         *
         * @param url
         * @param destFileDir        本地文件存储的文件夹
         * @param listener
         * @param uiProgressListener 用来显示下载进度信息的监听器
         */
        public void downloadAsyn(final String url, final String filename, final String destFileDir, final ApiUiUpdateListener listener, UIProgressListener uiProgressListener, Object tag) {
            final Request request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .build();
            final Call call = ProgressHelper.addProgressResponseListener(mOkHttpClient, uiProgressListener).newCall(request);
            if (listener != null) {
                listener.preLoad();
            }
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    sendFailedStringCallback(listener, SERVER_ERROR);
                }

                @Override
                public void onResponse(Response response) {
                    InputStream is = null;
                    byte[] buf = new byte[1024];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();

                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(dir, filename);
                        fos = new FileOutputStream(file, false);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        //如果下载文件成功，传递的数据为文件的绝对路径
                        LogTool.d(TAG, file.getAbsolutePath());
                        sendSuccessResultCallback(listener, file.getAbsolutePath());
                    } catch (IOException e) {
                        sendFailedStringCallback(listener, SERVER_ERROR);
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }

                }
            });
        }


        public void downloadAsyn(final String url, final String filename, final String destFileDir, final ApiUiUpdateListener listener, UIProgressListener uiProgressListener) {
            downloadAsyn(url, filename, destFileDir, listener, uiProgressListener, null);
        }
    }
    //====================HttpsDelegate=======================

    /**
     * Https相关模块
     */
    public class HttpsDelegate {

        public void setCertificates(InputStream... certificates) {
            setCertificates(certificates, null, null);
        }

        public TrustManager[] prepareTrustManager(InputStream... certificates) {
            if (certificates == null || certificates.length <= 0) return null;
            try {

                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates) {
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    try {
                        if (certificate != null)
                            certificate.close();
                    } catch (IOException e)

                    {
                    }
                }
                TrustManagerFactory trustManagerFactory = null;

                trustManagerFactory = TrustManagerFactory.
                        getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

                return trustManagers;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
            try {
                if (bksFile == null || password == null) return null;

                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());
                return keyManagerFactory.getKeyManagers();

            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
            try {
                TrustManager[] trustManagers = prepareTrustManager(certificates);
                KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
                SSLContext sslContext = SSLContext.getInstance("TLS");

                sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
                mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }


        public class MyTrustManager implements X509TrustManager {
            private X509TrustManager defaultTrustManager;
            private X509TrustManager localTrustManager;

            public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
                TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var4.init((KeyStore) null);
                defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
                this.localTrustManager = localTrustManager;
            }


            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException ce) {
                    localTrustManager.checkServerTrusted(chain, authType);
                }
            }


            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }

    }

    //====================ImageUtils=======================
    public static class ImageUtils {
        /**
         * 根据InputStream获取图片实际的宽度和高度
         *
         * @param imageStream
         * @return
         */
        public static ImageSize getImageSize(InputStream imageStream) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);
            return new ImageSize(options.outWidth, options.outHeight);
        }

        public static class ImageSize {
            int width;
            int height;

            public ImageSize() {
            }

            public ImageSize(int width, int height) {
                this.width = width;
                this.height = height;
            }

            @Override
            public String toString() {
                return "ImageSize{" +
                        "width=" + width +
                        ", height=" + height +
                        '}';
            }
        }

        public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
            // 源图片的宽度
            int width = srcSize.width;
            int height = srcSize.height;
            int inSampleSize = 1;

            int reqWidth = targetSize.width;
            int reqHeight = targetSize.height;

            if (width > reqWidth && height > reqHeight) {
                // 计算出实际宽度和目标宽度的比率
                int widthRatio = Math.round((float) width / (float) reqWidth);
                int heightRatio = Math.round((float) height / (float) reqHeight);
                inSampleSize = Math.max(widthRatio, heightRatio);
            }
            return inSampleSize;
        }

        /**
         * 根据ImageView获适当的压缩的宽和高
         *
         * @param view
         * @return
         */
        public static ImageSize getImageViewSize(View view) {

            ImageSize imageSize = new ImageSize();

            imageSize.width = getExpectWidth(view);
            imageSize.height = getExpectHeight(view);

            return imageSize;
        }

        /**
         * 根据view获得期望的高度
         *
         * @param view
         * @return
         */
        private static int getExpectHeight(View view) {

            int height = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = view.getWidth(); // 获得实际的宽度
            }
            if (height <= 0 && params != null) {
                height = params.height; // 获得布局文件中的声明的宽度
            }

            if (height <= 0) {
                height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
            }

            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (height <= 0) {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                height = displayMetrics.heightPixels;
            }

            return height;
        }

        /**
         * 根据view获得期望的宽度
         *
         * @param view
         * @return
         */
        private static int getExpectWidth(View view) {
            int width = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = view.getWidth(); // 获得实际的宽度
            }
            if (width <= 0 && params != null) {
                width = params.width; // 获得布局文件中的声明的宽度
            }

            if (width <= 0)

            {
                width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
            }
            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (width <= 0)

            {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                width = displayMetrics.widthPixels;
            }

            return width;
        }

        /**
         * 通过反射获取imageview的某个属性值
         *
         * @param object
         * @param fieldName
         * @return
         */
        private static int getImageViewFieldValue(Object object, String fieldName) {
            int value = 0;
            try {
                Field field = ImageView.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                int fieldValue = field.getInt(object);
                if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                    value = fieldValue;
                }
            } catch (Exception e) {
            }
            return value;

        }
    }


}

