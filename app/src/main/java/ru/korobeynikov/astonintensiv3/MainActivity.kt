package ru.korobeynikov.astonintensiv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import ru.korobeynikov.astonintensiv3.contact.Contact
import ru.korobeynikov.astonintensiv3.contact.ContactAdapter
import ru.korobeynikov.astonintensiv3.contact.ContactDialog
import ru.korobeynikov.astonintensiv3.contact.ContactDiffUtilCallback
import ru.korobeynikov.astonintensiv3.contact.ContactViewModel
import ru.korobeynikov.astonintensiv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var isMultipleDelete = false
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var recyclerTouchListener: RecyclerTouchListener
    private lateinit var recyclerDeleteListener: RecyclerTouchListener
    var listDeleteContacts = ArrayList<Contact>()
    lateinit var adapter: ContactAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.view = this

        recyclerTouchListener =
            RecyclerTouchListener(this, object : RecyclerTouchListener.ClickListener {
                override fun onClick(view: View, position: Int) {
                    createAndShowDialog("editing", position)
                }
            })
        recyclerDeleteListener =
            RecyclerTouchListener(this, object : RecyclerTouchListener.ClickListener {
                override fun onClick(view: View, position: Int) {
                    val contact = adapter.dataSet[position]
                    if (!listDeleteContacts.contains(contact)) {
                        listDeleteContacts.add(contact)
                        val deleteContacts = binding.textDeleteContacts.text
                        binding.textDeleteContacts.text = getString(
                            R.string.text_delete_contacts,
                            deleteContacts,
                            contact.name,
                            contact.surname
                        )
                    } else {
                        listDeleteContacts.remove(contact)
                        val deleteContacts = binding.textDeleteContacts.text.toString()
                        binding.textDeleteContacts.text =
                            deleteContacts.replace("\n${contact.name} ${contact.surname}", "")
                    }
                }
            })

        contactViewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        contactViewModel.getData()?.observe(this) {
            adapter.dataSet = it[0] as ArrayList<Contact>
            binding.btnAdd.visibility = it[1] as Int
            binding.btnCancel.visibility = it[2] as Int
            binding.btnDelete.visibility = it[3] as Int
            binding.recyclerViewContacts.layoutParams = it[4] as LayoutParams
            binding.btnGarbage.isEnabled = it[5] as Boolean
            binding.textDeleteContacts.visibility = it[6] as Int
            binding.textDeleteContacts.text = it[7] as CharSequence
            listDeleteContacts = it[8] as ArrayList<Contact>
            isMultipleDelete = it[9] as Boolean
            binding.recyclerViewContacts.removeOnItemTouchListener(recyclerTouchListener)
            if (!isMultipleDelete)
                binding.recyclerViewContacts.addOnItemTouchListener(recyclerTouchListener)
            else
                binding.recyclerViewContacts.addOnItemTouchListener(recyclerDeleteListener)
        }

        val recyclerViewContacts = binding.recyclerViewContacts
        val listContacts = ArrayList<Contact>()
        for (i in 1..100)
            listContacts.add(Contact(i, "Name $i", "Surname $i", "$i-$i-$i"))
        adapter = ContactAdapter(listContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        recyclerViewContacts.adapter = adapter
        recyclerViewContacts.addOnItemTouchListener(recyclerTouchListener)
    }

    fun addContact() = createAndShowDialog("addition")

    fun createAndShowDialog(nameOperation: String, position: Int = -1) {
        val contactDialog = ContactDialog()
        val bundle = Bundle()
        bundle.putString("nameOperation", nameOperation)
        if (nameOperation == "editing")
            bundle.putInt("position", position)
        contactDialog.arguments = bundle
        contactDialog.show(supportFragmentManager, "contactDialog")
    }

    fun clickGarbage() {
        isMultipleDelete = true
        binding.recyclerViewContacts.removeOnItemTouchListener(recyclerTouchListener)
        val lpRecyclerView = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)
        lpRecyclerView.bottomToTop = R.id.btnCancel
        lpRecyclerView.topToTop = ConstraintSet.PARENT_ID
        binding.recyclerViewContacts.layoutParams = lpRecyclerView
        binding.btnGarbage.isEnabled = false
        binding.btnAdd.visibility = View.GONE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnCancel.visibility = View.VISIBLE
        binding.textDeleteContacts.visibility = View.VISIBLE
        binding.recyclerViewContacts.addOnItemTouchListener(recyclerDeleteListener)
    }

    fun cancel() {
        isMultipleDelete = false
        binding.recyclerViewContacts.removeOnItemTouchListener(recyclerDeleteListener)
        val lpRecyclerView = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)
        lpRecyclerView.bottomToTop = R.id.btnAdd
        lpRecyclerView.topToTop = ConstraintSet.PARENT_ID
        binding.recyclerViewContacts.layoutParams = lpRecyclerView
        binding.btnGarbage.isEnabled = true
        binding.btnAdd.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.GONE
        binding.btnCancel.visibility = View.GONE
        binding.textDeleteContacts.visibility = View.GONE
        listDeleteContacts.clear()
        binding.textDeleteContacts.text = "Удалить:"
        binding.recyclerViewContacts.addOnItemTouchListener(recyclerTouchListener)
    }

    fun deleteContacts() =
        if (listDeleteContacts.isNotEmpty()) {
            val newListContacts = ArrayList<Contact>(adapter.dataSet.size)
            for (contact in adapter.dataSet)
                newListContacts.add(Contact(contact.id, contact.name, contact.surname, contact.phoneNumber))
            newListContacts.removeAll(listDeleteContacts.toSet())
            updateList(newListContacts)
            cancel()
        } else
            Toast.makeText(this, "Необходимо указать хотя бы 1 контакт", Toast.LENGTH_SHORT).show()

    fun updateList(listContacts: ArrayList<Contact>) {
        val contactDiffUtilCallback = ContactDiffUtilCallback(adapter.dataSet, listContacts)
        val contactDiffResult = DiffUtil.calculateDiff(contactDiffUtilCallback)
        adapter.dataSet = listContacts
        contactDiffResult.dispatchUpdatesTo(adapter)
    }

    override fun onStop() {
        super.onStop()
        contactViewModel.loadData(
            arrayOf(
                adapter.dataSet,
                binding.btnAdd.visibility,
                binding.btnCancel.visibility,
                binding.btnDelete.visibility,
                binding.recyclerViewContacts.layoutParams,
                binding.btnGarbage.isEnabled,
                binding.textDeleteContacts.visibility,
                binding.textDeleteContacts.text,
                listDeleteContacts,
                isMultipleDelete
            )
        )
    }
}