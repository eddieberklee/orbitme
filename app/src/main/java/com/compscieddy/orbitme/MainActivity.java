package com.compscieddy.orbitme;

import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.compscieddy.eddie_utils.Etils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

  @Bind(R.id.orbit_container) ViewGroup mOrbitContainer;
  @Bind(R.id.orbit_circle) View mOrbitCircle;

  float mOrbitCircleX, mOrbitCircleY;

  float startX = -1, startY = -1;
  private RectF mCircleRect = null;
  private View mRootView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = getLayoutInflater();
    mRootView = inflater.inflate(R.layout.activity_main, null);
    setContentView(mRootView);
    ButterKnife.bind(this);

    mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        float x = mOrbitCircle.getX();
        float y = mOrbitCircle.getY();
        if (x != -1f && y != -1f) {
          mOrbitCircleX = x;
          mOrbitCircleY = y;
          float circleSize = getResources().getDimensionPixelSize(R.dimen.inner_orbit_size);
          mCircleRect = new RectF(mOrbitCircleX, mOrbitCircleY, mOrbitCircleX + circleSize, mOrbitCircleY + circleSize);
        }
      }
    });
    mOrbitContainer.setOnTouchListener(this);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {

    float x = event.getX();
    float y = event.getY();
    float diffX, diffY;

    float screenWidth = Etils.getScreenWidth(MainActivity.this);
    float centerX = screenWidth / 2;

    if (mCircleRect == null) { return false; }

    Path circlePath = new Path();
    circlePath.addOval(mCircleRect, Path.Direction.CW);

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = x;
        startY = y;

        View circleView;
        float[] pos;
        for (int i = 1; i < 7; i++) {
          pos = Etils.getDistanceInPathCoordinates(circlePath, (i)/6f);
          circleView = mOrbitContainer.getChildAt(i);
          float circleSize = getResources().getDimensionPixelSize(R.dimen.circle_size);
          circleView.animate().x(pos[0] - circleSize / 2).y(pos[1] - circleSize / 2);
//          circleView.setX(pos[0] - circleSize / 2); circleView.setY(pos[1] - circleSize / 2);
        }

        break;
      case MotionEvent.ACTION_UP:
        // reset locations
        break;
      case MotionEvent.ACTION_MOVE:
        diffX = x - startX;
        diffY = y - startY;
        break;
    }
    return false;
  }
}













