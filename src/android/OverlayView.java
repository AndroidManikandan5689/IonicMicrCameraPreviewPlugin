package cordova.plugin.raqmiyat.micrcameraview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * OverlayView implementation to Draw MICR Area in live capture mode
 */
public class OverlayView extends View {

   private Paint mLiveCapturePaint;
   private Paint mLiveCapturePaintMicr;
   private Paint mTextPaint;
   private Paint mRectPaint;

   private float mVerticalStrokeWidth;
   private float mMicrClearBandHeight;
   private Rect mMicrAreaBounds;
   private RectF mMicrNoteBounds;
   private static String mMicrNoteLine1;
   private static String mMicrNoteLine2;
   private static String mMicrAreaNote;
   private int mMicrNoteTop;
   private int mMicrNoteLeftLine1;
   private int mMicrNoteLeftLine2;
   private int mMicrAreaNoteLeft;

   private Rect mLiveCaptureRect;

   public OverlayView(Context context) {
      super(context);
      init(context, null, 0);
   }

   public OverlayView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init(context, attrs, 0);
   }

   public OverlayView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      init(context, attrs, defStyle);
   }

   void init(Context context, AttributeSet attrs, int defStyle) {
      if(mMicrNoteLine1 == null)
         mMicrNoteLine1 = "Make sure the MICR code fits within the MICR Area guides.";
      if(mMicrNoteLine2 == null)
         mMicrNoteLine2 = "Also ensure there is plenty of light on the check.";
      if(mMicrAreaNote == null)
         mMicrAreaNote = "MICR Area";

      // Init overlay rectangle
      mLiveCaptureRect = new Rect();

      mMicrAreaBounds = new Rect();
      mMicrNoteBounds = new RectF();
      mLiveCapturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mLiveCapturePaint.setColor(Color.RED);
      mLiveCapturePaint.setStrokeWidth(3);
      mLiveCapturePaint.setStyle(Paint.Style.STROKE);

      mLiveCapturePaintMicr = new Paint(Paint.ANTI_ALIAS_FLAG);
      mLiveCapturePaintMicr.setColor(Color.RED);
      mLiveCapturePaintMicr.setStrokeWidth(3);
      mLiveCapturePaintMicr.setStyle(Paint.Style.STROKE);
      mLiveCapturePaintMicr.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));

      mTextPaint = new Paint();
      mTextPaint.setColor(Color.WHITE);
      mTextPaint.setTextSize(20);

      mRectPaint = new Paint();
      mRectPaint.setColor(Color.DKGRAY);
      mRectPaint.setAlpha(128);
   }

   @Override
   public void onDraw(Canvas canvas) {
      super.onDraw(canvas);

      canvas.drawRoundRect(mMicrNoteBounds, 10, 10, mRectPaint);
      canvas.drawText(mMicrNoteLine1, mMicrNoteLeftLine1, mMicrNoteTop, mTextPaint);
      canvas.drawText(mMicrNoteLine2, mMicrNoteLeftLine2, mMicrNoteTop * 2, mTextPaint);
      canvas.drawText(mMicrAreaNote, mMicrAreaNoteLeft, (int)(mMicrAreaBounds.top + (mMicrAreaBounds.height() / 3.0) * 2.0), mTextPaint);

      canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.top, mLiveCaptureRect.right, mLiveCaptureRect.top, mLiveCapturePaint);
      canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.top, mLiveCaptureRect.left, mLiveCaptureRect.top + mVerticalStrokeWidth, mLiveCapturePaint);
      canvas.drawLine(mLiveCaptureRect.right, mLiveCaptureRect.top, mLiveCaptureRect.right, mLiveCaptureRect.top + mVerticalStrokeWidth, mLiveCapturePaint);
      canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.bottom, mLiveCaptureRect.left, mLiveCaptureRect.bottom - mVerticalStrokeWidth, mLiveCapturePaint);
      canvas.drawLine(mLiveCaptureRect.right, mLiveCaptureRect.bottom, mLiveCaptureRect.right, mLiveCaptureRect.bottom - mVerticalStrokeWidth, mLiveCapturePaint);
      canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.bottom, mLiveCaptureRect.right, mLiveCaptureRect.bottom, mLiveCapturePaint);

      canvas.drawLine(mMicrAreaBounds.left, mMicrAreaBounds.top, mMicrAreaBounds.right, mMicrAreaBounds.top, mLiveCapturePaintMicr);
   }

   /**
    * Update MICR area
    * @param surfaceSize View size.
    * @param previewSize Camera preview size.
    * @return the MICR read area.
    */
   public LeadRect updateArea(LeadSize viewSize, LeadSize surfaceSize, LeadSize previewSize) {
      // Update the area
      float horizontalMargins = viewSize.getWidth() / 15;
      float verticalMargins = viewSize.getHeight() / 15;

      float instructionLabelWidth = viewSize.getWidth() - (horizontalMargins * 2);
      float instructionLabelHeight = (viewSize.getHeight() / 8);
      float instructionLabelLeft = horizontalMargins;
      float instructionLabelTop = verticalMargins / 2;

      float cameraGuideLeft = instructionLabelLeft;
      float cameraGuideTop = instructionLabelTop + instructionLabelHeight + (verticalMargins / 2);
      float cameraGuideWidth = instructionLabelWidth;
      float cameraGuideHeight = viewSize.getHeight() - cameraGuideTop - verticalMargins;

      mVerticalStrokeWidth = (float)(cameraGuideHeight / 4.0);

      mMicrClearBandHeight = cameraGuideHeight / 5;

      mLiveCaptureRect = new Rect((int)cameraGuideLeft, (int)cameraGuideTop, (int)(cameraGuideWidth + cameraGuideLeft), (int)(cameraGuideHeight + cameraGuideTop));

      mMicrNoteTop = (int)(mLiveCaptureRect.top / 3.0);
      mMicrNoteLeftLine1 = (int)(((mLiveCaptureRect.width()) / 2 + horizontalMargins)  - (mTextPaint.measureText(mMicrNoteLine1) / 2));
      mMicrNoteLeftLine2 = (int)(((mLiveCaptureRect.width()) / 2 + horizontalMargins)  - (mTextPaint.measureText(mMicrNoteLine2) / 2));
      mMicrAreaNoteLeft = (int)(((mLiveCaptureRect.width()) / 2 + horizontalMargins)  - (mTextPaint.measureText(mMicrAreaNote) / 2));

      int micrTop = (int)(cameraGuideHeight + cameraGuideTop - mMicrClearBandHeight);
      mMicrAreaBounds = new Rect((int)cameraGuideLeft, micrTop, mLiveCaptureRect.right, (int)(mMicrClearBandHeight + micrTop));
      mMicrNoteBounds = new RectF(mLiveCaptureRect.left, mMicrNoteTop - mTextPaint.getTextSize(), mLiveCaptureRect.right, mLiveCaptureRect.top - 10);

      float ratioX = (float)surfaceSize.getWidth() / previewSize.getWidth();
      float ratioY = (float)surfaceSize.getHeight() / previewSize.getHeight();

      return LeadRect.fromLTRB(
              (int)(mMicrAreaBounds.left / ratioX),
              (int)(mMicrAreaBounds.top / ratioY),
              (int)(mMicrAreaBounds.right / ratioX),
              (int)(mMicrAreaBounds.bottom / ratioY));
   }
}
