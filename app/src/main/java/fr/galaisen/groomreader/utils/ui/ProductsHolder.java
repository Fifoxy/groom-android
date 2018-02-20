package fr.galaisen.groomreader.utils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.galaisen.groomreader.R;


public class ProductsHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView price;
    public TextView id;

    public ProductsHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.product_name_view);
        price = (TextView) itemView.findViewById(R.id.price_view);
        id = (TextView) itemView.findViewById(R.id.product_id_view);
    }
}
