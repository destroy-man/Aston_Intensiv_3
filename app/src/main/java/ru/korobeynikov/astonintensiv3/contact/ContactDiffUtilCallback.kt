package ru.korobeynikov.astonintensiv3.contact

import androidx.recyclerview.widget.DiffUtil

class ContactDiffUtilCallback(private val oldList: List<Contact>,
                              private val newList: List<Contact>, ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}