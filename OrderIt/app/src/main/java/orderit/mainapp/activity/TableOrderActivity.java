package orderit.mainapp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import orderit.mainapp.R;

public class TableOrderActivity extends AppCompatActivity {

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
        //TODO: prepare screen
        setContentView(R.layout.activity_table_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO: add 10 rows for order
        for(int i = 0; i < 10; i++){
            addRow(findViewById(R.id.tableOrder));
        }

    }

    private void addRow(View view){
        //add new order row
        TableLayout ll = (TableLayout) findViewById(R.id.tblLayout);
        TableRow row = new TableRow(view.getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);
        TextView no = createSequenceLabel(view.getContext());
        row.addView(no);
        EditText code = createTextInput(view.getContext());
        code.setInputType(InputType.TYPE_CLASS_NUMBER);
        code.setMaxLines(ONE_LINE);
        row.addView(createSpace(view.getContext()));
        row.addView(code);
        TextView item = createTextInput(view.getContext());
        item.setMaxLines(ONE_LINE);
        row.addView(createSpace(view.getContext()));
        row.addView(item);
        TextView quantity = createTextInput(view.getContext());
        quantity.setMaxLines(ONE_LINE);
        row.addView(createSpace(view.getContext()));
        row.addView(quantity);
        ImageButton cancel = createCancelImgBtn(row);
        row.addView(cancel);
        ll.addView(row);
    }

    private TextView createLabel(Context context){
        return new TextView(context);
    }
    private TextView createSequenceLabel(Context context){
        TextView lbl = createLabel(context);
        lbl.setText(no.getAndIncrement() + "");
        return lbl;
    }

    private EditText createTextInput(Context context){
        return new EditText(context);
    }

    private Space createSpace(Context context){
        return new Space(context);
    }
    private ImageButton createCancelImgBtn(View row) {
        ImageButton cancelBtn = prepareImgBtn(this.cancelBtnImg);
        TableRow.LayoutParams params = (TableRow.LayoutParams) row.getLayoutParams();
        params.width = TableRow.LayoutParams.MATCH_PARENT;
        params.height = TableRow.LayoutParams.MATCH_PARENT;

        cancelBtn.setLayoutParams(params);
        return cancelBtn;
    }


    private ImageButton prepareImgBtn(int imageId) {
        ImageButton imgBtn = new ImageButton(getApplicationContext());
        imgBtn.setBackgroundResource(imageId);
        return imgBtn;
    }

}
