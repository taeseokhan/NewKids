package senori.or.jp.newkids.calendar;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import senori.or.jp.newkids.DayActivity;

public class CalendarView {
    private final static int ROWS = 7;
    private final static int COLS = 7;

    private Context context;
    //private ArrayList<Item> list;

    private LinearLayout m_targetLayout; // / �޷��� ���� ���̾ƿ�
    // private Button[] m_controlBtn; // / �޷� ��Ʈ���� ��ư 4�� [���⵵, �����⵵, ����, ������]
    // private TextView[] m_viewTv; // / �� �� �� ǥ���� �ؽ�Ʈ�� 3��[��, �� , ��]

    private Calendar m_Calendar; // / ����� �޷�
    private Calendar iCal; // ���纻
    private LinearLayout[] m_linely; // 7���� ����ǥ�� + �ִ� 6
    private LinearLayout[] m_celly; // 7ĭ
    private TextView[] m_cellTextBtn;
    private LinearLayout[] m_horizontalLine;
    private LinearLayout[] m_verticalLine;
    private LinearLayout m_endline;

    private int m_starPos; // ������� ������ ��ġ
    private int m_lastDay; // �״� ��������
    private int m_selDay; // ���� ���õ� ��¥
    private int m_nextDay; // ������ ��������
    private int m_sum; // ����� �� ����
    private float m_ratingAvg; // ��������
    private float m_displayScale;
    private float m_textSize;
    private float m_topTextSize;

    public int m_tcHeight = 50;
    private int m_cWidth;
    private int m_cHeight;
    private int m_lineSize = 1;

    static public class gsCalendarColorParam {
        int m_lineColor = 0xff000000; // / ��輱 ��
        int m_cellColor = 0x00000000; // / ĭ�� ����
        int m_topCellColor = 0xffcccccc; // / ���� ����
        int m_textColor = 0xff000000; // / �۾���
        int m_sundayTextColor = 0xffff0000; // / �Ͽ��� �۾���
        int m_saturdayTextColor = 0xff0000ff; // / ����� �۾���
        int m_topTextColor = 0xff000000; // / ���� �۾���
        int m_topSundayTextColor = 0xffff0000; // / ���� �Ͽ��� �۾���
        int m_topSaturdatTextColor = 0xff0000ff; // / ���� ����� �۾���
        int m_todayCellColor = 0x999999ff; // / ���ó�¥�� ����
        int m_todayTextColor = 0xffffffff; // / ���ó�¥�� �۾���
        int m_dayColor = 0xffffffff;
    }

    private gsCalendarColorParam m_colorParam;

    // / ������ �����ϰ� ������ bgcolor�� ó����( ���� ���������� )
    private Drawable m_bgImgId = null; // / ��ü ����̹���
    private Drawable m_cellBgImgId = null; // / ��ĭ�� ��� �̹���
    private Drawable m_topCellBgImgId = null; // / ��� ���� ���� �κ��� ��� �̹���

    private Drawable m_todayCellBgImgId = null; // / ���� ��¥�� ��� �̹���

    // / ��ܿ� ǥ���ϴ� ���� �ؽ�Ʈ
    // String[] m_dayText = { "��", "��", "ȭ", "��", "��", "��", "��" };

    // /////////////////////////////////////////

    private Button m_preYearBtn; // / ���⵵ ��ư
    private Button m_nextYearBtn; // / �����⵵ ��ư
    private Button m_preMonthBtn; // / ���� ��ư
    private Button m_nextMonthBtn; // / ������ ��ư

    private TextView m_yearTv; // / �� ǥ�ÿ� �ؽ�Ʈ
    private TextView m_mothTv; // / �� ǥ�ÿ� �ؽ�Ʈ
    private TextView m_dayTv; // / ��¥ ǥ�ÿ� �ؽ�Ʈ

    // / ������ MMdd�������� �ִ´�.
    // / ������ 2�� 4 5 6�̶��
    // / [0204] [0205] [0206] �̷��� ����
    private ArrayList<Integer> m_holiDay = new ArrayList<Integer>();

    @SuppressWarnings("deprecation")
    public CalendarView(Context context, LinearLayout layout) {
        // / context����
        this.context = context;
        //this.list = list;
        // / Ÿ�� ���̾ƿ� ����
        m_targetLayout = layout;

        // / ���� ��¥�� �޷� ����
        m_Calendar = Calendar.getInstance();
        // m_Calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);

        // / ǥ���� ������ ���̾� ����
        m_linely = new LinearLayout[COLS]; // / 7���� ���̾ƿ� ����
        m_celly = new LinearLayout[COLS * ROWS]; // / �׾ȿ� �ٴ� 7���� �� 49���� ���̾ƿ� ����
        m_cellTextBtn = new TextView[COLS * ROWS]; // / ������ ������ ��ư�� ����
        m_horizontalLine = new LinearLayout[COLS - 1]; // / ���� ���м� ���̾ƿ�
        m_verticalLine = new LinearLayout[(COLS - 1) * ROWS]; // / ���� ���м� ���̾ƿ�
        m_endline = new LinearLayout(context);
        // / ȭ���� ũ�⿡ ���� ������
        m_displayScale = context.getResources().getDisplayMetrics().density;

        m_topTextSize = m_displayScale * 5.0f;
        m_textSize = m_displayScale * 5.0f;

        m_colorParam = new gsCalendarColorParam();
        Display dis = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        m_cWidth = dis.getWidth() / 7;
        m_cHeight = dis.getHeight() / 11;
        // ROWS = 7 ; /// ��/���
        // COLS = 14 ; /// ĭ/����
    }

    // / �޷��� �����Ѵ�.( ��� �ɼǵ�[�÷���, �ؽ�Ʈ ũ�� ��]�� ������ �Ŀ� �������� ��� �� �� ȣ��)
    public void initCalendar() {
        createViewItem();
        setLayoutParams();
        setLineParam();
        setContentext();
        setOnEvent();
        printView();
    }

    // / �÷��� �Ķ���� ����
    public void setColorParam(gsCalendarColorParam param) {
        m_colorParam = param;
    }

    // / ������� �� �̹����� ����
    public void setBackground(Drawable bg) {
        m_bgImgId = bg;
    }

    public void setCellBackground(Drawable bg) {
        m_cellBgImgId = bg;
    }

    public void setTopCellBackground(Drawable bg) {
        m_topCellBgImgId = bg;
    }

    public void setCalendarSize(int width, int height) {
        m_cWidth = (width - (m_lineSize * COLS - 1)) / COLS;
        m_cHeight = (height - (m_lineSize * ROWS - 1)) / ROWS;
        m_tcHeight = (height - (m_lineSize * COLS - 1)) / COLS;
    }

    public void setCellSize(int cellWidth, int cellHeight, int topCellHeight) {
        m_cWidth = cellWidth;
        m_cHeight = cellHeight;
        m_tcHeight = topCellHeight;
    }

    public void setTopCellSize(int topCellHeight) {
        m_tcHeight = topCellHeight;
    }

    public void setCellSize(int allCellWidth, int allCellHeight) {
        m_cWidth = allCellWidth;
        m_cHeight = allCellHeight;
        m_tcHeight = allCellHeight;
    }

    public void setTextSize(float size) {
        m_topTextSize = m_displayScale * size;
        m_textSize = m_displayScale * size;
    }

    public void setTextSize(float textSize, float topTextSize) {
        m_topTextSize = m_displayScale * topTextSize;
        m_textSize = m_displayScale * textSize;
    }

    public void redraw() {
        m_targetLayout.removeAllViews();
        initCalendar();

    }

    // //////////////////������ ��¥ĭ�� ��ȭ�� �ִ� �Լ� //////////////////////////
    // / �̳༮�� �ҷ������� ���´� ��¥�� ���÷� ���õǾ��ְų� ���� �������� ����
    // / �׷����� m_cellLy[ ��¥ + m_startPos ].setTextColor( ) ;
    // / m_startPos�� ������ ������ ��¥�� ���ϸ� �ش� ��¥ĭ�� ������� �ٲ� �� ����
    // / ////////////////////////////////////////////////////////////////////
    // / ���õ� ��¥ĭ�� ��ȭ�� �ֱ����� �Լ� 1ȣ
    public void setSelectedDay(int cellColor, int textColor) {
        m_colorParam.m_todayCellColor = cellColor;
        m_colorParam.m_todayTextColor = textColor;
        m_cellTextBtn[m_Calendar.get(Calendar.DAY_OF_MONTH) + m_starPos - 1]
                .setTextColor(textColor);
        m_cellTextBtn[m_Calendar.get(Calendar.DAY_OF_MONTH) + m_starPos - 1]
                .setBackgroundColor(cellColor);
    }

    // / ���õ� ��¥ĭ�� ��ȭ�� �ֱ����� �Լ� 2ȣ
    public void setSelectedDayTextColor(int textColor) {
        m_colorParam.m_todayTextColor = textColor;
        m_cellTextBtn[m_Calendar.get(Calendar.DAY_OF_MONTH) + m_starPos - 1]
                .setTextColor(textColor);
    }

    // / ���õ� ��¥ĭ�� ��ȭ�� �ֱ����� �Լ� 3ȣ
    @SuppressWarnings("deprecation")
    public void setSelectedDay(Drawable bgimg) {
        m_todayCellBgImgId = bgimg;
        m_colorParam.m_todayCellColor = 0x00000000;
        m_cellTextBtn[m_Calendar.get(Calendar.DAY_OF_MONTH) + m_starPos - 1]
                .setBackgroundDrawable(bgimg);
        Log.d("===", (m_Calendar.get(Calendar.DAY_OF_MONTH) - 1) + "");
    }

    // /////////////////////////// ������ ó�� ///////////////////////
    // / ������ MMdd�������� �ִ´�.
    // / ������ 2�� 4 5 6�̶��
    // / [0204] [0205] [0206] �̷��� ����
    public void addHoliday(int holiday_MMdd) {
        m_holiDay.add(holiday_MMdd);
    }

    // / ������ ����Ʈ�� �������鼭 �ش� ��¥�� �Ͽ��ϰ� ���� ������ ����
    public void applyHoliday() {
        // / ���� �޷��� ���� ����
        Integer iMonth = m_Calendar.get(Calendar.MONTH) + 1;

        // / ���Ϸ� ����� ��� ��¥ ���� ���� ����
        for (int k = 0; k < m_holiDay.size(); k++) {
            int holiday = m_holiDay.get(k); // / ���� ���� ���Ѵ���
            if (holiday / 100 == iMonth) // / ���� ������ ���
            {
                // / �ش� ��¥�� ���� �÷��� ����
                m_cellTextBtn[holiday % 100 + m_starPos]
                        .setTextColor(m_colorParam.m_sundayTextColor);
            }
        }
    }

    // / ������ ����ŭ ���� �������� ��¥ ǥ��Ǵ� �� + ��輱 �ټ�
    public void createViewItem() {
        // / 7���̸� ��輱 ���α��� ���ؼ� 13���� �����ؾ��Ѵ�.
        for (int i = 0; i < ROWS * 2 - 1; i++) {
            // / ¦�������϶���
            if (i % 2 == 0) {
                // / ������ 13�� ���������� ������ ǥ�õǴ� ������ 7����
                m_linely[i / 2] = new LinearLayout(context);
                m_targetLayout.addView(m_linely[i / 2]); // ���� ���̾ƿ��� �ڽ����� ���

                // / ��¥ǥ�� ĭ�� ��輱 ���ؼ� 13���� ĭ�� ����
                for (int j = 0; j < COLS * 2 - 1; j++) {

                    // / �޷� ������ ���� ĭ
                    if (j % 2 == 0) {
                        int pos = ((i / 2) * COLS) + (j / 2);

                        Log.d("pos1", "" + pos);
                        m_celly[pos] = new LinearLayout(context);
                        m_cellTextBtn[pos] = new TextView(context);
                        m_linely[i / 2].addView(m_celly[pos]);
                        m_celly[pos].addView(m_cellTextBtn[pos]);

                    } else // / �̰� �ܼ��� ��輱
                    {
                        int pos = ((i / 2) * (COLS - 1)) + (j - 1) / 2;

                        Log.d("pos2", "" + pos);
                        m_verticalLine[pos] = new LinearLayout(context);
                        m_linely[i / 2].addView(m_verticalLine[pos]);
                    }
                }
            } else // / �̰� ������ ��輱
            {
                m_horizontalLine[(i - 1) / 2] = new LinearLayout(context);
                m_targetLayout.addView(m_horizontalLine[(i - 1) / 2]);

            }
        }

    }

    // / ���̾ƿ��� ��ư�� ����, �۾��� �� ViewParams�� ����
    @SuppressWarnings("deprecation")
    public void setLayoutParams() {
        // / ���� ���̾ƿ��� ���η� ����
        m_targetLayout.setOrientation(LinearLayout.VERTICAL);
        // / ���� ��ü ����� ������ �־���
        if (m_bgImgId != null) {
            m_targetLayout.setBackgroundDrawable(m_bgImgId);
        }

        // / ������ ����ŭ ���� �������� ��¥ ǥ��Ǵ� �� + ��輱 �ټ�
        for (int i = 0; i < ROWS * 2 - 1; i++) {
            if (i % 2 == 0) {
                // / �� ������ �����ϴ� ���̾ƿ����� ���η� ����~
                m_linely[i / 2].setOrientation(LinearLayout.HORIZONTAL);
                m_linely[i / 2].setLayoutParams( // / ���̾ƿ� ������� warp_content�� ����
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                // / ��ĭ��ĭ �ɼ��� ����
                for (int j = 0; j < COLS; j++) {
                    int cellnum = ((i / 2) * COLS) + j;
                    // / ��ĭ��ĭ�� �����ϴ� ���̾ƿ� ������� ���� wrap_content�� ����
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    // param.setMargins( 1, 1, 1, 1 ) ; /// ������ 1�� �༭ ������ �׸���.
                    m_celly[cellnum].setLayoutParams(param);
                    // / ��ĭ��ĭ ���� ��ư
                    m_cellTextBtn[cellnum].setGravity(Gravity.TOP);

                    // / ���ϴ� ���� �۾��� �۾� ũ�� �����ϴ� �κ�

                    // / ù������ ��ȭ��������� ǥ���ϴ� �κ�
                    if (i == 0) {
                        // / ���� ǥ���ϴ� �κ��� ���� ����
                        m_cellTextBtn[cellnum]
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        m_cWidth, m_tcHeight));

                        // / ���� �۾���
                        if (m_topCellBgImgId != null) {
                            m_celly[cellnum]
                                    .setBackgroundDrawable(m_topCellBgImgId);
                        } else {
                            m_celly[cellnum]
                                    .setBackgroundColor(m_colorParam.m_topCellColor);
                        }

                        // / ����ϰ� �Ͽ����� �ٸ� �÷��� ǥ���Ѵ�.
                        switch (j) {
                            case 0:
                                m_cellTextBtn[cellnum]
                                        .setTextColor(m_colorParam.m_topSundayTextColor);
                                break;
                            case 6:
                                m_cellTextBtn[cellnum]
                                        .setTextColor(m_colorParam.m_topSaturdatTextColor);
                                break;
                            default:
                                m_cellTextBtn[cellnum]
                                        .setTextColor(m_colorParam.m_topTextColor);
                                break;
                        }

                        // / �۾� ũ��
                        m_cellTextBtn[cellnum].setTextSize(m_topTextSize);
                    } else // / ���ϴ� ��¥ ǥ���ϴ� �κ�
                    {

                        // / ���� ǥ�õǴ� �κ��� ���̿� ����
                        m_cellTextBtn[cellnum]
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        m_cWidth, m_cHeight));

                        // / bg�� �۾���
                        if (m_cellBgImgId != null) {
                            m_celly[cellnum]
                                    .setBackgroundDrawable(m_cellBgImgId);
                        } else {
                            m_celly[cellnum]
                                    .setBackgroundColor(m_colorParam.m_cellColor);
                        }

                        // / ����ϰ� �Ͽ����� �ٸ� �÷��� ǥ���Ѵ�.
                        switch (j) {
                            case 0:
                                m_cellTextBtn[cellnum]
                                        .setTextColor(m_colorParam.m_sundayTextColor);
                                break;
                            case 6:
                                m_cellTextBtn[cellnum]
                                        .setTextColor(m_colorParam.m_saturdayTextColor);
                                break;
                            default:
                                m_cellTextBtn[cellnum]
                                        .setTextColor(m_colorParam.m_textColor);
                                break;
                        }

                        // / �۾� ũ��
                        m_cellTextBtn[cellnum].setTextSize(m_textSize);
                    }

                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void setLineParam() {
        for (int i = 0; i < ROWS - 1; i++) {
            m_horizontalLine[i].setBackgroundColor(m_colorParam.m_lineColor); // /
            // ���λ�
            m_horizontalLine[i].setLayoutParams( // / ���� �����̴ϱ� ���δ� �� ���δ� �β���ŭ
                    new LinearLayout.LayoutParams(Toolbar.LayoutParams.FILL_PARENT,
                            m_lineSize));
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS - 1; j++) {
                int pos = (i * (COLS - 1)) + j;
                m_verticalLine[pos]
                        .setBackgroundColor(m_colorParam.m_lineColor); // / ���λ�
                m_verticalLine[pos].setLayoutParams( // / ���� �����̴ϱ� ���δ� ��~ ���δ�
                        // �β���ŭ
                        new LinearLayout.LayoutParams(m_lineSize,
                                Toolbar.LayoutParams.FILL_PARENT));
            }
        }
    }

    // / �޷��� �����ϴ� �� �� ���� �����ϱ�
    public void setContentext() {
        // / �޷��� �ϳ� �����ؼ� �۾��Ѵ�.
        iCal = (Calendar) m_Calendar.clone();

        // / ��¥�� ��~
        m_selDay = iCal.get(Calendar.DATE);

        // / ��¥�� 1�� �����Ͽ� ���� 1���� ���� �������� ����
        iCal.set(Calendar.DATE, 1);
        // / ����ǥ���ϴ� �� �� 7ĭ + ������ ù �����ϴ� ĭ��
        m_starPos = COLS + iCal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

        // / 1�� ���ؼ� ������ 1�Ϸ� ������ٰ� 1���� ���� ���� ���������� ������
        iCal.add(Calendar.MONTH, 1);
        iCal.add(Calendar.DATE, -1);

        m_lastDay = iCal.get(Calendar.DAY_OF_MONTH); // / �ش� ���� �������� ��~

        // / 0���� 6��ĭ������ ��ȭ���������~ �� ä������
        for (int k = 0; k < COLS; k++) {
            // m_cellTextBtn[k].setText(m_dayText[k % 7]);
        }

        // / 7������ ó�� ������ġ �������� �������� ä��
        for (int i = COLS; i < m_starPos; i++) {
            m_cellTextBtn[i].setText("");
            //m_cellTextBtn[i].setBackground(null);
        }

        // / ������ġ���ʹ� 1���� �ؼ� ���� ������������ ���ڷ� ä��
        m_ratingAvg = 0;
        m_sum = 0;
        for (int i = 0; i < m_lastDay; i++) {
            //Log.v("position", "" + m_lastDay);
            // m_cellTextBtn[i + m_starPos].setBackground(null);
            m_cellTextBtn[i + m_starPos].setText((i + 1) + "");

//            for (int j = 0; j < list.size(); j++) {
//                int get_month = list.get(j).get_MONTH();
//                int now_month = iCal.get(Calendar.MONTH) + 1;
//                if (iCal.get(Calendar.YEAR) == list.get(j).get_YEAR()
//                        && list.get(j).get_DAY() == i + 1
//                        && get_month == now_month) {
//
//                    // Log.d("years", "" + get_month + " / " + now_month);
//                    m_cellTextBtn[i + m_starPos]
//                            .setBackgroundResource(R.drawable.poo);
//                    m_sum++;
//                    m_ratingAvg = m_ratingAvg
//                            + Float.valueOf(list.get(j).get_RATING());
//
//                }//       }
            // Log.d("1234", "" + m_sum);

        }

        m_nextDay = 1;
        // / ������������ �������� �������� ä��
        for (
                int i = m_starPos + m_lastDay;
                i < COLS * ROWS; i++)

        {

            m_cellTextBtn[i].setText("");
            // m_cellTextBtn[i].setBackground(null);

        }

    }

    // / �� ��ư�鿡 setOnClickListener �ֱ�
    public void setOnEvent() {
        // / ��ȭ��������� ���ִ� �κп��� ������ ������ �ʿ� ����
        for (int i = COLS; i < COLS * ROWS; i++) {
            final int k = i;
            m_cellTextBtn[i].setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (m_cellTextBtn[k].getText().toString().length() > 0) {
                        m_Calendar.set(Calendar.DATE,
                                Integer.parseInt(m_cellTextBtn[k].getText()
                                        .toString()));
                        if (m_dayTv != null)
                            m_dayTv.setText(m_Calendar
                                    .get(Calendar.DAY_OF_MONTH) + "");
                        printView();
                        myClickEvent(m_Calendar.get(Calendar.YEAR),
                                m_Calendar.get(Calendar.MONTH),
                                m_Calendar.get(Calendar.DAY_OF_MONTH),
                                m_Calendar.get(Calendar.DAY_OF_WEEK));

                    }
                }
            });
        }
    }

    // / �޷��� ��� ���� �� �� ���� �������
    public void printView() {
        // / �ؽ�Ʈ ����� ������ �� �ؽ�Ʈ �信�ٰ� �� �� ���� �������
        if (m_yearTv != null)
            m_yearTv.setText(m_Calendar.get(Calendar.YEAR) + "");
        if (m_mothTv != null) {
            // int imonth = iCal.get( Calendar.MONTH ) ;
            m_mothTv.setText((m_Calendar.get(Calendar.MONTH) + 1) + "");
        }
        if (m_dayTv != null)
            m_dayTv.setText(m_Calendar.get(Calendar.DAY_OF_MONTH) + "");

    }

    // / �⵵�� ���� ��~ ��~��
    public void preYear() {
        m_Calendar.add(Calendar.YEAR, -1);
        // updateList(list);
        setContentext();
        printView();
    }

    public void nextYear() {
        m_Calendar.add(Calendar.YEAR, 1);
        // updateList(list);
        setContentext();
        printView();
    }

    public void preMonth() {
        m_Calendar.add(Calendar.MONTH, -1);
        // updateList(list);
        setContentext();
        printView();
    }

    public void nextMonth() {
        m_Calendar.add(Calendar.MONTH, 1);
        // updateList(list);
        setContentext();
        printView();
    }

    // / �ؽ�Ʈ�並 �־��ָ� ���� �ѷ��� (��� ��������� �ȻѸ�)
    public void setViewTarget(TextView[] tv) {
        m_yearTv = tv[0];
        m_mothTv = tv[1];
        m_dayTv = tv[2];
    }

    // / ��ư�� �־��ָ� �˾Ƽ� �ɼ� �־��� (���ó� ��� ������ �̺�Ʈ �ȳ���)
    public void setControl(Button[] btn) {
        m_preYearBtn = btn[0];
        m_nextYearBtn = btn[1];
        m_preMonthBtn = btn[2];
        m_nextMonthBtn = btn[3];

        if (m_preYearBtn != null)
            m_preYearBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preYear();
                }
            });
        if (m_nextYearBtn != null)
            m_nextYearBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextYear();
                }
            });
        if (m_preMonthBtn != null)
            m_preMonthBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preMonth();

                }
            });
        if (m_nextMonthBtn != null)
            m_nextMonthBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextMonth();

                }
            });
    }

    // / ���ϴ� ������ ��¥�� ������
    // / ��)
    // / String today = getData( "yyyy-MM-dd" )�̷���?
    public String getData(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(new Date(m_Calendar.getTimeInMillis()));
    }

    // / �޷¿��� ��¥�� Ŭ���ϸ� �� �Լ��� �θ���.
    public void myClickEvent(int yyyy, int MM, int dd, int day_week) {
//        for (int i = 0; i < list.size(); i++) {
//            // Log.d("asdfa", )
//            if (list.get(i).get_YEAR() == yyyy
//                    && list.get(i).get_MONTH() == MM + 1
//                    && list.get(i).get_DAY() == dd) {
//
//                Intent intent = new Intent(context, SaveActivity.class);
//                intent.putExtra("id", list.get(i).get_ID());
//                intent.putExtra("yyyy", yyyy);
//                intent.putExtra("mm", MM);
//                intent.putExtra("dd", dd);
//                intent.putExtra("week", day_week);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);
//                new MainActivity().finish();
//                return;
//            }
//
//        }
//        Intent intent = new Intent(context, SaveActivity.class);
//        intent.putExtra("yyyy", yyyy);
//        intent.putExtra("mm", MM);
//        intent.putExtra("dd", dd);
//        intent.putExtra("week", day_week);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
//        new MainActivity().finish();
        Intent intent = new Intent(context, DayActivity.class);
        context.startActivity(intent);
    }

    public int pixelToDip(int arg) {
        m_displayScale = context.getResources().getDisplayMetrics().density;
        return (int) (arg * m_displayScale);
    }

    public gsCalendarColorParam getBasicColorParam() {
        return new gsCalendarColorParam();
    }

//    public void updateList(ArrayList<Item> list) {
//
//        this.list = list;
//    }

    public int getMonthAllDay() {

        return m_lastDay;
    }

    public int getCount() {
        return m_sum;
    }

    public int getAvg() {
        return Integer.valueOf((int) ((Float.valueOf(m_sum) / Float
                .valueOf(m_lastDay)) * 100));
    }

    public float getratingAvg() {
        return m_ratingAvg / m_sum;

    }
}
