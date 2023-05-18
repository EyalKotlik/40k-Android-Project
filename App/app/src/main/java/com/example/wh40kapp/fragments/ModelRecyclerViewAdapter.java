package com.example.wh40kapp.fragments;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.Model;
import com.example.wh40kapp.Wargear;
import com.example.wh40kapp.databinding.FragmentModelViewerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Model}.
 */
public class ModelRecyclerViewAdapter extends
        RecyclerView.Adapter<ModelRecyclerViewAdapter.ViewHolder> {

    private final List<Model> mValues;

    public ModelRecyclerViewAdapter(List<Model> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentModelViewerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textView_modelName.setText(holder.mItem.getName() + "");
        holder.textView_wsValue.setText(holder.mItem.getWs() + "");
        holder.textView_bsValue.setText(holder.mItem.getBs() + "");
        holder.textView_strengthValue.setText(holder.mItem.getS() + "");
        holder.textView_toughnessValue.setText(holder.mItem.getT() + "");
        holder.textView_woundsValue.setText(holder.mItem.getW() + "");
        holder.textView_attacksValue.setText(holder.mItem.getA() + "");
        holder.textView_modelCostValue.setText(holder.mItem.getCost() + "");
        holder.editText_numOfModelValue.setText(holder.mItem.getModel_num() + "");
        holder.textView_equipmentDescription.setText(holder.mItem.getUnitComposition()+"");
        holder.textView_wargearOptions.setText(holder.mItem.getWargearOptions()+"");
        TextWatcher numOfModelValueTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {
                try {
                    holder.mItem.setModel_num(Integer.parseInt(s.toString()));
                    Log.d("TAG", "afterTextChanged: "+holder.mItem.getModel_num()+" "+holder.mItem.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        holder.editText_numOfModelValue.addTextChangedListener(numOfModelValueTextWatcher);
        if (holder.mItem.getSaves().get("armour") != null)
            holder.textView_saveValue.setText("(A.) " + holder.mItem.getSaves()
                    .get("armour")[0] + "/" + holder.mItem.getSaves().get("armour")[1]);
        else
            holder.textView_saveValue.setText("(D.) " + holder.mItem.getSaves()
                    .get("daemonic")[0] + "/" + holder.mItem.getSaves().get("daemonic")[1]);
        ArrayList<Wargear> wargear = new ArrayList<Wargear>();
        try {
            for (int i = 0; i < holder.mItem.getWargear().size(); i++) {
                wargear.add(holder.mItem.getWargear().get(i));
                Log.d("TAG", "onBindViewHolder: "+wargear.get(i).getName()+" "+wargear.get(i).getProfileChoice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.recyclerView_wargearList.setAdapter(new WargearRecyclerViewAdapter(wargear));
        holder.recyclerView_wargearList.setLayoutManager(new LinearLayoutManager(holder.recyclerView_wargearList.getContext()));
        holder.setTexEditListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches("\\d+")) {
                    holder.mItem.setModel_num(Integer.parseInt(editable.toString()));
                    Log.d("TAG", "afterTextChanged: " + holder.mItem.getModel_num() + " " + holder.mItem.getName());
                    return;
                }
                Toast.makeText(holder.editText_numOfModelValue.getContext(), "Model num can only be a positive integer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView_modelName, textView_wsValue, textView_bsValue,
                textView_strengthValue, textView_toughnessValue, textView_woundsValue,
                textView_attacksValue, textView_saveValue, textView_modelCostValue, textView_equipmentDescription, textView_wargearOptions;
        public final RecyclerView recyclerView_wargearList;
        public Model mItem;
        public final EditText editText_numOfModelValue;

        public ViewHolder(
                FragmentModelViewerBinding binding
        ) {
            super(binding.getRoot());
            this.textView_modelName = binding.textViewModelName;
            this.textView_wsValue = binding.textViewWsValue;
            this.textView_bsValue = binding.textViewBsValue;
            this.textView_strengthValue = binding.textViewStrengthValue;
            this.textView_toughnessValue = binding.textViewToughnessValue;
            this.textView_woundsValue = binding.textViewWoundsValue;
            this.textView_attacksValue = binding.textViewAttacksValue;
            this.textView_saveValue = binding.textViewSaveValue;
            this.recyclerView_wargearList = binding.recyclerViewWargearList;
            this.textView_modelCostValue = binding.textViewModelCostValue;
            this.editText_numOfModelValue = binding.editTextNumOfModelValue;
            this.textView_equipmentDescription = binding.textViewEquipmentDescription;
            this.textView_wargearOptions = binding.textViewWargearOptions;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }

        public void setTexEditListener(TextWatcher textEditListener) {
            editText_numOfModelValue.addTextChangedListener(textEditListener);
        }
    }
}