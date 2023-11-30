package ru.korobeynikov.astonintensiv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import ru.korobeynikov.astonintensiv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val listContacts=ArrayList<Contact>()
    lateinit var adapter: ContactAdapter
    lateinit var binding:ActivityMainBinding
    lateinit var recyclerTouchListener: RecyclerTouchListener
    var listDeleteContacts=ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.view=this
        val recyclerViewContacts=binding.recyclerViewContacts
        for(i in 1..100)
            listContacts.add(Contact(i,"Name $i","Surname $i", "$i-$i-$i"))
        val layoutManager=LinearLayoutManager(this)
        adapter=ContactAdapter(listContacts)
        recyclerViewContacts.layoutManager=layoutManager
        recyclerViewContacts.adapter=adapter
        recyclerTouchListener=RecyclerTouchListener(this,object :RecyclerTouchListener.ClickListener{
            override fun onClick(view: View, position: Int) {
                createAndShowDialog("editing",position)
            }
        })
        recyclerViewContacts.addOnItemTouchListener(recyclerTouchListener)
    }

    fun addContact(){
        createAndShowDialog("addition")
    }

    fun createAndShowDialog(nameOperation:String,position:Int=-1){
        val contactDialog=ContactDialog()
        val bundle=Bundle()
        bundle.putString("nameOperation",nameOperation)
        if(nameOperation=="editing")
            bundle.putInt("position",position)
        contactDialog.arguments=bundle
        contactDialog.show(supportFragmentManager,"contactDialog")
    }

    fun clickGarbage(){
        binding.recyclerViewContacts.removeOnItemTouchListener(recyclerTouchListener)
        val lpRecyclerView=ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,0)
        lpRecyclerView.bottomToTop=R.id.btnCancel
        lpRecyclerView.topToTop=ConstraintSet.PARENT_ID
        binding.recyclerViewContacts.layoutParams=lpRecyclerView
        binding.btnGarbage.isEnabled=false
        binding.btnAdd.visibility=View.GONE
        binding.btnDelete.visibility=View.VISIBLE
        binding.btnCancel.visibility=View.VISIBLE
        binding.textDeleteContacts.visibility=View.VISIBLE
        recyclerTouchListener=RecyclerTouchListener(this,object :RecyclerTouchListener.ClickListener{
            override fun onClick(view: View, position: Int) {
                val contact=adapter.dataSet[position]
                if(!listDeleteContacts.contains(contact)){
                    listDeleteContacts.add(contact)
                    val deleteContacts=binding.textDeleteContacts.text
                    binding.textDeleteContacts.text="$deleteContacts\n${contact.name} ${contact.surname}"
                } else{
                    listDeleteContacts.remove(contact)
                    val deleteContacts=binding.textDeleteContacts.text.toString()
                    binding.textDeleteContacts.text=deleteContacts.replace("\n${contact.name} ${contact.surname}","")
                }
            }
        })
        binding.recyclerViewContacts.addOnItemTouchListener(recyclerTouchListener)
    }

    fun cancel(){
        binding.recyclerViewContacts.removeOnItemTouchListener(recyclerTouchListener)
        val lpRecyclerView=ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,0)
        lpRecyclerView.bottomToTop=R.id.btnAdd
        lpRecyclerView.topToTop=ConstraintSet.PARENT_ID
        binding.recyclerViewContacts.layoutParams=lpRecyclerView
        binding.btnGarbage.isEnabled=true
        binding.btnAdd.visibility=View.VISIBLE
        binding.btnDelete.visibility=View.GONE
        binding.btnCancel.visibility=View.GONE
        binding.textDeleteContacts.visibility=View.GONE
        listDeleteContacts.clear()
        binding.textDeleteContacts.text="Удалить:"
        recyclerTouchListener=RecyclerTouchListener(this,object :RecyclerTouchListener.ClickListener{
            override fun onClick(view: View, position: Int) {
                createAndShowDialog("editing",position)
            }
        })
        binding.recyclerViewContacts.addOnItemTouchListener(recyclerTouchListener)
    }

    fun deleteContacts(){
        if(listDeleteContacts.isNotEmpty()){
            listContacts.removeAll(listDeleteContacts)
            updateList()
            cancel()
        }
        else
            Toast.makeText(this,"Необходимо указать хотя бы 1 контакт",Toast.LENGTH_SHORT).show()
    }

    fun updateList(){
        adapter.notifyDataSetChanged()
    }
}