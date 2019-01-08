package cn.hankchan.utils.client;

import org.apache.http.impl.client.HttpClients;

/**
 * @author hankChan
 *
 */
public class SimpleHttpClient extends DefaultHttpClient {

    /**
     * 最普通的Http客户端,默认自动初始化
     * 需要调用初始化方法{@link #init()}执行实例化,结束使用时调用销毁方法{@link #shutdown()}执行资源回收
     */
    public SimpleHttpClient() {
        if(null == httpClient) {
            httpClient = HttpClients.createDefault();
        }
    }

    @Override
    public void init() {
        if(null == httpClient) {
            httpClient = HttpClients.createDefault();
        }
    }
}
