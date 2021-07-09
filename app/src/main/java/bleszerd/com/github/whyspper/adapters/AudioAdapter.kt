package bleszerd.com.github.whyspper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.models.AudioModel
import bleszerd.com.github.whyspper.ui.fragments.AudioListFragment
import bleszerd.com.github.whyspper.ui.fragments.AudioListFragment.OnAudioSelected

class AudioAdapter(private val arrayList: List<AudioModel>) :
    RecyclerView.Adapter<AudioAdapter.ViewHolder>() {
    private lateinit var listener: OnAudioSelected

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(audioModel: AudioModel) {
            itemView.findViewById<TextView>(R.id.title).text = audioModel.title
            itemView.findViewById<TextView>(R.id.path).text = audioModel.location

            if (audioModel.art != null) {
                itemView.findViewById<ImageView>(R.id.art).setImageBitmap(audioModel.art)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.audio_item_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioAdapter.ViewHolder, position: Int) {
        holder.bind(arrayList[position])
        val context = holder.itemView.context
        if (context is OnAudioSelected) {
            listener = context
        }
        holder.itemView.setOnClickListener {
            listener.onAudioSelected(arrayList[position])
        }
    }

    override fun getItemCount(): Int = arrayList.size


}