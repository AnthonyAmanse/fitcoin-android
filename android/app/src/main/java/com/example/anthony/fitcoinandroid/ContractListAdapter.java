package com.example.anthony.fitcoinandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;

public class ContractListAdapter extends RecyclerView.Adapter<ContractListAdapter.ContractListViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<ContractModel> contractModels;

    public ContractListAdapter(Context context, ArrayList<ContractModel> contractModels) {
        this.context = context;
        this.contractModels = contractModels;
    }

    @Override
    public ContractListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contract_item, null);
        return new ContractListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContractListViewHolder holder, int position) {
        ContractModel contractModel = contractModels.get(position);

        String contractDetails = String.valueOf(contractModel.quantity) + " of " + contractModel.productName;

        holder.cancelContractButton.setVisibility(View.GONE);

        holder.contractIdFromList.setText(contractModel.getContractId());
        holder.contractStateFromList.setText(contractModel.getState());
        holder.contractDetails.setText(contractDetails);

        holder.contractCard.setTag(contractModel);
        holder.contractCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final ContractModel contractModel = (ContractModel) view.getTag();

        Button cancelButton = view.findViewById(R.id.cancelContractButton);

        if (cancelButton.getVisibility() == View.GONE) {
            cancelButton.setVisibility(View.VISIBLE);
        } else {
            cancelButton.setVisibility(View.GONE);
        }



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FITNESS_CONTRACT_ITEM",contractModel.getContractId());

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Decline this contract?");
                alertDialog.setMessage("Do you want to nullify contract " + contractModel.getContractId() + ".\nThe items are: " + contractModel.getQuantity() + " of " + contractModel.getProductName());
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Decline",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                // This finishes the activity
//                ((AppCompatActivity) view.getContext()).finish();
            }
        });

//        Intent intent = new Intent(view.getContext(),ContractDetails.class);
//        intent.putExtra("CONTRACT_JSON", new Gson().toJson(contractModel, ContractModel.class));
//
//        view.getContext().startActivity(intent);
    }

    public void declineContract() {

    }

    public class ContractListViewHolder extends RecyclerView.ViewHolder {
        TextView contractIdFromList, contractStateFromList, contractDetails;
        CardView contractCard;
        Button cancelContractButton;

        public ContractListViewHolder(View view) {
            super(view);

            contractIdFromList = view.findViewById(R.id.contractIdFromList);
            contractStateFromList = view.findViewById(R.id.contractStateFromList);
            contractDetails = view.findViewById(R.id.contractDetails);
            contractCard = view.findViewById(R.id.contractCard);
            cancelContractButton = view.findViewById(R.id.cancelContractButton);
        }
    }

    @Override
    public int getItemCount() {
        return contractModels.size();
    }
}
