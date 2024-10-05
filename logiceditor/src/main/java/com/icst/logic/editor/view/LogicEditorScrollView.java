package com.icst.logic.editor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Custom scrollable view for block-based editor, allowing controlled scrolling behavior with
 * options to restrict or force scroll actions.
 */
public class LogicEditorScrollView extends FrameLayout {

  private int scrollThreshold; // Minimum scroll distance required to consider scroll
  private float initialX; // Initial X coordinate for touch event
  private float initialY; // Initial Y coordinate for touch event
  private boolean allowScroll; // Flag to allow or restrict scrolling

  /* After scroll threshold is met */
  private float initialScrollX; // Initial X coordinate for touch event
  private float initialScrollY; // Initial Y coordinate for touch event

  public LogicEditorScrollView(final Context context, final AttributeSet set) {
    super(context, set);
    /*
     * Saves initial X and Y coordinates for onInterceptTouchEvent calculations.
     * Setting -1.0f in variable represent that user has had not touched a view yet.
     * as ACTION_DOWN is not performed yet.
     */
    initialX = -1.0f;
    initialY = -1.0f;

    // Defining scroll threshold a values.
    scrollThreshold = ViewConfiguration.get(context).getScaledTouchSlop();
  }

  /**
   * Return false if you want the event to transfer to its child. Return true if you do not want the
   * event to transfer to its child.
   */
  @Override
  public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
    // Transfer event to children if scroll is disabled.
    if (!getAllowScroll()) return false;

    // A boolean that will store if change in motion x, y is greater than scroll thresh hold.
    boolean isScrollThresholdAchieved = false;

    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
      // Store initial x, y coordinates of touches.
      initialX = motionEvent.getX();
      initialY = motionEvent.getY();
    }

    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
      // Store initial x, y coordinates of touches to be -1 as view is released.
      initialX = -1.0f;
      initialY = -1.0f;
      isScrollThresholdAchieved = false;
    }

    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
      // Calculate change in x, y coordinates respect with initial coordinates.
      float deltaX = motionEvent.getX() - initialX;
      float deltaY = motionEvent.getY() - initialY;

      // Store |deltaX| and |deltaY|

      float deltaXMagnitude = Math.abs(deltaX);
      float deltaYMagnitude = Math.abs(deltaY);

      // Compare |deltaX| and |deltaY| with scroll threshold.
      if (deltaXMagnitude > scrollThreshold || deltaYMagnitude > scrollThreshold) {
        isScrollThresholdAchieved = true;
      }
    }
    return isScrollThresholdAchieved;
  }

  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
      // Store initial x, y coordinates of touches(after threshold scroll).
      initialScrollX = motionEvent.getX();
      initialScrollY = motionEvent.getY();
    }

    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
      /**
       * Store initial x, y coordinates of touches to be -1 as view is released(after threshold
       * scroll was met).
       */
      initialScrollX = -1.0f;
      initialScrollY = -1.0f;
    }

    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
      /*
       * Calculate change in x, y coordinates respect with initial coordinates after threshold scroll was met.
       */
      float deltaX = initialScrollX - motionEvent.getX();
      float deltaY = initialScrollY - motionEvent.getY();

      // Store |deltaX| and |deltaY|

      float deltaXMagnitude = Math.abs(deltaX);
      float deltaYMagnitude = Math.abs(deltaY);

      float finalScrollX = 0;
      float finalScrollY = 0;

      if (deltaX < 0) {
        // From up to down is scrolled
        // Make sure user does not scroll greater than current scrollX

        if (deltaXMagnitude <= getScrollX()) {
          finalScrollX = deltaX;
        }
      } else {
        // From down to up is scrolled
        // Scroll if there is more content
        if (getParent() != null) {
          if (getParent() instanceof View parent) {
            if (getScrollX() + parent.getWidth() < getWidth()) {
              finalScrollX = deltaX;
            }
          }
        }
      }

      if (deltaY < 0) {
        // From left to right is scrolled
        // Make sure user does not scroll greater than current scrollY

        if (deltaXMagnitude <= getScrollY()) {
          finalScrollX = deltaY;
        }
      } else {
        // From right to left is scrolled
        // Scroll if there is more content
        if (getParent() != null) {
          if (getParent() instanceof View parent) {
            if (getScrollY() + parent.getHeight() < getHeight()) {
              finalScrollY = deltaY;
            }
          }
        }
      }

      // Scroll to calculated delta coordinates
      scrollBy((int)(finalScrollX), (int)(finalScrollY));
    }
    return true;
  }

  public boolean getAllowScroll() {
    return this.allowScroll;
  }

  public void setAllowScroll(boolean allowScroll) {
    this.allowScroll = allowScroll;
  }
}