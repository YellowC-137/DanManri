// Generated by view binder compiler. Do not edit!
package com.example.dku_lf.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.dku_lf.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFoundWritingBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button addMapBtnFound;

  @NonNull
  public final Button addPhotoBtnFound;

  @NonNull
  public final EditText contentTextEditFound;

  @NonNull
  public final ProgressBar foundWritingProg;

  @NonNull
  public final Button submitBtnFound;

  @NonNull
  public final EditText titleEditFound;

  @NonNull
  public final ImageView userUploadImageFound;

  private ActivityFoundWritingBinding(@NonNull RelativeLayout rootView,
      @NonNull Button addMapBtnFound, @NonNull Button addPhotoBtnFound,
      @NonNull EditText contentTextEditFound, @NonNull ProgressBar foundWritingProg,
      @NonNull Button submitBtnFound, @NonNull EditText titleEditFound,
      @NonNull ImageView userUploadImageFound) {
    this.rootView = rootView;
    this.addMapBtnFound = addMapBtnFound;
    this.addPhotoBtnFound = addPhotoBtnFound;
    this.contentTextEditFound = contentTextEditFound;
    this.foundWritingProg = foundWritingProg;
    this.submitBtnFound = submitBtnFound;
    this.titleEditFound = titleEditFound;
    this.userUploadImageFound = userUploadImageFound;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFoundWritingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFoundWritingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_found_writing, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFoundWritingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addMapBtn_found;
      Button addMapBtnFound = rootView.findViewById(id);
      if (addMapBtnFound == null) {
        break missingId;
      }

      id = R.id.addPhotoBtn_found;
      Button addPhotoBtnFound = rootView.findViewById(id);
      if (addPhotoBtnFound == null) {
        break missingId;
      }

      id = R.id.contentText_edit_found;
      EditText contentTextEditFound = rootView.findViewById(id);
      if (contentTextEditFound == null) {
        break missingId;
      }

      id = R.id.found_writing_Prog;
      ProgressBar foundWritingProg = rootView.findViewById(id);
      if (foundWritingProg == null) {
        break missingId;
      }

      id = R.id.submitBtn_found;
      Button submitBtnFound = rootView.findViewById(id);
      if (submitBtnFound == null) {
        break missingId;
      }

      id = R.id.title_edit_found;
      EditText titleEditFound = rootView.findViewById(id);
      if (titleEditFound == null) {
        break missingId;
      }

      id = R.id.user_upload_image_found;
      ImageView userUploadImageFound = rootView.findViewById(id);
      if (userUploadImageFound == null) {
        break missingId;
      }

      return new ActivityFoundWritingBinding((RelativeLayout) rootView, addMapBtnFound,
          addPhotoBtnFound, contentTextEditFound, foundWritingProg, submitBtnFound, titleEditFound,
          userUploadImageFound);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
