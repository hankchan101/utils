package cn.hankchan.utils;

import cn.hankchan.utils.consts.SystemEnvNameConst;
import cn.hankchan.utils.unique.SnowflakeIdWorker;

import java.util.Optional;

/**
 * <p>分布式唯一id生成工具类</p>
 * 对于分布式系统,需要通过指定数据中心id及机器id,避免在分布式系统中产生id碰撞.
 * 通过定义系统环境变量名{@link SystemEnvNameConst#DATA_CENTER_ID_OF_UNIQUE_ID}的值来定义数据中心id,可选值为0-31,默认值为0
 * 通过定义系统环境变量名{@link SystemEnvNameConst#WORKER_ID_OF_UNIQUE_ID}的值来定义机器id,可选值为0-31,默认值为0
 * 也就是说最高支持在32x32个vm的分布式集群系统中生成唯一id,每个vm每秒能够产生26万ID左右.
 * @author hankChan
 *
 */
public class UniqueUtils {

    private static SnowflakeIdWorker snowflakeIdWorker;
    private static int workerId;
    private static int dataCenterId;

    static {
        workerId = Integer.valueOf(Optional.ofNullable(System.getenv(SystemEnvNameConst.WORKER_ID_OF_UNIQUE_ID)).orElse("0"));
        dataCenterId = Integer.valueOf(Optional.ofNullable(System.getenv(SystemEnvNameConst.DATA_CENTER_ID_OF_UNIQUE_ID)).orElse("0"));
        snowflakeIdWorker = new SnowflakeIdWorker(workerId, dataCenterId);
    }

    /**
     * 禁止new实例化
     */
    private UniqueUtils() {}

    /**
     * 获取分布式全局唯一id,在分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分),每秒能够产生26万ID左右.
     * 默认数据中心Id为0,可选值为0-31(5位比特),由环境变量{@link SystemEnvNameConst#DATA_CENTER_ID_OF_UNIQUE_ID}指定;
     * 机器Id为0,可选值为0-31(5位比特),由环境变量{@link SystemEnvNameConst#WORKER_ID_OF_UNIQUE_ID}指定
     * @return 全局唯一id
     */
    public static long getUUId() {
        return snowflakeIdWorker.nextId();
    }

    /**
     * 获取在当前分布式系统中的机器id,可选值为0-31,默认为0
     * @return 机器id
     */
    public static int getWorkerId() {
        return workerId;
    }

    /**
     * 获取在当前分布式系统中的数据中心id,可选值为0-31,默认为0
     * @return 数据中心id
     */
    public static int getDataCenterId() {
        return dataCenterId;
    }
}
