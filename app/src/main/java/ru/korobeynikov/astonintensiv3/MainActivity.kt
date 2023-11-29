package ru.korobeynikov.astonintensiv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import ru.korobeynikov.astonintensiv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        val listContacts=ArrayList<Contact>()
    }

    lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        val recyclerViewContacts=binding.recyclerViewContacts
        for(i in 1..100)
            listContacts.add(Contact(i,"Name $i","Surname $i", "$i-$i-$i"))
        val layoutManager=LinearLayoutManager(this)
        adapter=ContactAdapter(listContacts)
        recyclerViewContacts.layoutManager=layoutManager
        recyclerViewContacts.adapter=adapter
    }

    fun addContact(){
        val contactDialog=ContactDialog()
        val bundle=Bundle()
        bundle.putString("nameOperation","addition")
        contactDialog.arguments=bundle
        contactDialog.show(supportFragmentManager,"contactDialog")
    }

    fun editContact(){

    }

    fun deleteContact(){

    }

    fun updateList(){
        adapter.notifyDataSetChanged()
    }
}