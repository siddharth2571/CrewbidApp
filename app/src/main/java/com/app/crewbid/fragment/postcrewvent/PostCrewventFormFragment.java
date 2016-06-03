package com.app.crewbid.fragment.postcrewvent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.jsonparser.ParserAddNewEvent;
import com.app.crewbid.model.ClsAddNewEvent;
import com.app.crewbid.model.ClsKeyValue;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.picker.DatePickerFragment;
import com.app.crewbid.picker.DatePickerFragment.DateChooserInterface;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.ui.CustomSpinner;
import com.app.crewbid.ui.CustomSpinner.OnListItemClick;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;

public class PostCrewventFormFragment extends Fragment implements
        OnClickListener, KeyInterface {

    private static final int REQ_ADD_NEW_EVENT = 1;

    private Button btnContinue;
    private CustomSpinner spinnerCategory, spinnerCrew, spinnerDate,
            spinnerCity;
    private EditText edtEventName, edtNumberAttending, edtBudget;


    private static ArrayList<ClsKeyValue> listCrew = new ArrayList<ClsKeyValue>();
    public static ClsAddNewEvent clsAddNewEvent;

    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        listCrew.clear();
        listCrew.add(new ClsKeyValue("0", "Crew"));
        callAddNewEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root = inflater.inflate(R.layout.frag_post_crewvent_form,
                container, false);
        return root;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        ((MainFragmentActivity) getContext()).setHeader(getActivity()
                .getString(R.string.post_a_crewvent));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        btnContinue = (Button) getView().findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(this);

        spinnerCategory = (CustomSpinner) getView().findViewById(
                R.id.spinnerCategory);
        spinnerCategory.setHint("Select Category");
        spinnerCategory.setOnListItemClick(new OnListItemClick() {
            @Override
            public void onListItemClick(Object pos) {
                if (clsAddNewEvent != null) {
                    clsAddNewEvent.setPosCategory(clsAddNewEvent.getProjectCategoryList().get((Integer) pos).getName());
//                    Toast.makeText(getContext(), "=>" + clsAddNewEvent.getPosCategory(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinnerCrew = (CustomSpinner) getView().findViewById(R.id.spinnerCrew);

        spinnerCrew.setHint("Crew");

        spinnerCrew.setOnListItemClick(new OnListItemClick() {
            @Override
            public void onListItemClick(Object pos) {
                if (clsAddNewEvent != null) {
                    clsAddNewEvent.setPosCrew((Integer) pos);
                }
            }
        });

        spinnerCity = (CustomSpinner) getView().findViewById(R.id.spinnerCity);
        spinnerCity.setHint("Select City");
        spinnerCity.setOnListItemClick(new OnListItemClick() {
            @Override
            public void onListItemClick(Object pos) {
                if (clsAddNewEvent != null) {
                    clsAddNewEvent.setPosCity((Integer) pos);
                }
            }
        });

        spinnerDate = (CustomSpinner) getView().findViewById(R.id.spinnerDate);
        spinnerDate.setHint("Select Date");
        spinnerDate.setOnClickListener(this);
        edtEventName = (EditText) getView().findViewById(R.id.edtEventName);
        edtEventName.addTextChangedListener(new MyEditTextWatcher(
                R.id.edtEventName));
        edtNumberAttending = (EditText) getView().findViewById(
                R.id.edtNumberAttending);
        edtNumberAttending.addTextChangedListener(new MyEditTextWatcher(
                R.id.edtNumberAttending));
        edtBudget = (EditText) getView().findViewById(R.id.edtBudget);
        edtBudget.addTextChangedListener(new MyEditTextWatcher(R.id.edtBudget));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        setData();
    }

    private void setData() {
        if (clsAddNewEvent == null)
            return;
        spinnerCrew.setData(listCrew);

        clsAddNewEvent.setPosCrew(0);
        // Toast.makeText(getContext(), "=>" + clsAddNewEvent.getPosCategory(), Toast.LENGTH_SHORT).show();
        spinnerCity.setData(clsAddNewEvent.getProjectCityList());
        spinnerCategory.setData(clsAddNewEvent.getProjectCategoryList());

        setDate();
        setCrew();
        setCity();
        setCategory();

    }

    private void setDate() {
        if (clsAddNewEvent == null || clsAddNewEvent.getmCalendar() == null)
            return;

        spinnerDate.setText(Utility.convertMillisToDate(clsAddNewEvent
                .getmCalendar().getTimeInMillis(), KeyInterface.DD_MM_YYYY));
    }

    private void setCrew() {
        if (clsAddNewEvent == null)
            return;
        spinnerCrew.setSelectedPos(clsAddNewEvent.getPosCrew());
    }

    private void setCity() {
        if (clsAddNewEvent == null
                || clsAddNewEvent.getProjectCityList() == null)
            return;
        spinnerCity.setSelectedPos(clsAddNewEvent.getPosCity());
    }

    private void setCategory() {
        if (clsAddNewEvent == null
                || clsAddNewEvent.getProjectCategoryList() == null)
            return;
        spinnerCategory.setSelectedPos(clsAddNewEvent.getPosCategory());

    }

    private class MyEditTextWatcher implements TextWatcher {
        private int id;

        public MyEditTextWatcher(int idRes) {
            // TODO Auto-generated constructor stub
            id = idRes;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            if (clsAddNewEvent == null)
                return;

            if (id == R.id.edtEventName) {
                clsAddNewEvent.setEventName(s.toString());
            } else if (id == R.id.edtNumberAttending) {
                clsAddNewEvent.setNumberAttending(s.toString());
            } else if (id == R.id.edtBudget) {
                clsAddNewEvent.setBudget(s.toString());
                clsAddNewEvent.setFilterBudget(s.toString());
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnContinue: {
                if (!checkValidation()) {
                    return;
                }
                PostCrewventUploadFragment crewventUploadFragment = new PostCrewventUploadFragment();
                ((MainFragmentActivity) getContext()).switchContent(
                        crewventUploadFragment, true, FRAG_POST_CREWVENT_UPLOAD);
                break;
            }

            case R.id.spinnerDate: {
                openDatePicker();
                break;
            }
        }
    }

    private boolean checkValidation() {
        if (clsAddNewEvent == null)
            return false;

        if (clsAddNewEvent.getPosCrew() == -1) {
            MainFragmentActivity.toast(getActivity(), "Please select crew");
            return false;
        } else if (Utility.filter(clsAddNewEvent.getEventName()).length() == 0) {
            MainFragmentActivity
                    .toast(getActivity(), "Please enter event name");
            return false;
        } else if (clsAddNewEvent.getmCalendar() == null) {
            MainFragmentActivity.toast(getActivity(), "Please select date");
            return false;
        } else if (Utility.filter(clsAddNewEvent.getNumberAttending()).length() == 0) {
            MainFragmentActivity.toast(getActivity(),
                    "Please enter number attending");
            return false;
        } else if (Utility.filter(clsAddNewEvent.getBudget()).length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter budget");
            return false;
        } else if (clsAddNewEvent.getPosCity() == -1) {
            MainFragmentActivity.toast(getActivity(), "Please select city");
            return false;
        } else if (TextUtils.isEmpty(clsAddNewEvent.getPosCategory())) {
            MainFragmentActivity.toast(getActivity(), "Please select category");
            return false;
        }

        return true;
    }

    private void openDatePicker() {
        DatePickerFragment newFragment = new DatePickerFragment();
        // newFragment.setFromDate(clsBandHelper.getFromDate());
        // newFragment.setCompareDate(todayDate);
        // newFragment.setResourceId(v.getId());
        newFragment.setDateChooserInterface(new DateChooserInterface() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day, int resourceId, boolean fromClick) {
                // TODO Auto-generated method stub
                if (fromClick) {
                    if (spinnerDate != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);

                        Log.i("date", "=" + calendar);
                        Log.i("date", "=" + Calendar.getInstance());

                        if (null != calendar) {
//                            if (calendar.after(Calendar.getInstance()) || calendar == Calendar.getInstance())
                                clsAddNewEvent.setmCalendar(calendar);
                        }
                        setDate();
                    }
                }
            }

            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day, int resourceId) {
                // TODO Auto-generated method stub
            }
        });
        newFragment.show(((MainFragmentActivity) getActivity())
                .getSupportFragmentManager(), "datePicker");
    }

    private void callAddNewEvent() {
        String uId = AppDelegate.getString(PreferenceData.USERID);
        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setmCallback(asyncTaskCompleteListener);
        networkTask.setHashMapParams(new NetworkParam().getAddNewParam(uId));
        networkTask.execute(NetworkParam.METHOD_POST_CREW_ADD_NEW_EVENT,
                String.valueOf(REQ_ADD_NEW_EVENT));
        ProgressDialogUtility.show(getActivity(), null, false);
    }

    private AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_ADD_NEW_EVENT) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    // MainFragmentActivity.toast(getActivity(),
                    // clsResponse.getDispMessage());

                    ClsAddNewEvent addNewEvent = (ClsAddNewEvent) clsResponse
                            .getObject();
                    clsAddNewEvent = addNewEvent;
                    setData();
                } else {
                    MainFragmentActivity.toast(getActivity(),
                            clsResponse.getDispMessage());
                }
            }
        }

        @Override
        public ClsNetworkResponse doBackGround(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (!clsResponse.isSuccess()
                    || clsResponse.getResult_String() == null)
                return clsResponse;
            if (clsResponse.getRequestCode() == REQ_ADD_NEW_EVENT) {
                clsResponse = new ParserAddNewEvent(clsResponse).parse();
            }
            return clsResponse;
        }
    };
}
