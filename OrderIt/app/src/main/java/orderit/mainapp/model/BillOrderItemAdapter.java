package orderit.mainapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import orderit.mainapp.R;

/**
 * Created by Blackcool on 2/28/16.
 */
public class BillOrderItemAdapter extends ArrayAdapter<BillOrderItem>{
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<BillOrderItem> billOrderItemList = null;
    private ArrayList<BillOrderItem> dataList;


    public BillOrderItemAdapter(Context context, List<BillOrderItem> objects) {
        super(context, 0, objects);

        this.mContext = context;
        this.billOrderItemList = objects;
        this.inflater = LayoutInflater.from(mContext);
        this.dataList = new ArrayList<BillOrderItem>();
        this.dataList.addAll(billOrderItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.bill_order_row, parent, false);
        }
        BillOrderItem billOrderItem = billOrderItemList.get(position);
        TextView orderName = (TextView) convertView.findViewById(R.id.tvOrderName);
        orderName.setText(billOrderItem.getOrderName());

        TextView quantity = (TextView) convertView.findViewById(R.id.tvQuantity);
        quantity.setText(billOrderItem.getQuantity());

        TextView singlePrice = (TextView) convertView.findViewById(R.id.tvSinglePrice);
        singlePrice.setText(billOrderItem.getSinglePrice());
        return convertView;
    }
}
