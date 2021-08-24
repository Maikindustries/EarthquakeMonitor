package com.example.earthquakemonitor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquakemonitor.databinding.EqListItemBinding

private val TAG = EqAdapter::class.java.simpleName

/*Primer parametro de lis adapter es para el tipo de dato, el segundo es para que se reciclen(bajas la lista y el de arrbia se desaparece y aparecen nuevos)*/
class EqAdapter: ListAdapter<Earthquake, EqAdapter.EqViewHolder>(DiffCallBack) {

    /*Es para verificar cual adapter se quito o se cambió*/
    companion object DiffCallBack : DiffUtil.ItemCallback<Earthquake>() {
        override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem == newItem
        }
    }

    lateinit var onItemClickListener: (Earthquake) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EqAdapter.EqViewHolder {
        /* Funciona con Relative Layout
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.eq_list_item, parent, false)
        return EqViewHolder(view) y modificar más cosas(checar video 50 y 51 curso udemy android)*/
        /*Para que funcione con Data binding*/
        val binding: EqListItemBinding = EqListItemBinding.inflate(LayoutInflater.from(parent.context))
        return EqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EqAdapter.EqViewHolder, position: Int) {
        val earthquake = getItem(position)
        holder.bind(earthquake)
    }

    inner class EqViewHolder(private  val binding: EqListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(earthquake: Earthquake) {
            binding.eqMagnitudeText.text = earthquake.magnitude.toString()
            binding.eqPlaceText.text = earthquake.place
            binding.root.setOnClickListener {
                if (::onItemClickListener.isInitialized){
                    onItemClickListener(earthquake)
                } else {
                    Log.e(TAG, "onItemClickListener not initialized")
                }
            }
            binding.executePendingBindings()
        }
    }
}