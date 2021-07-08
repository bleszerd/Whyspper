package bleszerd.com.github.whyspper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.models.AudioModel

class MusicAdapter(private val arrayList: List<AudioModel>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(audioModel: AudioModel){
            itemView.findViewById<TextView>(R.id.title).text = audioModel.title
            itemView.findViewById<TextView>(R.id.path).text = audioModel.location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicAdapter.ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int = arrayList.size
}