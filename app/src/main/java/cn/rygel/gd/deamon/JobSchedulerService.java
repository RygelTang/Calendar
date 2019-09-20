package cn.rygel.gd.deamon;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.orhanobut.logger.Logger;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    private static final int JOB_ID = 10000;

    public static void scheduleJobService(Context context) {
        boolean isSuccess = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(context, JobSchedulerService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMinimumLatency(DaemonUtil.getIntervalTime());
                builder.setOverrideDeadline(DaemonUtil.getIntervalTime() * 2);
                builder.setBackoffCriteria(DaemonUtil.getIntervalTime(), JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
            } else {
                builder.setPeriodic(DaemonUtil.getIntervalTime());
            }
            builder.setPersisted(true);
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.cancelAll();
                isSuccess = jobScheduler.schedule(builder.build()) == JobScheduler.RESULT_SUCCESS;
            }
        }
        if (isSuccess) {
            Logger.d("Scheduler Success!");
        } else {
            Logger.e("Scheduler Failed!");
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.d("onStartJob()");
        DaemonHolder.startService();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Logger.d("onStopJob()");
        DaemonHolder.startService();
        return false;
    }
}
