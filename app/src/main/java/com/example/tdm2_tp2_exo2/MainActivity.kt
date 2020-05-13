package com.example.tdm2_tp2_exo2

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var REQUEST_READ_CONTACTS_SMS = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        var contacts: MutableList<Contact> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECEIVE_SMS
                ) == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED
        ) {

            var list = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )

            if (list != null) {
                while (list.moveToNext()) {
                    val name =
                            list.getString(list.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone =
                            list.getString(list.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    var email :String = " "
                    val contactId= list.getString(list.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID))
                    val emails: Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null)
                    if (emails != null) {
                        while (emails.moveToNext()) {
                            email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                            break
                        }
                    }
                    if (email.isNullOrEmpty()){email=" df"}
                    var contact = Contact(name, phone, email)
                    contacts.add(contact)
                }


                recyclerView.adapter = ListAdapter(contacts as ArrayList<Contact>)
                list.close()
            }

        }else{
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.INTERNET),
                    REQUEST_READ_CONTACTS_SMS)

        }

    }
}
