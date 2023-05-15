package vukan.com.fftd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vukan.com.fftd.R;
import vukan.com.fftd.models.Conv;
import vukan.com.fftd.models.Message;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private List<Conv> conversations;
    private final SimpleDateFormat sfd;
    final private ListItemClickListener mOnClickListener;

    public ConversationAdapter(ListItemClickListener listener) {
        this.conversations = new ArrayList<>();
        this.sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        mOnClickListener = listener;
    }

    public void setConversations(List<Conv> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.bind(position);


        holder.itemView.setOnLongClickListener(view -> {
            mOnClickListener.onListItemClick(conversations.get(position), view);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        final TextView senderName;
        final TextView adName;
        final TextView lastMessage;
        final TextView date;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.sender_name);
            adName = itemView.findViewById(R.id.ad_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            date = itemView.findViewById(R.id.date);
        }

        void bind(int index) {
            adName.setText(conversations.get(index).getPostName());
            senderName.setText(conversations.get(index).getUserName());
            lastMessage.setText(conversations.get(index).getMessages().get(conversations.get(index).getMessages().size() - 1).getContent());
            date.setText(sfd.format(conversations.get(index).getMessages().get(conversations.get(index).getMessages().size() - 1).getDateTime().toDate()));
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(Conv conv, View view);
    }
}