package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RatingsListAdapter extends ArrayAdapter<Rating> {
    private final Context context;
    private final Rating[] ratings;

    public RatingsListAdapter(Context context, Rating[] ratings) {
        super(context, R.layout.ratings_list_item, ratings);
        this.context = context;
        this.ratings = ratings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ratingsListItem;
        if (convertView == null) {
            ratingsListItem = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.ratings_list_item, parent, false);
        } else {
            ratingsListItem = convertView;
        }
        ((TextView) ratingsListItem.findViewById(R.id.ratings_list_item_description))
                .setText(ratings[position].description);
        ((TextView) ratingsListItem.findViewById(R.id.ratings_list_item_rating))
                .setText((new Integer(ratings[position].value)).toString());
        return ratingsListItem;
    }
}
