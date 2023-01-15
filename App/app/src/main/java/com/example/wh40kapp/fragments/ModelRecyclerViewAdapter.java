package com.example.wh40kapp.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.Model;
import com.example.wh40kapp.databinding.FragmentModelViewerBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Model}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ModelRecyclerViewAdapter extends RecyclerView.Adapter<ModelRecyclerViewAdapter.ViewHolder> {

    private final List<Model> mValues;

    public ModelRecyclerViewAdapter(List<Model> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return new ViewHolder(FragmentModelViewerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textView_wsValue.setText(holder.mItem.getWs()+"");
        holder.textView_bsValue.setText(holder.mItem.getBs()+"");
        holder.textView_strengthValue.setText(holder.mItem.getS()+"");
        holder.textView_toughnessValue.setText(holder.mItem.getT()+"");
        holder.textView_woundsValue.setText(holder.mItem.getW()+"");
        holder.textView_attacksValue.setText(holder.mItem.getA()+"");
        if (holder.mItem.getSaves().get("armour") != null)
            holder.textView_saveValue.setText("(A.) "+holder.mItem.getSaves().get("armour")[0]+"/"+holder.mItem.getSaves().get("armour")[1]);
        else
            holder.textView_saveValue.setText("(D.) "+holder.mItem.getSaves().get("daemonic")[0]+"/"+holder.mItem.getSaves().get("daemonic")[1]);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView_wsValue, textView_bsValue, textView_strengthValue, textView_toughnessValue, textView_woundsValue, textView_attacksValue, textView_saveValue;
        public Model mItem;

    public ViewHolder(
            FragmentModelViewerBinding binding
    ) {
      super(binding.getRoot());
        this.textView_wsValue = binding.textViewWsValue;
        this.textView_bsValue = binding.textViewBsValue;
        this.textView_strengthValue = binding.textViewStrengthValue;
        this.textView_toughnessValue = binding.textViewToughnessValue;
        this.textView_woundsValue = binding.textViewWoundsValue;
        this.textView_attacksValue = binding.textViewAttacksValue;
        this.textView_saveValue = binding.textViewSaveValue;
    }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}