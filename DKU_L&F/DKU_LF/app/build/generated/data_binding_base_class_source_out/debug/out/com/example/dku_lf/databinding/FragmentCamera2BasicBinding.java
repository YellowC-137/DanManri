// Generated by view binder compiler. Do not edit!
package com.example.dku_lf.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.dku_lf.AutoFitTextureView;
import com.example.dku_lf.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentCamera2BasicBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final LinearLayout control;

  @NonNull
  public final Button picture;

  @NonNull
  public final AutoFitTextureView texture;

  private FragmentCamera2BasicBinding(@NonNull RelativeLayout rootView,
      @NonNull LinearLayout control, @NonNull Button picture, @NonNull AutoFitTextureView texture) {
    this.rootView = rootView;
    this.control = control;
    this.picture = picture;
    this.texture = texture;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentCamera2BasicBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentCamera2BasicBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_camera2_basic, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentCamera2BasicBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.control;
      LinearLayout control = rootView.findViewById(id);
      if (control == null) {
        break missingId;
      }

      id = R.id.picture;
      Button picture = rootView.findViewById(id);
      if (picture == null) {
        break missingId;
      }

      id = R.id.texture;
      AutoFitTextureView texture = rootView.findViewById(id);
      if (texture == null) {
        break missingId;
      }

      return new FragmentCamera2BasicBinding((RelativeLayout) rootView, control, picture, texture);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
