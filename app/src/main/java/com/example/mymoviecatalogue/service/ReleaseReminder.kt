package com.example.mymoviecatalogue.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mymoviecatalogue.R
import com.example.mymoviecatalogue.activity.MainActivity
import com.example.mymoviecatalogue.model.Movie
import com.example.mymoviecatalogue.presenter.SettingPresenter
import com.example.mymoviecatalogue.view.SettingView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReleaseReminder : BroadcastReceiver(), SettingView.View {
    private val movies = ArrayList<Movie>()

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "22"

        private fun getReminderPendingIntent(context: Context) : PendingIntent {
            val alarmIntent = Intent(context, ReleaseReminder::class.java)

            return PendingIntent.getBroadcast(context, 28, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val appName = context.getString(R.string.app_name)
        val presenter: SettingView.Presenter = SettingPresenter(this)
        presenter.setRepeatingReminder(context, appName)
    }

    override fun setReminder(movie: ArrayList<Movie>) {
        movies.clear()
        movies.addAll(movie)
    }

    override fun onSuccess(context: Context, appName: String?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val currentDate = dateFormat.format(date)

        val notifyId = 0

        if (movies.size > 0) {
            val thread = object : Thread() {
                override fun run() {
                    try {
                        for (movie in movies) {
                            if(movie.date.equals(currentDate)) {
                                showNotification(context, appName, notifyId, movie.title.toString())
                            }
                            sleep(2000)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            thread.start()
        }
    }

    override fun errorHandler(t: Throwable?) {

    }

    private fun showNotification(context: Context, appName: String?, notifyId: Int, movieTitle: String?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_foreground)
                .setContentTitle(appName)
                .setContentText(String.format(context.getString(R.string.release_reminder_msg), movieTitle))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000, 1000))
                .setSound(ringtone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_UPCOMING", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_UPCOMING", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 100, 100, 100, 100, 100)

            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(notifyId, builder.build())
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, getReminderPendingIntent(context))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, getReminderPendingIntent(context))
        }

        Toast.makeText(context, context.getString(R.string.release_enabled_msg), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getReminderPendingIntent(context))
        Toast.makeText(context, context.getString(R.string.release_disabled_msg), Toast.LENGTH_SHORT).show()
    }
}