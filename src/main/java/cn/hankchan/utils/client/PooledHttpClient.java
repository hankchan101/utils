package cn.hankchan.utils.client;

import cn.hankchan.utils.client.ssl.UnverifyX509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.util.Random;

/**
 * 连接池形式的Http客户端
 * @author hankChan
 *
 */
public class PooledHttpClient extends DefaultHttpClient {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String TLS = "TLS";

    private boolean enableSSL;
    private int maxTotal;
    private int maxPerRoute;
    private int defaultMaxPerRoute;
    private int port;
    private String host;
    private PoolingHttpClientConnectionManager clientConnectionManager;

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     * 默认maxTotal为2048,maxPerRoute为100,defaultMaxPerRoute为50,port为[40000,50000)之间的随机数,host为localhost
     */
    public PooledHttpClient() {
        this(false, 2048, 100, 50);
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     * 默认maxTotal为2048,maxPerRoute为100,defaultMaxPerRoute为50,port为[40000,50000)之间的随机数,host为localhost
     * @param enableSSL 是否允许SSL请求,默认为false
     */
    public PooledHttpClient(boolean enableSSL) {
        this(enableSSL, 2048, 100, 50);
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收.
     * port为[40000,50000)之间的随机数,host为localhost
     * @param maxTotal maxTotal
     * @param maxPerRoute maxPerRoute
     * @param defaultMaxPerRoute defaultMaxPerRoute
     */
    public PooledHttpClient(int maxTotal, int maxPerRoute, int defaultMaxPerRoute) {
        // port为[40000,50000)之间的随机数
        this(false, maxTotal, maxPerRoute, defaultMaxPerRoute, new Random().nextInt(10000)+40000);
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收.
     * port为[40000,50000)之间的随机数,host为localhost
     * @param enableSSL 是否允许SSL请求,默认为false
     * @param maxTotal maxTotal
     * @param maxPerRoute maxPerRoute
     * @param defaultMaxPerRoute defaultMaxPerRoute
     */
    public PooledHttpClient(boolean enableSSL, int maxTotal, int maxPerRoute, int defaultMaxPerRoute) {
        // port为[40000,50000)之间的随机数
        this(enableSSL, maxTotal, maxPerRoute, defaultMaxPerRoute, new Random().nextInt(10000)+40000);
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     * 默认host为localhost
     * @param maxTotal maxTotal
     * @param maxPerRoute maxPerRoute
     * @param defaultMaxPerRoute defaultMaxPerRoute
     * @param port port
     */
    public PooledHttpClient(int maxTotal, int maxPerRoute, int defaultMaxPerRoute, int port) {
        this(false, maxTotal, maxPerRoute, defaultMaxPerRoute, port, "localhost");
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     * 默认host为localhost
     * @param enableSSL 是否允许SSL请求,默认为false
     * @param maxTotal maxTotal
     * @param maxPerRoute maxPerRoute
     * @param defaultMaxPerRoute defaultMaxPerRoute
     * @param port port
     */
    public PooledHttpClient(boolean enableSSL, int maxTotal, int maxPerRoute, int defaultMaxPerRoute, int port) {
        this(enableSSL, maxTotal, maxPerRoute, defaultMaxPerRoute, port, "localhost");
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     * @param maxTotal maxTotal
     * @param maxPerRoute maxPerRoute
     * @param defaultMaxPerRoute defaultMaxPerRoute
     * @param port port
     * @param host host
     */
    public PooledHttpClient(int maxTotal, int maxPerRoute, int defaultMaxPerRoute, int port, String host) {
        this(false, maxTotal, maxPerRoute, defaultMaxPerRoute, defaultMaxPerRoute, host);
    }

    /**
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     * @param enableSSL 是否允许SSL请求,默认为false
     * @param maxTotal maxTotal
     * @param maxPerRoute maxPerRoute
     * @param defaultMaxPerRoute defaultMaxPerRoute
     * @param port port
     * @param host host
     */
    public PooledHttpClient(boolean enableSSL, int maxTotal, int maxPerRoute, int defaultMaxPerRoute, int port, String host) {
        this.enableSSL = enableSSL;
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        this.port = port;
        this.host = host;
    }

    @Override
    public void init() {
        if(enableSSL) {
            SSLContext context = null;
            try {
                context = SSLContext.getInstance(TLS);
                TrustManager trustManager = new UnverifyX509TrustManager();
                context.init(null, new TrustManager[]{trustManager}, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> connectionSocketFactory = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, PlainConnectionSocketFactory.INSTANCE)
                    .register(HTTPS, sslConnectionSocketFactory).build();
            clientConnectionManager = new PoolingHttpClientConnectionManager(connectionSocketFactory);
            clientConnectionManager.setMaxTotal(maxTotal);
            clientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            HttpHost httpHost = new HttpHost(host, port);
            clientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), maxPerRoute);
            httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
        } else {
            clientConnectionManager = new PoolingHttpClientConnectionManager();
            clientConnectionManager.setMaxTotal(maxTotal);
            clientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            HttpHost httpHost = new HttpHost(host, port);
            clientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), maxPerRoute);
            httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
