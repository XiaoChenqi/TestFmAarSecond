package com.facilityone.wireless.componentservice.chart;

import android.os.Bundle;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

import java.util.List;

/**
 * Created by: owen.
 * Date: on 2018/11/13 下午5:04.
 * Description:
 * email:
 */

public interface ChartService extends IService {
    BaseFragment getProjectChartFragment(List<Long> projectIds,boolean single);
}
