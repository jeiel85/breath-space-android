package com.example.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        try {
            val channelId = "breath_space_reminders"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Breath Space Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Relaxing reminders for breathing sessions"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val titlesArray = context.resources.getStringArray(com.example.R.array.notification_titles)
            val messagesArray = context.resources.getStringArray(com.example.R.array.notification_messages)
            
            val title = if (titlesArray.isNotEmpty()) titlesArray.random() else "잠깐 멈춰도 괜찮아요."
            val message = if (messagesArray.isNotEmpty()) messagesArray.random() else "잠깐 멈추고 숨을 쉬어볼까요?"

            val clickIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("DEEP_LINK_TO_MOOD_CHECK", true)
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(),
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(101, notification)
            
            // Auto re-schedule for the next day
            intent?.let {
                val freq = it.getIntExtra("FREQ", 1)
                val hour = it.getIntExtra("HOUR", 20)
                val min = it.getIntExtra("MIN", 0)
                ReminderScheduler.scheduleAlarms(context, true, hour, min, freq)
            }
        } catch (e: Exception) {
            Log.e("ReminderReceiver", "Error delivering notification", e)
        }
    }
}

object ReminderScheduler {
    fun cancelAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderReceiver::class.java)
        
        // Cancel base alarm and any multi-frequency ones
        for (i in 0..2) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                1000 + i,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
        Log.d("ReminderScheduler", "Alarms cancelled")
    }

    fun scheduleAlarms(context: Context, enabled: Boolean, hour: Int, minute: Int, frequency: Int) {
        // Always cancel previous alarms first
        cancelAlarms(context)

        if (!enabled) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val baseCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        // Loop for frequency: 1, 2, or 3 times a day
        // Frequency = 1: Schedule at base hour
        // Frequency = 2: Base hour, and Base hour + 12 hours
        // Frequency = 3: Base hour, Base hour + 8 hours, and Base hour + 16 hours
        val hourInterval = when (frequency) {
            2 -> 12
            3 -> 8
            else -> 24
        }
        val maxAlarms = if (frequency in 1..3) frequency else 1

        for (i in 0 until maxAlarms) {
            val alarmCalendar = (baseCalendar.clone() as Calendar).apply {
                add(Calendar.HOUR_OF_DAY, i * hourInterval)
            }
            
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("FREQ", frequency)
                putExtra("HOUR", hour)
                putExtra("MIN", minute)
                putExtra("ALARM_IDX", i)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                1000 + i,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmCalendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmCalendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("ReminderScheduler", "Alarm $i scheduled at: ${alarmCalendar.time}")
        }
    }

    fun triggerTestNotification(context: Context) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("FREQ", 1)
            putExtra("HOUR", 20)
            putExtra("MIN", 0)
        }
        context.sendBroadcast(intent)
    }
}
