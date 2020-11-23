package com.example.dku_lf.ui.models;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener listener;

    private GestureDetector gestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);  // 클릭했을때 postItem의 view와 position 가져오기
        void onItemLongClick(View view, int position);  // 길게 클릭했을 경우
    }

    //클래스 생성자. 리사이클뷰를 연결해서 제스쳐 이벤트를 추가해줌.
    public RecyclerViewItemClickListener(Context context, final RecyclerView recyclerView, final OnItemClickListener listener) {
        this.listener = listener; //callback을 사용해야해서 당연하게 들어가 있음
        //제스쳐 디렉트. SimpleOnGestureListener에 여러가지 제스쳐에 대한 메서드들이 잇음
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            //한번 탭했을 때
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            //길게 눌렀을 때
            @Override
            public void onLongPress(MotionEvent e) {
                View v = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(v != null && listener != null) {
                    listener.onItemLongClick(v, recyclerView.getChildAdapterPosition(v));
                }
            }
        });
    }

    //터치 이벤트를 인터셉트 해주는 메서드
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        // 리사이클러 뷰의 이벤트의 getX,Y를 받기위해 뷰를 생성
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        //null 체크는 안해주면 오류. 손가락의 움직임의 모션 이벤트를 받는다. onMove, onUp 등등
        if(view != null && gestureDetector.onTouchEvent(e)){
            //onItemclick listener에 getX, Y의 뷰를 Child뷰(안에있는 뷰. 뷰홀더)를 받고 리사이클러뷰(rv) 어뎁터에서 가져온 자식의 포지션을 넣어준다.
            listener.onItemClick(view, rv.getChildAdapterPosition(view));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


}
