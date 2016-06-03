package com.app.crewbid.fragment.postcrewvent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.utility.ImageFilePath;
import com.app.crewbid.utility.Utility;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PostCrewventUploadFragment extends Fragment implements
        OnClickListener, KeyInterface {
    public static final String TAG = PostCrewventUploadFragment.class
            .getSimpleName();

    public static final int PICKLOGO_REQUEST_CODE = 101;
    public static final int PICKATTACHMENT_REQUEST_CODE = 102;

    private Button btnSkip, btnContinue;
    private RelativeLayout relativeLogo, relativeAttachment;
    ImageView imgUpload, imgAttachment;
    String seletedItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root = inflater.inflate(R.layout.frag_post_crewvent_upload,
                container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        ((MainFragmentActivity) getContext()).setHeader(getActivity()
                .getString(R.string.post_a_crewvent));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    private void init(View v) {
        btnSkip = (Button) v.findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(this);
        btnContinue = (Button) v.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        relativeLogo = (RelativeLayout) v.findViewById(R.id.relativeLogo);
        relativeLogo.setOnClickListener(this);

        imgUpload = (ImageView) v.findViewById(R.id.imgUpload);
        imgAttachment = (ImageView) v.findViewById(R.id.imgAttachment);

        relativeAttachment = (RelativeLayout) v
                .findViewById(R.id.relativeAttachment);
        relativeAttachment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnSkip:
                startPostCrewventPayment();
                break;
            case R.id.btnContinue:
                startPostCrewventPayment();
                break;
            case R.id.relativeLogo:
                openFilePicker();
                seletedItems = "logo";
                break;
            case R.id.relativeAttachment:
                openFilePicker();
                seletedItems = "attachment";
                break;
        }
    }

    private void openFilePicker() {
        /*Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        if (image) {
			fileintent.setType("image*//*");
        } else {
			fileintent.setType("**");
		}
		try {
			((MainFragmentActivity) getActivity()).startActivityForResult(
					fileintent, image ? PICKLOGO_REQUEST_CODE
							: PICKATTACHMENT_REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			Utility.log("tag",
					"No activity can handle picking a file. Showing alternatives.");
		}*/
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(i, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getContext(), "=Called", Toast.LENGTH_SHORT).show();

        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                if (seletedItems.equals("logo")) {
                    imgUpload.setImageBitmap(selectedImage);
                    PostCrewventFormFragment.clsAddNewEvent.setLogoPath(imageUri.getPath());
                } else {
                    imgAttachment.setImageBitmap(selectedImage);
                    PostCrewventFormFragment.clsAddNewEvent.setAttachmentPath(imageUri.getPath());
                }

//                Toast.makeText(getContext(), "=" + imageUri, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResultFromActivity(int requestCode, int resultCode,
                                     Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case PICKLOGO_REQUEST_CODE:
            case PICKATTACHMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String selectedImagePath = null;
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImagePath = ImageFilePath.getPath(
                                    getActivity(), selectedImageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        onFileSelected(selectedImagePath, requestCode);
                        Utility.log("Image File Path", "" + selectedImagePath);
                    }
                }
                break;
        }
    }

    private void onFileSelected(String path, int requestCode) {
        if (path == null || Utility.filter(path).length() == 0
                || PostCrewventFormFragment.clsAddNewEvent == null) {
            MainFragmentActivity.toast(getActivity(), "File not found");
            return;
        } else {
            String ext = Utility.getExtension(path);
            if (Utility.filter(ext).length() == 0) {
                MainFragmentActivity
                        .toast(getActivity(), "File format unknown");
                return;
            }

            boolean matched = false;
            if (requestCode == PICKLOGO_REQUEST_CODE) {
                for (int i = 0; i < FILE_TYPE_IMAGE_FILTER.length; i++) {
                    if (ext.equalsIgnoreCase(FILE_TYPE_IMAGE_FILTER[i])) {
                        matched = true;
                        break;
                    }
                }
            } else if (requestCode == PICKATTACHMENT_REQUEST_CODE) {
                matched = true;
                // bypass scanning, type not specified
                for (int i = 0; i < FILE_TYPE_OTHER_FILTER.length; i++) {
                    if (ext.equalsIgnoreCase(FILE_TYPE_OTHER_FILTER[i])) {
                        matched = true;
                        break;
                    }
                }
            }

            if (!matched) {
                MainFragmentActivity.toast(getActivity(),
                        "File type not allowed");
                return;
            }
            Utility.log(TAG, "path?" + path);

            // set data
            if (requestCode == PICKLOGO_REQUEST_CODE) {
                PostCrewventFormFragment.clsAddNewEvent.setLogoPath(path);
            } else if (requestCode == PICKATTACHMENT_REQUEST_CODE) {
                PostCrewventFormFragment.clsAddNewEvent.setAttachmentPath(path);
            }
        }
    }

    private void startPostCrewventPayment() {
        PostCrewventPaymentFragment crewventPaymentFragment = new PostCrewventPaymentFragment();
        ((MainFragmentActivity) getContext()).switchContent(
                crewventPaymentFragment, true, FRAG_POST_CREWVENT_PAYMENT);
    }

}
