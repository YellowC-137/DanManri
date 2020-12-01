// Generated by view binder compiler. Do not edit!
package com.example.dku_lf.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.dku_lf.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLostPostBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button lostChatBtn;

  @NonNull
  public final RelativeLayout mapLayout;

  @NonNull
  public final TextView noMarker;

  @NonNull
  public final TextView postContentsLost;

  @NonNull
  public final TextView postNameLost;

  @NonNull
  public final TextView postTitleLost;

  @NonNull
  public final TextView time;

  @NonNull
  public final ImageView uploadedImageLost;

  private ActivityLostPostBinding(@NonNull LinearLayout rootView, @NonNull Button lostChatBtn,
      @NonNull RelativeLayout mapLayout, @NonNull TextView noMarker,
      @NonNull TextView postContentsLost, @NonNull TextView postNameLost,
      @NonNull TextView postTitleLost, @NonNull TextView time,
      @NonNull ImageView uploadedImageLost) {
    this.rootView = rootView;
    this.lostChatBtn = lostChatBtn;
    this.mapLayout = mapLayout;
    this.noMarker = noMarker;
    this.postContentsLost = postContentsLost;
    this.postNameLost = postNameLost;
    this.postTitleLost = postTitleLost;
    this.time = time;
    this.uploadedImageLost = uploadedImageLost;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLostPostBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLostPostBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_lost_post, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLostPostBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.lost_chat_btn;
      Button lostChatBtn = rootView.findViewById(id);
      if (lostChatBtn == null) {
        break missingId;
      }

      id = R.id.map_layout;
      RelativeLayout mapLayout = rootView.findViewById(id);
      if (mapLayout == null) {
        break missingId;
      }

      id = R.id.no_marker;
      TextView noMarker = rootView.findViewById(id);
      if (noMarker == null) {
        break missingId;
      }

      id = R.id.post_contents_lost;
      TextView postContentsLost = rootView.findViewById(id);
      if (postContentsLost == null) {
        break missingId;
      }

      id = R.id.post_name_lost;
      TextView postNameLost = rootView.findViewById(id);
      if (postNameLost == null) {
        break missingId;
      }

      id = R.id.post_title_lost;
      TextView postTitleLost = rootView.findViewById(id);
      if (postTitleLost == null) {
        break missingId;
      }

      id = R.id.time;
      TextView time = rootView.findViewById(id);
      if (time == null) {
        break missingId;
      }

      id = R.id.uploaded_image_lost;
      ImageView uploadedImageLost = rootView.findViewById(id);
      if (uploadedImageLost == null) {
        break missingId;
      }

      return new ActivityLostPostBinding((LinearLayout) rootView, lostChatBtn, mapLayout, noMarker,
          postContentsLost, postNameLost, postTitleLost, time, uploadedImageLost);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}