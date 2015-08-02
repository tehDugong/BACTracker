package org.cs160.bactracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avifrankl on 8/1/15.
 */
public class EntryAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater vi;

    public EntryAdapter(Context context,ArrayList<Item> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(R.layout.section_item, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.section_text);
                sectionView.setText(si.getTitle());

            } else {
                EntryItem ei = (EntryItem) i;
                v = vi.inflate(R.layout.list_item, null);
                final TextView title = (TextView) v.findViewById(R.id.entry_text);


                if (title != null)
                    title.setText(ei.title);
            }
        }
        return v;
    }

}
