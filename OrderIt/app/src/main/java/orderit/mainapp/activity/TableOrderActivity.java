package orderit.mainapp.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import orderit.mainapp.R;

public class TableOrderActivity extends AppCompatActivity {

    private static final int ITEMS_LAYOUT = 999;
    private int cancelBtnImg = R.drawable.close;
    private AtomicInteger no = new AtomicInteger(1);
    private static final int ONE_LINE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGui();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow(view);
            }
        });

        /*cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row

            }
        });*/
    }

    private void initGui() {
        setContentView(R.layout.activity_table_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addItemOrders();

    }

    private void addItemOrders() {
        GridLayout orderDetails = (GridLayout) findViewById(R.id.orderDetails);
        for (int i = 0; i < 10; i++) {
            addRow(orderDetails);
        }
    }

    private void addRow(View view) {
        //add new order row
        GridLayout orderItems = (GridLayout) findViewById(R.id.orderItems);
        TextView no = createSequenceLabel(view.getContext());
        orderItems.addView(no);
        EditText code = createTextInput(view.getContext());
        code.setInputType(InputType.TYPE_CLASS_NUMBER);
        code.setWidth(getPixelFromDP(60));
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(1, 2);
        params.setGravity(Gravity.FILL);
        code.setLayoutParams(params);
        orderItems.addView(code);
        TextView itemName = createTextInput(view.getContext());
        itemName.setWidth(getPixelFromDP(120));
        GridLayout.LayoutParams params1 = new GridLayout.LayoutParams();
        params1.columnSpec = GridLayout.spec(3, 3);
        params1.setGravity(Gravity.FILL);
        itemName.setLayoutParams(params1);
        orderItems.addView(itemName);
        Spinner quantity = getQuantitySpinner(view.getContext());
        quantity.setDropDownWidth(getPixelFromDP(25));
        orderItems.addView(quantity);
        Button cancel = getCancelButton(view.getContext());
        orderItems.addView(cancel);
    }

    private Button getCancelButton(Context context){
        Button cancel = new Button(context, null, android.R.attr.buttonStyleSmall);
        cancel.setText("X");
        return cancel;
    }
    private Spinner getQuantitySpinner(Context context) {
        Spinner quantity = new Spinner(context);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.qty_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        quantity.setAdapter(adapter);
        return quantity;
    }

    private int getPixelFromDP(int dps) {
//        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
//        int pixels = (int) (dps * scale + 0.5f);
        int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, getResources().getDisplayMetrics());
        return pixels;
    }

    private TextView createLabel(Context context) {
        TextView label = new TextView(context);
        if (Build.VERSION.SDK_INT < 23) {
            label.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        } else {
            label.setTextAppearance(android.R.style.TextAppearance_Medium);
        }
        return label;
    }

    private TextView createSequenceLabel(Context context) {
        TextView lbl = createLabel(context);
        lbl.setText(no.getAndIncrement() + "");
        return lbl;
    }

    private EditText createTextInput(Context context) {
        EditText input = (EditText) getLayoutInflater().inflate(R.layout.edit_text, null);
        return input;
    }

    private Space createSpace(Context context) {
        return new Space(context);
    }

    private ImageButton createCancelImgBtn(View row) {
        ImageButton cancelBtn = prepareImgBtn(this.cancelBtnImg);
        return cancelBtn;
    }


    private ImageButton prepareImgBtn(int imageId) {
        ImageButton imgBtn = new ImageButton(getApplicationContext());
        imgBtn.setBackgroundResource(imageId);
        return imgBtn;
    }

}
