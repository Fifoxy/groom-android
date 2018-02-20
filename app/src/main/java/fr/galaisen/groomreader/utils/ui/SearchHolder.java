package fr.galaisen.groomreader.utils.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.model.Ticket;
import fr.galaisen.groomreader.utils.listeners.ClickListener;


public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private CardView view;
    public TextView firstname;
    public TextView lastname;
    public TextView id;

    private Ticket ticket;
    private ClickListener listener;

    public SearchHolder(View itemView) {
        super(itemView);

        view = (CardView) itemView.findViewById(R.id.cv);
        firstname = (TextView) itemView.findViewById(R.id.firstname_view);
        lastname = (TextView) itemView.findViewById(R.id.lastname_view);
        id = (TextView) itemView.findViewById(R.id.ticket_id_view);
    }


    public void setView(Ticket ticket, ClickListener listener){
        this.ticket = ticket;
        this.listener = listener;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.listener.onSearchItemClick(this.ticket);
    }
}
