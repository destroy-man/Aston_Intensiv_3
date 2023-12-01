package ru.korobeynikov.astonintensiv3.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import ru.korobeynikov.astonintensiv3.MainActivity
import ru.korobeynikov.astonintensiv3.R
import ru.korobeynikov.astonintensiv3.databinding.ContactDialogBinding

class ContactDialog : DialogFragment() {

    private var positionContact = -1
    private lateinit var binding: ContactDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?, ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_dialog, container, false)
        binding.view = this
        val nameOperation = arguments?.getString("nameOperation")
        if (nameOperation == "addition")
            binding.btnContact.text = getText(R.string.button_add)
        else if (nameOperation == "editing") {
            binding.btnContact.text = getText(R.string.button_edit)
            positionContact = arguments?.getInt("position") ?: -1
        }
        return binding.root
    }

    fun clickButton() =
        if (positionContact == -1)
            addContact()
        else
            editContact()

    private fun addContact() {
        val mainActivity = activity as MainActivity
        val listContacts = mainActivity.adapter.dataSet
        val newListContacts = ArrayList<Contact>(listContacts.size)
        for (contact in listContacts)
            newListContacts.add(Contact(contact.id, contact.name, contact.surname, contact.phoneNumber))
        val idContact = newListContacts.last().id + 1
        val nameContact = binding.textNameContact.text.toString()
        val surnameContact = binding.textSurnameContact.text.toString()
        val phoneContact = binding.textPhoneContact.text.toString()
        newListContacts.add(Contact(idContact, nameContact, surnameContact, phoneContact))
        mainActivity.updateList(newListContacts)
        dismiss()
    }

    private fun editContact() {
        val mainActivity = activity as MainActivity
        val listContacts = mainActivity.adapter.dataSet
        val newListContacts = ArrayList<Contact>(listContacts.size)
        for (contact in listContacts)
            newListContacts.add(Contact(contact.id, contact.name, contact.surname, contact.phoneNumber))
        val contact = newListContacts[positionContact]
        val nameContact = binding.textNameContact.text.toString()
        val surnameContact = binding.textSurnameContact.text.toString()
        val phoneContact = binding.textPhoneContact.text.toString()
        if (nameContact.isNotEmpty())
            contact.name = nameContact
        if (surnameContact.isNotEmpty())
            contact.surname = surnameContact
        if (phoneContact.isNotEmpty())
            contact.phoneNumber = phoneContact
        newListContacts[positionContact] = contact
        mainActivity.updateList(newListContacts)
        dismiss()
    }

    fun cancel() = dismiss()
}