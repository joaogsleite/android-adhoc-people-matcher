package pt.ulisboa.tecnico.cmu.locmess.main.locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmu.locmess.R;

public class LocationAdapter extends ArrayAdapter<LocationModel> {

    private static class ViewHolder {
        TextView type;
        TextView name;
    }
    private int lastPosition = -1;
    public List<LocationModel> list;

    public LocationAdapter(Context context, List<LocationModel> list){
        super(context, R.layout.layout_pair_locations);
        this.list = list;
    }

    public void insertItem(LocationModel p){

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSsid().equals(p.getSsid())){
                list.get(i).setName(p.getName());
                notifyDataSetChanged();
                return;
            }
        }

        list.add(p);
        notifyDataSetChanged();
    }


    @Override
    public LocationModel getItem(int position){
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LocationModel location = list.get(position);
        ViewHolder holder;

        final View result;

        if (view == null) {

            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.layout_pair_locations, parent, false);

            holder.type = (TextView) view.findViewById(R.id.type);
            holder.name = (TextView) view.findViewById(R.id.name);

            result = view;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            result = view;
        }

        lastPosition = position;
        holder.type.setText(location.getType());
        holder.name.setText(location.getName());

        return result;
    }

}