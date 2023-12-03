package ru.korobeynikov.astonintensiv3.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.korobeynikov.astonintensiv3.R

class ContactAdapter(var dataSet: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textName: TextView
        private val textSurname: TextView
        private val textPhone: TextView

        init {
            textName = view.findViewById(R.id.textNameContact)
            textSurname = view.findViewById(R.id.textSurnameContact)
            textPhone = view.findViewById(R.id.textPhoneContact)
        }

        fun bind(contact: Contact) {
            textName.text = contact.name
            textSurname.text = contact.surname
            textPhone.text = contact.phoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }
}