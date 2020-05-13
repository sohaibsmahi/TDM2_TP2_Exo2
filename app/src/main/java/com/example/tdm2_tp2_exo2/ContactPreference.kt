package com.example.tdm2_tp2_exo2

import android.content.Context

class ContactPreference (context: Context){
    val NAME = "Contact SharedPreference"
    val CONTACTS = "Selected Contacts"

    val preference = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun getContacts(): MutableSet<String> {
        return preference.getStringSet(CONTACTS, mutableSetOf()) as MutableSet<String>
    }
    fun setContacts(contacts:MutableSet<String>){
        val editor = preference.edit()
        editor.putStringSet(CONTACTS, contacts)
        editor.apply()
    }
}
