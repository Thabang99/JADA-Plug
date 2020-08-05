package bw.ac.biust.datingapp.Cards;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;

        import java.util.List;

        import bw.ac.biust.datingapp.R;

public class arrayAdapter extends ArrayAdapter<cards> {


    Context context;
    public arrayAdapter(Context context, int resourceId, List<cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        cards card_item = (cards) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.helloText);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView bio = (TextView) convertView.findViewById(R.id.bioText);

        name.setText(card_item.getName());
        bio.setText(card_item.getBio());
        switch (card_item.getProfileImageUrl()){

            case "default":
                Glide.with(convertView.getContext()).load(R.drawable.person).into(image);
                break;

            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;

        }

        // Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(image);

        return convertView;
    }
}
