// Generated by view binder compiler. Do not edit!
package com.example.dku_lf.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.example.dku_lf.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSettingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button leaveButton;

  @NonNull
  public final Button logoutButton;

  private FragmentSettingBinding(@NonNull ConstraintLayout rootView, @NonNull Button leaveButton,
      @NonNull Button logoutButton) {
    this.rootView = rootView;
    this.leaveButton = leaveButton;
    this.logoutButton = logoutButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSettingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSettingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_setting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSettingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.leave_button;
      Button leaveButton = rootView.findViewById(id);
      if (leaveButton == null) {
        break missingId;
      }

      id = R.id.logout_button;
      Button logoutButton = rootView.findViewById(id);
      if (logoutButton == null) {
        break missingId;
      }

      return new FragmentSettingBinding((ConstraintLayout) rootView, leaveButton, logoutButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}