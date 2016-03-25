package name.haoxin.brushedmetal;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by hx on 16/3/25.
 */
public class FpsListener {
    //设定动画的FPS桢数，此数值越高,动画速度越快。
//    private final int FPS = 100;
    // 换算为运行周期
//    private final long PERIOD = (long) (1.0 / FPS * 1000000000); // 单位: ns(纳秒)
    // FPS最大间隔时间，换算为1s = 10^9ns
//    private final long FPS_MAX_INTERVAL = 1000000000L; // 单位: ns

    private final long FPS_INTERVAL_MILL = 1000L;

    // 实际的FPS数值
    private long nowFPS = 0L;

    // FPS累计用间距时间
//    private long interval = 0L; // in ns
    private long time;
    //运行桢累计
    private long frameCount = 0;

    //格式化小数位数
//    private DecimalFormat df = new DecimalFormat("0.0");

    public void reset() {
        frameCount = 0L;
//        interval = 0L;
        nowFPS = 0L;
        time = System.currentTimeMillis();
    }

    /** */
    /**
     * 制造FPS数据
     */
    public void makeFPS() {
        frameCount++;
//        interval += PERIOD;
        //当实际间隔符合时间时。
//        if (interval >= FPS_MAX_INTERVAL) {
//            //nanoTime()返回最准确的可用系统计时器的当前值，以毫微秒为单位
//            long timeNow = System.nanoTime();
//            // 获得到目前为止的时间距离
//            long realTime = timeNow - time; // 单位: ns
//            //换算为实际的fps数值
//            nowFPS = ((double) frameCount / realTime) * FPS_MAX_INTERVAL;
//            //变更数值
//            frameCount = 0L;
//            interval = 0L;
//            time = timeNow;
//        }
        long timeNow = System.currentTimeMillis();
        long elapsedTime = timeNow - time;
        if (elapsedTime >= FPS_INTERVAL_MILL) {
            nowFPS = frameCount * FPS_INTERVAL_MILL / elapsedTime;
            time = timeNow;
            frameCount = 0;
        }
    }

    public long getNowFPS() {
        return nowFPS;
    }

    public String getFPS() {
//        return df.format(nowFPS);
        return String.valueOf(nowFPS);
    }
}
