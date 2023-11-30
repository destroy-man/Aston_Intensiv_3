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
    var positionContact=-1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.contact_dialog,container,false)
        binding.view=this
        val nameOperation=arguments?.getString("nameOperation")
        if(nameOperation=="addition")
            binding.btnContact.text="Добавить"
        else if(nameOperation=="editing"){
            binding.btnContact.text="Изменить"
            positionContact=arguments?.getInt("position") ?: -1
        }
        return binding.root
    }

    fun clickButton(){
        if(positionContact==-1)
            addContact()
        else
            editContact()
    }

    fun addContact(){
        val mainActivity=activity as MainActivity
        val listContacts=mainActivity.listContacts
        val idContact=listContacts.last().id+1
        val nameContact=binding.textNameContact.text.toString()
        val surnameContact=binding.textSurnameContact.text.toString()
        val phoneContact=binding.textPhoneContact.text.toString()
        listContacts.add(Contact(idContact,nameContact,surnameContact,phoneContact))
        mainActivity.updateList()
        dismiss()
    }

    fun editContact(){
        val mainActivity=activity as MainActivity
        val listContacts=mainActivity.listContacts
        val contact=listContacts[positionContact]
        val nameContact=binding.textNameContact.text.toString()
        val surnameContact=binding.textSurnameContact.text.toString()
        val phoneContact=binding.textPhoneContact.text.toString()
        if(nameContact.isNotEmpty())
            contact.name=nameContact
        if(surnameContact.isNotEmpty())
            contact.surname=surnameContact
        if(phoneContact.isNotEmpty())
            contact.phoneNumber=phoneContact
        listContacts[positionContact]=contact
        mainActivity.updateList()
        dismiss()
    }

    fun cancel(){
        dismiss()
    }
}