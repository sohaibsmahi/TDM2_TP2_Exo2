package com.example.tdm2_tp2_exo2

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.annotation.RequiresApi
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class SMSReceiver : BroadcastReceiver() {
    private val channelId = "channel-01"
    private val channelName = "SIL Channel"
    private val importance = NotificationManager.IMPORTANCE_HIGH
    private lateinit var appExecutors: AppExecutors
    @RequiresApi(Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        appExecutors = AppExecutors()

        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val extras = intent.extras
        if(extras != null){
            val sms = extras.get("pdus") as Array<Any>

            for(indice in sms.indices){
                val format = extras.getString("format")

                val message = SmsMessage.createFromPdu(sms[indice] as ByteArray, format)

                val phone_num = message.originatingAddress.toString()
                val message_text = message.messageBody.toString()
                var mail = ""

                val testIntent = Intent(context, MainActivity::class.java)
                val pTestIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), testIntent, 0)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val mChannel = NotificationChannel(
                        channelId, channelName, importance)
                    notificationManager.createNotificationChannel(mChannel)

                }
                /*val index = phone_num.indexOf("+213")
                val form = "0"+phone_num.substring(index + 1)
                print(form)
                val contactPreference = ContactPreference(context)
                val contacts = contactPreference.getContacts()
                    for (con in contacts){
                        print(con)
                        val style = con.split(";").toTypedArray()
                        if (form.equals(style[0])) {
                            mail = style[1]
                            break
                        }
                    }

*/
                sendEmail("gs_smahi@esi.dz")
                val noti = Notification.Builder(context, channelId)
                    .setContentTitle("Le mail est envoyé à $mail")
                    .setContentText("Le mail est envoyé à $phone_num avec succès")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pTestIntent)
                    .setAutoCancel(true)

                    .build()

                notificationManager.notify(0, noti)

                Toast.makeText(context, "$phone_num vous a envoyé un message", Toast.LENGTH_SHORT).show()

            }
        }
    }
     fun sendEmail(mail: String){
        appExecutors.diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session =  Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    //Authenticating the password
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                val emailId = mail
                //Setting sender address
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailId))
                //Adding subject
                mm.subject = "Réponse du SMS"
                //Adding message
                mm.setText("Ce mail est une réponse de votre SMS de tout à l'heure, qu'est-ce que vous voulez ?")

                //Sending email
                Transport.send(mm)

                appExecutors.mainThread().execute {
                    //Something that should be executed on main thread.
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }

}
