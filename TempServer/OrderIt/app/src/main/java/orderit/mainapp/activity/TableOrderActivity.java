package orderit.mainapp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        for(int i = 0; i < 10; i++){
            addRow(findViewById(R.id.tableOrder));
        }

    }

    private void addRow(View view){
        //add new order row
        GridLayout orderItems = (GridLayout) findViewById(R.id.orderItems);
        TextView no = createSequenceLabel(view.getContext());
        orderItems.addView(no);
        EditText code = createTextInput(view.getContext());
        code.setInputType(InputType.TYPE_CLASS_NUMBER);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(1, 2);
        params.setGravity(Gravity.FILL);
        code.setLayoutParams(params);
        orderItems.addView(code);
        TextView itemName = createTextInput(view.getContext());
        GridLayout.LayoutParams params1 = new GridLayout.LayoutParams();
        params1.columnSpec = GridLayout.spec(3, 3);
        params1.setGravity(Gravity.FILL);
        itemName.setLayoutParams(params1);
        orderItems.addView(itemName);
        TextView quantity = createTextInput(view.getContext());
        orderItems.addView(quantity);
        orderItems.addView(createTextInput(view.getContext()));
    }

    private TextView createLabel(Context context){
        TextView label =  new TextView(context);
        if (Build.VERSION.SDK_INT < 23) {
            label.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        }else{
            label.setTextAppearance(android.R.style.TextAppearance_Medium);
        }
        return label;
    }
    private TextView createSequenceLabel(Context context){
        TextView lbl = createLabel(context);
        lbl.setText(no.getAndIncrement() + "");
        return lbl;
    }

    private EditText createTextInput(Context context){
        EditText input = new EditText(context);
        input.setLines(ONE_LINE);
        return input;
    }

    private Space createSpace(Context context){
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
