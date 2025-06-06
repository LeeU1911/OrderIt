package orderit.mainapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import orderit.mainapp.R;
import orderit.mainapp.activity.TableListActivity;
import orderit.mainapp.model.ExpandableListAdapter;
import orderit.mainapp.model.User;
import orderit.mainapp.utility.JSONParser;

/**
 * Created by LanTuan on 1/25/16.
 */
public class OrderPopupDialog extends DialogFragment {
    public static final String ITEM_NAME = "orderItem";
    public static final String ITEM_COUNT = "orderCnt";

    protected String  itemName = "";
    private int     itemCount = 1;

    private static Context context;
    private static LayoutInflater layoutInflater;

    private TextView    setCount;
    protected ImageView   incImg;
    protected ImageView   decImg;

    protected static ExpandableListAdapter parentAdapter;

    public static OrderPopupDialog newInstance(ExpandableListAdapter expandableListAdapter, Context contextArg, String name, int count) {
        parentAdapter = expandableListAdapter;
        context = contextArg;
        layoutInflater = (LayoutInflater) contextArg.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        OrderPopupDialog frag = new OrderPopupDialog();
        Bundle args = new Bundle();
        args.putString(ITEM_NAME, name);
        args.putInt(ITEM_COUNT, count);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Resources res = getActivity().getResources();
        Bundle bundle = getArguments();

        // Get data
        itemName = bundle.getString(ITEM_NAME);
        itemCount = bundle.getInt(ITEM_COUNT);

        // setting view
        View settingView = layoutInflater.inflate(R.layout.order_popup, null);
        // Get set count
        setCount = (TextView)settingView.findViewById(R.id.orderSetCnt);
        setCount.setText(String.format("%s", itemCount));
        // Get handler of image increment
        incImg = (ImageView)settingView.findViewById(R.id.orderIncBtn);
        incImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                itemCount++;
                setCount.setText(String.format("%s", itemCount));
            }
        });

        // Get handler of image decrement
        decImg = (ImageView)settingView.findViewById(R.id.orderDecBtn);
        decImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                itemCount--;
                setCount.setText(String.format("%s", itemCount));
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);

        dialog.setTitle(itemName);
        dialog.setView(settingView);
        dialog.setPositiveButton("Confirm", new PositiveButtonClickListener());

        return dialog.create();
    }

    class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if(setCount == null) Log.d("AA", "NULL");
            else{
                String value = setCount.getText().toString();
                parentAdapter.onUserChangedDiskCnt(Integer.valueOf(value));
            }
            dialog.dismiss();
        }
    }
}
