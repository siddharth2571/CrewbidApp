package com.app.crewbid.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.crewbid.R;
import com.app.crewbid.adapter.CustomSpinnerAdapter;
import com.app.crewbid.model.ClsAddNewEvent.ClsCategory;
import com.app.crewbid.model.ClsAddNewEvent.ClsCity;
import com.app.crewbid.model.ClsKeyValue;
import com.app.crewbid.utility.Utility;

import java.util.ArrayList;

public class CustomSpinner extends RelativeLayout {

    private static final String TAG = CustomSpinner.class.getSimpleName();

    private Adapter adapter;
    private OnListItemClick onListItemClick;
    private int currentPos = -1;
    private ArrayList<ClsKeyValue> list = new ArrayList<ClsKeyValue>(1);
    private PopupWindow popupWindow;

    public CustomSpinner(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("NewApi")
    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setSingleLine();
        textView.setText("");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources()
                .getDimensionPixelSize(R.dimen.textsize_edittext));
        textView.setPadding(0, 0, 46, 0);
        addView(textView);

        RelativeLayout.LayoutParams layoutParamsArrow = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsArrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParamsArrow.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParamsArrow);
        imageView.setImageResource(R.drawable.ic_arrow);
        addView(imageView);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    private TextView getTextView() {
        if (getChildAt(0) != null) {
            return (TextView) getChildAt(0);
        }
        return null;
    }

    public void setText(String text) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setText(text);
        }
    }

    public void setHint(String hint) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setText(hint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static interface OnListItemClick {
        public void onListItemClick(Object pos);
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @SuppressWarnings("unchecked")
    public void setData(ArrayList<?> list) {
        if (list == null || list.size() == 0)
            return;

        Object object = list.get(0);
        if (object instanceof ClsKeyValue) {
            this.list = (ArrayList<ClsKeyValue>) list;
        } else if (object instanceof ClsCity) {
            ArrayList<ClsKeyValue> listTemp = new ArrayList<ClsKeyValue>();
            for (Object o : list) {
                listTemp.add(new ClsKeyValue(((ClsCity) o).getId(),
                        ((ClsCity) o).getName()));
            }
            this.list = listTemp;
        } else if (object instanceof ClsCategory) {
            ArrayList<ClsKeyValue> listTemp = new ArrayList<ClsKeyValue>();
            for (Object o : list) {
                listTemp.add(new ClsKeyValue(((ClsCategory) o).getId(),
                        ((ClsCategory) o).getName()));
            }
            this.list = listTemp;
        }
    }

    public void setSelectedPos(int pos) {
        if (list == null || pos == -1)
            return;

        TextView tv = getTextView();
        if (list.size() > pos && tv != null) {
            tv.setText(list.get(pos).getValue().toString());
        }
    }

    public void setSelectedPos(String pos) {
        if (list == null)
            return;

        TextView tv = getTextView();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(pos)) {
                tv.setText(list.get(i).getValue().toString());
            }
        }
    }

    @SuppressLint("InflateParams")
    private void showPopup() {
        if (list == null || (popupWindow != null && popupWindow.isShowing())) {
            Utility.log(TAG, "Exception on list");
            return;
        }

        View spinnerView = ((LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.view_spinner, null);
        ListView listView = (ListView) spinnerView
                .findViewById(R.id.listSpinner);

        int height = getContext().getResources().getDimensionPixelSize(
                R.dimen.spinner_text_min_height);
        if (list.size() == 0) {
            height = 20;
        } else if (list.size() > 4) {
            height = height * 4;
        } else {
            height = height * list.size();
        }

        popupWindow = new PopupWindow(spinnerView);
        popupWindow.setClippingEnabled(true);
        // popupWindow.setWindowLayoutMode(0,
        // ViewGroup.LayoutParams.WRAP_CONTENT);
        // right edit text
        popupWindow.setWindowLayoutMode(0, 0);
        popupWindow.setWidth(this.getWidth());
        popupWindow.setHeight(height);

        final CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(
                getContext(), list);
        listView.setAdapter(spinnerAdapter);
        listView.setSelection(currentPos);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                currentPos = pos;

                if (onListItemClick != null) {
                    onListItemClick.onListItemClick(pos);
                }

                String item = (String) spinnerAdapter.getList().get(pos)
                        .getValue();
                TextView tv = getTextView();
                if (tv != null) {
                    tv.setText(item);
                }
                if (onListItemClick != null) {
                    onListItemClick.onListItemClick(pos);
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentView(spinnerView);
        popupWindow.setBackgroundDrawable(getContext().getResources()
                .getDrawable(R.drawable.bg_spinner_popup));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(this, 0, 0);
    }

    public OnListItemClick getOnListItemClick() {
        return onListItemClick;
    }

    public void setOnListItemClick(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }
}
