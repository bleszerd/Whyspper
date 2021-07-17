package bleszerd.com.github.whyspper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.exceptions.ListenerException
import bleszerd.com.github.whyspper.listeners.AudioTapListener
import bleszerd.com.github.whyspper.models.AudioModel

class AudioAdapter(private val arrayList: List<AudioModel>) :
    RecyclerView.Adapter<AudioAdapter.ViewHolder>() {

    companion object {
        const val INITIAL_SELECTED_POSITION_VALUE: Int = -1
        var listener: AudioTapListener? = null
        var selectedPositions: Int = INITIAL_SELECTED_POSITION_VALUE;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(audioModel: AudioModel) {
            itemView.isSelected = selectedPositions == adapterPosition

            //set text labels
            itemView.findViewById<TextView>(R.id.albumItemTitle).text = audioModel.title
            itemView.findViewById<TextView>(R.id.audioItemArtist).text = audioModel.artist
            val coverImageView = itemView.findViewById<ImageView>(R.id.art)

            if (audioModel.albumArt != null){
                coverImageView.setImageBitmap(audioModel.albumArt)
            } else {
                coverImageView.setImageResource(R.drawable.ic_album)
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
        val currentAudioModel = arrayList[position]
        holder.bind(currentAudioModel)

        if(listener == null)
            throw ListenerException(this::class.java)

        holder.itemView.setOnClickListener { view ->
            selectedPositions = holder.adapterPosition

            listener?.onAudioSelected(view, currentAudioModel)

            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = arrayList.size
}