package cn.rygel.gd.network;

//import com.google.gson.GsonBuilder;
//import com.orhanobut.logger.Logger;
//
//import java.util.HashMap;
//import java.util.concurrent.TimeUnit;
//
//import rygel.cn.uilibrary.BuildConfig;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitCreator {
//
//    private static final OkHttpClient.Builder CLITENT_BUIDLER = new OkHttpClient.Builder();
//
//    /**
//     * API Service 集合，避免重复创建API Service 造成性能消耗
//     */
//    private static final HashMap<String, Object> serviceMap = new HashMap<>();
//    private static final long DEFAULT_TIME_OUT = 30;
//    private static OkHttpClient httpClient;
//    private static Retrofit retrofitRaw;
//    private static Retrofit retrofitRxJava;
//    private static Retrofit retrofitGson;
//
//    public enum TYPE {
//        CALL,
//        GSON,
//        RXJAVA
//    }
//
//
//    public static <T> T create(String baseUrl, Class<T> service) {
//        return create(baseUrl, service, TYPE.GSON);
//    }
//
//
//    /**
//     * 根据业务需要返回不同类型的 Service 接口实现
//     *
//     * @param baseUrl
//     * @param service
//     * @param type
//     * @param <T>
//     * @return
//     */
//    public static <T> T create(String baseUrl, Class<T> service, TYPE type) {
//        if (serviceMap.containsKey(service.getName())) {
//            return (T) serviceMap.get(service.getName());
//        }
//        T proxy;
//        switch (type) {
//            /*返回Call 对象*/
//            case CALL:
//                proxy = rawRetrofitBuilder(baseUrl).create(service);
//                break;
//            // 返回 数据经过Gson对象转换的 Observable对象
//            case GSON:
//                proxy = gsonRetrofitBuilder(baseUrl).create(service);
//                break;
//            // 返回 数据未经过对象转换的 Observable对象
//            case RXJAVA:
//                proxy = rxRetrofitBuilder(baseUrl).create(service);
//                break;
//            default:
//                throw new RuntimeException(RetrofitCreator.class.getName() + ": the type is not available");
//        }
//        serviceMap.put(service.getName(), proxy);
//        return proxy;
//    }
//
//
//    /**
//     * 创建 全局的OkHttpClient
//     *
//     * @return
//     */
//    public static OkHttpClient getHttpClient() {
//        if (httpClient != null)
//            return httpClient;
//
//        // 当App为调试状态时打印 网络请求信息
//        if (BuildConfig.DEBUG) {
//            // 请求的返回做统一处理
//            CLITENT_BUIDLER.addInterceptor(chain -> {
//
//                Request.Builder builder = chain.request().newBuilder();
//                return chain.proceed(builder.build());
//
//            }).addInterceptor(new HttpLoggingInterceptor(message -> {
//
//                // 打印请求/返回结果
//                if ("".equals(message))
//                    return;
//                if (message.startsWith("{") || message.startsWith("[")) {
//                    Logger.json(message);
//                } else {
//                    Logger.i(message);
//                }
//            }).setLevel(HttpLoggingInterceptor.Level.BODY));
//        }
//
//        httpClient = CLITENT_BUIDLER
//                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
//                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
//                .build();
//
//        return httpClient;
//    }
//
//    private static Retrofit rawRetrofitBuilder(String baseUrl) {
//
//        if (retrofitRaw != null) {
//            return retrofitRaw;
//        }
//
//        retrofitRaw = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(getHttpClient())
//                .build();
//
//        return retrofitRaw;
//    }
//
//
//    private static Retrofit rxRetrofitBuilder(String baseUrl) {
//        if (retrofitRxJava != null) {
//            return retrofitRxJava;
//        }
//
//        retrofitRxJava = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(getHttpClient())
//                .build();
//
//        return retrofitRxJava;
//    }
//
//    private static Retrofit gsonRetrofitBuilder(String baseUrl) {
//        if (retrofitGson != null)
//            return retrofitGson;
//
//        retrofitGson = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                // 非严格的Json->对象的转换
//                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
//                .client(getHttpClient())
//                .build();
//
//        return retrofitGson;
//    }
//
//}
