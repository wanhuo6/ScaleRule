package playfire.top.scalerule.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import playfire.top.scalerule.R;
import playfire.top.scalerule.utils.CommonUtils;
import playfire.top.scalerule.widget.HorizontalScaleScrollView;

public class MainActivity extends AppCompatActivity {

    private TextView mTvHorizontalValue;
    private HorizontalScaleScrollView mHorizontalScaleScrollView;
    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

    }


    private void initView() {
        mHorizontalScaleScrollView= (HorizontalScaleScrollView) findViewById(R.id.horizontal_scale_view);
        mTvHorizontalValue= (TextView) findViewById(R.id.tv_horizontal_value);

        mHorizontalScaleScrollView.setOnScrollListener(new HorizontalScaleScrollView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                double value=scale;
                mTvHorizontalValue.setText(String.valueOf(value/2));
            }
        });

    }

    private void initData() {
        mScreenWidth = CommonUtils.getScreenWidthpx(this);
        mHorizontalScaleScrollView.setDefaultNumber(170, mScreenWidth);//设置默认选择刻度

    }
}
