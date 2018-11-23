package com.xp.calendar.calendar;

/**
 * Created by panda on 2015/11/10.
 * 自定义日历卡
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarCard extends View {

    private static final int TOTAL_COL = 7; // 7列
    private static final int TOTAL_ROW = 6; // 6行

    private Paint mCirclePaint; // 绘制圆形的画笔
    private Paint mCirclePaintKong; // 绘制圆形的画笔
    private Paint mTextPaint; // 绘制文本的画笔
    private Paint mDianPaint; // 绘点画笔
    private int mViewWidth; // 视图的宽度
    private int mViewHeight; // 视图的高度
    private int mCellSpace; // 单元格间距
    private int mW;//横向间距
    private int mH;//纵向间距
    private Row rows[] = new Row[TOTAL_ROW]; // 行数组，每个元素代表一行
    private static CustomDate mShowDate; // 自定义的日期，包括year,month,day
    private OnCellClickListener mCellClickListener; // 单元格点击回调事件
    private int touchSlop; //
    private boolean callBackCellSpace;

    private Cell mClickCell;
    private float mDownX;
    private float mDownY;
    private List<Custom> listDay;
    private SimpleDateFormat sdfLast = new SimpleDateFormat("yyyy-M-d");

    private CustomDate customNow;

    /**
     * 单元格点击的回调接口
     *
     * @author wuwenjie
     */
    public interface OnCellClickListener {
        void clickDate(CustomDate date); // 回调点击的日期

        void changeDate(CustomDate date); // 回调滑动ViewPager改变的日期
    }

    public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCard(Context context) {
        super(context);
        init(context);
    }

    public CalendarCard(Context context, OnCellClickListener listener, List<Custom> listDay) {
        super(context);
        this.mCellClickListener = listener;
        this.listDay = listDay;
        init(context);
    }

    public void updateList(List<Custom> listDay) {
        this.listDay = listDay;
        update();
    }

    private void init(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaintKong = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDianPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaintKong.setStyle(Paint.Style.STROKE);
        mCirclePaintKong.setStrokeWidth(3);
        mDianPaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#ff7800")); // 红色圆形
        mCirclePaintKong.setColor(Color.parseColor("#ff7800")); // 红色圆形
        mDianPaint.setColor(Color.GRAY); // 灰色圆点
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initDate();
    }

    private void initDate() {
        customNow = new CustomDate(0, 0, 0);
        mShowDate = new CustomDate();
        fillDate();

    }

    private void fillDate() {
        int monthDay = DateUtil.getCurrentMonthDay();
        int month = DateUtil.getCurrentMonthNow();
        int year = DateUtil.getCurrentYeatNow();

        int yearClick;
        int monthClick;
        int dayClick;

        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month - 1); // 上个月的天数
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                mShowDate.month);
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new Row(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int position = i + j * TOTAL_COL; // 单元格位置
                int have = 0;
                // 这个月的
                if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                    day++;
                    yearClick = mShowDate.year;
                    monthClick = mShowDate.month;
                    dayClick = day;
                    rows[j].cells[i] = new Cell(new CustomDate(yearClick, monthClick, dayClick), State.CURRENT_MONTH_DAY, have, i, j);
                    for (int n = 0; n < listDay.size(); n++) {
                        if (mShowDate.getYear() == listDay.get(n).getYear() && mShowDate.getMonth() == listDay.get(n).getMonth() && day == listDay.get(n).getDay()) {
                            rows[j].cells[i] = new Cell(new CustomDate(yearClick, monthClick, dayClick), State.PITCH_ON_DAY, have, i, j);
                            break;
                        }
                    }

                    try {
                        String time = year + "-" + month + "-" + monthDay;
                        String timeNow = yearClick + "-" + monthClick + "-" + dayClick;
                        Date timeDate = sdfLast.parse(time);
                        Date timeNowDate = sdfLast.parse(timeNow);
                        long timeLong = timeDate.getTime();
                        long timeNowLong = timeNowDate.getTime();
                        if (timeNowLong == timeLong) {//今天
                            rows[j].cells[i] = new Cell(new CustomDate(yearClick, monthClick, dayClick), State.TODAY, have, i, j);
                        }
                    } catch (ParseException e) {

                    }
                } else if (position < firstDayWeek) {//过去一个月
                    yearClick = mShowDate.year;
                    monthClick = mShowDate.month - 1;
                    dayClick = lastMonthDays - (firstDayWeek - position - 1);
                    rows[j].cells[i] = new Cell(new CustomDate(yearClick, monthClick, dayClick), State.NO_CURRENT_MONTH_DAY, have, i, j);
                } else if (position >= firstDayWeek + currentMonthDays) {
                    yearClick = mShowDate.year;
                    monthClick = mShowDate.month + 1;
                    dayClick = position - firstDayWeek - currentMonthDays + 1;
                    rows[j].cells[i] = new Cell(new CustomDate(yearClick, monthClick, dayClick), State.NO_CURRENT_MONTH_DAY, have, i, j);
                }


            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null) {
                rows[i].drawCells(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mCellSpace = Math.min(mViewHeight / TOTAL_ROW, mViewWidth / TOTAL_COL);
        mW = mViewWidth / TOTAL_COL;
        mH = mViewHeight / TOTAL_ROW;
        if (!callBackCellSpace) {
            callBackCellSpace = true;
        }
        mTextPaint.setTextSize(mW / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mW);
                    int row = (int) (mDownY / mH);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 计算点击的单元格
     *
     * @param col
     * @param row
     */
    private void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW)
            return;
        if (mClickCell != null) {
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
        }
        if (rows[row] != null) {
            mClickCell = new Cell(rows[row].cells[col].date,
                    rows[row].cells[col].state, rows[row].cells[col].i,
                    rows[row].cells[col].j);

            CustomDate date = rows[row].cells[col].date;
            date.week = col;
            int monthDay = DateUtil.getCurrentMonthDay(); // 今天
            int monthNow = DateUtil.getCurrentMonthNow();//本月
            int yearNow = DateUtil.getCurrentYeatNow();//今年
            if (date.getYear() == yearNow && date.getMonth() == monthNow && date.getDay() == monthDay) {
                mCellClickListener.clickDate(date);
            }
            for (int n = 0; n < listDay.size(); n++) {
                if (date.getYear() == listDay.get(n).getYear() && date.getMonth() == listDay.get(n).getMonth() && date.getDay() == listDay.get(n).getDay()) {
                    mCellClickListener.clickDate(date);
                    break;
                }
            }
        }
    }


    public static CustomDate setCustomDate(CustomDate chage, Custom now) {
        chage.setYear(now.getYear());
        chage.setMonth(now.getMonth());
        chage.setDay(now.getDay());
        return chage;
    }

    /**
     * 组元素
     *
     * @author wuwenjie
     */
    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public Cell[] cells = new Cell[TOTAL_COL];

        // 绘制单元格
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    cells[i].drawSelf(canvas);
                }
            }
        }

    }

    /**
     * 单元格元素
     *
     * @author wuwenjie
     */
    class Cell {
        public CustomDate date;
        public State state;
        public int have;//0代表没数据1代表有数据
        public int i;
        public int j;

        public Cell(CustomDate date, State state, int i, int j) {
            super();
            this.date = date;
            this.state = state;
            this.i = i;
            this.j = j;
        }

        public Cell(CustomDate date, State state, int have, int i, int j) {
            super();
            this.date = date;
            this.state = state;
            this.have = have;
            this.i = i;
            this.j = j;
        }


        public void drawSelf(Canvas canvas) {
            switch (state) {
                case CURRENT_MONTH_DAY: //当月
                    mTextPaint.setColor(Color.parseColor("#484848"));
                    break;
                case TODAY: // 今天
                    mTextPaint.setColor(Color.parseColor("#ff7800"));
                    canvas.drawCircle((float) (mW * (i + 0.51)),
                            (float) ((j + 0.38) * mH), mCellSpace * 5 / 13,
                            mCirclePaintKong);
                    break;
                case PITCH_ON_DAY: //有数据的日期
                    mTextPaint.setColor(Color.parseColor("#fffffe"));
                    canvas.drawCircle((float) (mW * (i + 0.51)),
                            (float) ((j + 0.38) * mH), mCellSpace * 5 / 13,
                            mCirclePaint);
                    break;
                case NO_CURRENT_MONTH_DAY: //当页除去本月日期
                    mTextPaint.setColor(Color.GRAY);
                    break;

                default:
                    break;
            }
            // 绘制文字
            String content = date.day + "";
            canvas.drawText(content, (float) ((i + 0.5) * mW - mTextPaint.measureText(content) / 2), (float) ((j + 0.7) * mH - mTextPaint.measureText(content, 0, 1) / 2), mTextPaint);
        }
    }

    /**
     * @author wuwenjie 单元格的状态 当前月日期，过去的月的日期，下个月的日期
     */
    enum State {
        TODAY, NO_CURRENT_MONTH_DAY, CURRENT_MONTH_DAY, PITCH_ON_DAY
    }

    // 从左往右划，上一个月
    public CustomDate leftSlide() {
        if (mShowDate.month == 1) {
            mShowDate.month = 12;
            mShowDate.year -= 1;
        } else {
            mShowDate.month -= 1;
        }
        update();
        return mShowDate;
    }

    // 从右往左划，下一个月
    public CustomDate rightSlide() {
        if (mShowDate.month == 12) {
            mShowDate.month = 1;
            mShowDate.year += 1;
        } else {
            mShowDate.month += 1;
        }

        update();
        return mShowDate;
    }

    public void update() {
        fillDate();
        invalidate();
    }

}
