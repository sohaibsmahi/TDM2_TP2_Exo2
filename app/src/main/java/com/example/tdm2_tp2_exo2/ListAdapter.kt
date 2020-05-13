package com.example.tdm2_tp2_exo2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val contacts: ArrayList<Contact>):
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.contact_data, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var contact:Contact = contacts[position]
        holder.name.text = contact.name
        holder.phone.text = contact.phone
        holder.mail.text = contact.mail

        var format = contact.phone+";"+contact.mail
        val contactPreference = ContactPreference(holder.context)
        var contacts = contactPreference.getContacts()

        holder.select.text = if (contacts.contains(format)) "Déséléctionner" else "Séléctionner"

        holder.select.setOnClickListener {

                if (contacts.contains(format)){
                    contacts.remove(format)
                    contactPreference.setContacts(contacts)
                    holder.select.text = "Séléctionner"
                } else{
                    contacts.add(format)
                    contactPreference.setContacts(contacts)
                    holder.select.text = "Déséléctionner"
                }

        }

    }
    class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        var name: TextView = item.findViewById(R.id.name)
        var phone: TextView = item.findViewById(R.id.phone)
        var mail: TextView = item.findViewById(R.id.mail)
        var select: Button = item.findViewById(R.id.select)
        var context = item.context
    }
}