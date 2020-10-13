package com.flexapp.flex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.flexapp.flex.databinding.FragmentAddMealBinding;
import com.flexapp.flex.firebasemodels.Meal;
import com.flexapp.flex.interfaces.IAddMealFragment;
import com.flexapp.flex.repos.MainRepository;
import com.flexapp.flex.utils.Constants;
import com.flexapp.flex.utils.Utils;
import com.flexapp.flex.viewmodels.AddMealViewModel;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMealFragment extends Fragment implements IAddMealFragment {
    private int CODE_PICTURE_REQUEST = 8;
    public String TAG = getClass().getName();
    private String currentPhotoPath;
    public FragmentAddMealBinding mBinding;
    private MainRepository repo;
    public AddMealViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView...");
        this.repo = MainRepository.getInstance();
        this.viewModel = new AddMealViewModel();
        this.mBinding = FragmentAddMealBinding.inflate(inflater);

        getMealIfExists();

        this.mBinding.setData(viewModel);
        this.mBinding.setParent(this);
        return this.mBinding.getRoot();
    }

    private void getMealIfExists() {
        Log.v(this.TAG, "getMealIfExists...");
        if (getArguments() != null) {
            String mealId = getArguments().getString("mealId");
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle("Edit Meal");
            }
            if (mealId != null) {
                Log.d(this.TAG, mealId);
                getMeal(mealId);
                return;
            }
            Log.d(this.TAG, "mealId=null");
            return;
        }
        Log.d(this.TAG, "Args=null");
    }

    private void getMeal(final String mealId) {
        Log.v(this.TAG, "getMeal...");
        getContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "onReceive...");
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    Meal meal = (Meal) intent.getSerializableExtra(Meal.class.getName());
                    if (meal == null) {
                        Log.d(TAG, "meal=null");
                    } else {
                        Log.d(TAG, meal.toString());
                        viewModel.setId(mealId);
                        viewModel.setMeasuringSystem(meal.getMeasuringSystem());
                        viewModel.setTitle(meal.getTitle());
                        viewModel.setDescription(meal.getDescription());
                        viewModel.setCalories(meal.getCalories());
                        viewModel.setFats(meal.getFats());
                        viewModel.setProtein(meal.getProteins());
                        viewModel.setSugar(meal.getSugar());
                        viewModel.setImageRef(meal.getImageRef());
                    }
                } else {
                    Log.d(TAG, "unsuccessful");
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(Constants.INTENT_GET_MEAL_ACTION));
        this.repo.getMeal(Utils.getTodayDate(), mealId, getContext());
    }

    public void onImageClick() {
        Log.v(this.TAG, "onImageClick...");
        takeNewImage();
    }

    public void onScanBarcodeClick() {
        Log.v(this.TAG, "onScanBarcodeClick...");
    }

    public void onSaveClick() {
        Log.v(this.TAG, "onSaveClick...");
        hideKeyBoard();
        if (!this.viewModel.getTitle().isEmpty()) {
            this.mBinding.progressBar.setVisibility(View.VISIBLE);
            getContext().registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    mBinding.progressBar.setVisibility(View.GONE);
                    if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                        toast("Saved");
                        goBack();
                    } else {
                        toast("Error while trying to save");
                    }
                    getContext().unregisterReceiver(this);
                }
            }, new IntentFilter(Constants.INTENT_SAVE_MEAL_ACTION));
            this.repo.addTodayMeal(new Meal(this.viewModel.getId(), this.viewModel.getTitle(), this.viewModel.getDescription(), this.viewModel.getImageRef(), this.viewModel.getCalories(), this.viewModel.getFats(), this.viewModel.getProtein(), this.viewModel.getSugar()), Utils.getTodayDate(), getContext());
            return;
        }
        toast("Cannot save meal without title");
    }

    /* access modifiers changed from: private */
    public void toast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public void goBack() {
        getActivity().onBackPressed();
    }

    private void hideKeyBoard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(this.TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void takeNewImage() {
        Log.v(TAG, "takeNewImage...");
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            if (photoFile != null) {
                takePictureIntent.putExtra( "output", FileProvider.getUriForFile(getContext(), getActivity().getPackageName(), photoFile));
                startActivityForResult(takePictureIntent, CODE_PICTURE_REQUEST);
                return;
            }
            return;
        }
        toast("Cannot detect camera hardware");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str = this.TAG;
        Log.v(str, "onActivityResult..., " + requestCode + ", " + resultCode);
        if (requestCode == this.CODE_PICTURE_REQUEST && resultCode == -1) {
            String str2 = this.currentPhotoPath;
            if (str2 != null && !str2.isEmpty() && new File(this.currentPhotoPath).exists()) {
                this.viewModel.setImageRef(this.currentPhotoPath);
            }
            galleryAddPic();
        }
    }

    private File createImageFile() throws IOException {
        Log.v(this.TAG, "createImageFile...");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = File.createTempFile("FLEX_APP_" + timeStamp + "_", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Log.v(this.TAG, "galleryAddPic...");
        try {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            mediaScanIntent.setData(Uri.fromFile(new File(this.currentPhotoPath)));
            getActivity().sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            Log.e(this.TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
