package ink.itwo.android.debug.stack;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 Created by wang on 4/12/21. */
public class DebugStackHelper {
    private static Context sContext;
    private static Stack<Activity> stack = new Stack<>();
    private static String broadcastAction = "Activity_Stack_Broadcast";

    public static void init(Application application) {
        sContext = application;
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("", "");
                Log.d("", "");
                if (broadcastAction.equals(intent.getAction())) {
                    show();
                }
            }
        };
        application.registerReceiver(receiver, new IntentFilter(broadcastAction));

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                stack.add(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                stack.remove(activity);
            }
        });
        notification();
    }

    private static void notification() {
        String id = "debug_stack_notify_channel_01";
        String name = "stack";
        NotificationManager manager = (NotificationManager) sContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW));
            notification = new Notification.Builder(sContext, id)
                    .setChannelId(id)
                    .setContentTitle("Activity Stack")
                    .setContentText("")
                    .setSmallIcon(R.mipmap.ic_debug_stack)
                    .setContentIntent(PendingIntent.getBroadcast(sContext, 129, new Intent(broadcastAction), PendingIntent.FLAG_ONE_SHOT))
//                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(sContext, id)
                    .setChannelId(id)
                    .setContentTitle("Activity Stack")
                    .setContentText("")
                    .setSmallIcon(R.mipmap.ic_debug_stack)
                    .setContentIntent(PendingIntent.getBroadcast(sContext, 129, new Intent(broadcastAction), PendingIntent.FLAG_ONE_SHOT))
//                    .setAutoCancel(true)
                    .build();

        }
//        notification.flags |= PendingIntent.FLAG_NO_CREATE;
        manager.notify(5, notification);
    }

    private static void show() {
        Activity activity = stack.lastElement();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getDisplay().getRealMetrics(dm);
        float density = activity.getResources().getDisplayMetrics().density;
        int w = dm.widthPixels;
        int h = (int) (400 * density + 0.5f);
        Dialog dialog = new Dialog(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.layout_debug_stack, null);
        View tvClose = view.findViewById(R.id.tv_close);
        TextView tvStack = view.findViewById(R.id.tv_stack);
        tvClose.setOnClickListener(v -> dialog.dismiss());
        addView(tvStack);
        dialog.setContentView(view, new ViewGroup.LayoutParams(w, h));
        dialog.show();
    }

    private static void addView(TextView tvStack) {
        StringBuilder sb = new StringBuilder();
        for (Activity activity : stack) {
            sb.append("\n");
            sb.append("◉  ");
            sb.append(activity.getClass().getName());
            if (activity instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
                List<Fragment> fragments = manager.getFragments();
                for (Fragment fm : fragments) {
                    sb.append("\n");
                    sb.append("     *  ");
                    sb.append(fm.getClass().getName());
                }
            }
        }
        tvStack.setText(sb);
    }
}
