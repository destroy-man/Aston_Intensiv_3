package ru.korobeynikov.astonintensiv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import ru.korobeynikov.astonintensiv3.databinding.ContactDialogBinding

class ContactDialog:DialogFragment() {

    lateinit var binding: ContactDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val nameOperation=savedInstanceState?.getString("nameOperation")
        if(nameOperation=="addition")
            dialog?.setTitle("Добавление контакта")
        binding=DataBindingUtil.inflate(inflater,R.layout.contact_dialog,container,false)
        return binding.root
    }

    fun addContact(){
        val listContacts=MainActivity.listContacts
        val idContact=listContacts.last().id+1
        val nameContact=binding.textNameContact.text.toString()
        val surnameContact=binding.textSurnameContact.text.toString()
        val phoneContact=binding.textPhoneContact.text.toString()
        listContacts.add(Contact(idContact,nameContact,surnameContact,phoneContact))
    }
}