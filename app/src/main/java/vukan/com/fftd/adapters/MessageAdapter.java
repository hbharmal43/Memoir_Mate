package vukan.com.fftd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.models.Message;
import vukan.com.fftd.ui.pages.PagesFragmentDirections;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private final SimpleDateFormat sfd;
    String userName = "";
    String image = "";
    final FirebaseUser fire_user = FirebaseAuth.getInstance().getCurrentUser();

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
        this.sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void setMessages(List<Message> messages, String userName, String image) {
        this.messages = messages;
        this.userName = userName;
        this.image = image;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getReceiverID().equals(Objects.requireNonNull(fire_user).getUid()))
            return 1;
        return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new MessageViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_message, parent, false));

        return new MessageViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            if (!userName.isEmpty() && !image.isEmpty()) {
                MessageViewHolder1 messageViewHolder1 = (MessageViewHolder1) holder;
                messageViewHolder1.username.setText(userName);
                messageViewHolder1.dateTime.setText(sfd.format(messages.get(position).getDateTime().toDate()));
                messageViewHolder1.content.setText(messages.get(position).getContent());
                GlideApp.with(messageViewHolder1.senderImage.getContext()).load(image).into(messageViewHolder1.senderImage);
            }

            holder.itemView.findViewById(R.id.message_sender_image).setOnClickListener(view -> {
                PagesFragmentDirections.PorukeToMojiOglasiFragmentAction action = PagesFragmentDirections.porukeToMojiOglasiFragmentAction();
                action.setUserId(messages.get(position).getSenderID());
                Navigation.findNavController(view).navigate(action);
            });
        } else {
            MessageViewHolder2 messageViewHolder2 = (MessageViewHolder2) holder;
            messageViewHolder2.username.setText(Objects.requireNonNull(fire_user).getDisplayName());
            messageViewHolder2.dateTime.setText(sfd.format(messages.get(position).getDateTime().toDate()));
            messageViewHolder2.content.setText(messages.get(position).getContent());
            GlideApp.with(messageViewHolder2.receiverImage.getContext()).load(Objects.requireNonNull(fire_user).getPhotoUrl()).into(messageViewHolder2.receiverImage);

            holder.itemView.findViewById(R.id.message_receiver_image).setOnClickListener(view -> {
                PagesFragmentDirections.PorukeToMojiOglasiFragmentAction action = PagesFragmentDirections.porukeToMojiOglasiFragmentAction();
                action.setUserId(messages.get(position).getSenderID());
                Navigation.findNavController(view).navigate(action);
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder1 extends RecyclerView.ViewHolder {
        final CircleImageView senderImage;
        final TextView content;
        final TextView dateTime;
        final TextView username;

        MessageViewHolder1(@NonNull View itemView) {
            super(itemView);
            senderImage = itemView.findViewById(R.id.message_sender_image);
            content = itemView.findViewById(R.id.message_content);
            dateTime = itemView.findViewById(R.id.message_datetime);
            username = itemView.findViewById(R.id.message_sender);
        }
    }

    static class MessageViewHolder2 extends RecyclerView.ViewHolder {
        final CircleImageView receiverImage;
        final TextView content;
        final TextView dateTime;
        final TextView username;

        MessageViewHolder2(@NonNull View itemView) {
            super(itemView);
            receiverImage = itemView.findViewById(R.id.message_receiver_image);
            content = itemView.findViewById(R.id.message_content_r);
            dateTime = itemView.findViewById(R.id.message_datetime_r);
            username = itemView.findViewById(R.id.message_receiver);
        }
    }
}