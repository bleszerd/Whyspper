package bleszerd.com.github.whyspper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.models.AudioModel

class AudioAdapter(private val arrayList: List<AudioModel>) :
    RecyclerView.Adapter<AudioAdapter.ViewHolder>() {

    private lateinit var listener: OnAudioSelected

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(audioModel: AudioModel) {
            //set text labels
            itemView.findViewById<TextView>(R.id.albumItemTitle).text = audioModel.title
            itemView.findViewById<TextView>(R.id.audioItemArtist).text = audioModel.artist

            if (audioModel.albumArt != null){
                val coverImageView = itemView.findViewById<ImageView>(R.id.art)
                coverImageView.setImageBitmap(audioModel.albumArt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_audio_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioAdapter.ViewHolder, position: Int) {
        holder.bind(arrayList[position])

        //check if activity implements OnAudioSelected interface
        val context = holder.itemView.context
        if (context is OnAudioSelected) {
            listener = context
        }

        holder.itemView.setOnClickListener {
            listener.onAudioSelected(arrayList[position])
        }
    }

    override fun getItemCount(): Int = arrayList.size

    interface OnAudioSelected{
        fun onAudioSelected(audio: AudioModel)
    }

}