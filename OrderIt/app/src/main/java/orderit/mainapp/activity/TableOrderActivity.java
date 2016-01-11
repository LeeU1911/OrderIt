package orderit.mainapp.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import orderit.mainapp.R;

public class TableOrderActivity extends AppCompatActivity {

    private static final int CODE_INPUT_WIDTH = 60;
    private static final int ITEM_NAME_INPUT_WIDTH = 120;
    private static final int CODE_INPUT_COL_SPAN = 2;
    private static final int CODE_INPUT_START_COL = 1;
    private static final int ITEM_NAME_INPUT_COL_SPAN = 3;
    private static final int ITEM_NAME_INPUT_START_COL = 3;
    private static final int QTY_WIDTH = 25;
    private static final String CANCEL_BTN_TEXT = "X";
    private AtomicInteger no = new AtomicInteger(0);

    private Map<Integer, List<View>> itemRows = new HashMap<>();
    private boolean focusRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGui();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReadyToGetFocusAgain();
                addRow(view);
            }
        });

        Button reset = (Button) findViewById(R.id.reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllRows();
                getReadyToGetFocusAgain();
                requestFocusOnFirstEmptyRow();
            }
        });

    }

    private void getReadyToGetFocusAgain() {
        focusRequested = false;
    }

    private void clearAllRows() {
        for (int i = 1; i <= no.get(); i++) {
            List<View> row = itemRows.get(i);
            resetRowDataToDefault(row);
        }
    }

    private void initGui() {
        setContentView(R.layout.activity_table_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addItemOrders();

    }

    private void addItemOrders() {
        GridLayout orderDetails = (GridLayout) findViewById(R.id.orderDetails);
        for (int i = 0; i < 10; i++) {
            addRow(orderDetails);
        }
    }

    private void addRow(View view) {
        no.incrementAndGet();
        GridLayout orderItems = (GridLayout) findViewById(R.id.orderItems);

        itemRows.put(no.get(), new ArrayList<View>());

        addToView(orderItems, createSequenceLabel(view.getContext()));
        addToView(orderItems, createCodeInput());
        addToView(orderItems, createItemNameInput());
        addToView(orderItems, createQuantitySpinner(view.getContext()));
        addToView(orderItems, createCancelButton(view.getContext()));

        requestFocusOnFirstEmptyRow();
    }

    private void requestFocusOnFirstEmptyRow() {
        for(int i = 1; i <= no.get(); i++) {
            List<View> row = itemRows.get(i);
            if (isEmptyRow(row)) {
                for (View v : row) {
                    if (v instanceof AppCompatEditText) {
                        EditText input = (EditText) v;
                        if (isPrimaryInput(input) && !focusRequested) {
                            input.requestFocus();
                            focusRequested = true;
                        }
                    }
                }
            }
        }
    }

    private boolean isEmptyRow(List<View> row) {
        for(View v: row){
            if(v instanceof AppCompatEditText){
                EditText input = (EditText) v;
                if(isPrimaryInput(input) && "".equalsIgnoreCase(String.valueOf(input.getText()))){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPrimaryInput(EditText input){
        return input.getInputType() == InputType.TYPE_CLASS_NUMBER;
    }
    private void addToView(GridLayout orderItems, View view){
        itemRows.get(no.get()).add(view);
        orderItems.addView(view);
    }

    private EditText createCodeInput(){
        EditText code = createTextInput();
        code.setInputType(InputType.TYPE_CLASS_NUMBER);
        code.setWidth(getPixelFromDP(CODE_INPUT_WIDTH));
        code.setLayoutParams(getGridLayoutParams(CODE_INPUT_START_COL, CODE_INPUT_COL_SPAN));
        return code;
    }

    private TextView createItemNameInput(){
        TextView itemName = createTextInput();
        itemName.setWidth(getPixelFromDP(ITEM_NAME_INPUT_WIDTH));
        itemName.setLayoutParams(getGridLayoutParams(ITEM_NAME_INPUT_START_COL, ITEM_NAME_INPUT_COL_SPAN));
        itemName.setSingleLine();
        return itemName;
    }

    private GridLayout.LayoutParams getGridLayoutParams(int startCol, int colSpan){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(startCol, colSpan);
        params.setGravity(Gravity.FILL);
        return params;
    }
    private Button createCancelButton(Context context){
        Button cancel = new Button(context, null, android.R.attr.buttonStyleSmall);
        cancel.setText(CANCEL_BTN_TEXT);
        cancel.setId(no.get());
        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRowData(view);
            }
        }));
        return cancel;
    }

    private void clearRowData(View view) {
        List<View> row = itemRows.get(view.getId());
        resetRowDataToDefault(row);
    }

    private void resetRowDataToDefault(List<View> row) {
        for (View v : row) {
            if (v instanceof AppCompatEditText) {
                EditText input = (EditText) v;
                input.setText("");
            }
            if (v instanceof Spinner) {
                Spinner qty = (Spinner) v;
                qty.setSelection(0);
            }
        }
    }

    private Spinner createQuantitySpinner(Context context) {
        Spinner quantity = new Spinner(context);
        quantity.setAdapter(prepareQtyAdapter());
        quantity.setDropDownWidth(getPixelFromDP(QTY_WIDTH));
        return quantity;
    }

    private ArrayAdapter<CharSequence> prepareQtyAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.qty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int getPixelFromDP(int dps) {
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
        lbl.setText(no.get() + "");
        return lbl;
    }

    private EditText createTextInput() {
        EditText input = (EditText) getLayoutInflater().inflate(R.layout.edit_text, null);
        input.setId(no.get());
        return input;
    }



}
