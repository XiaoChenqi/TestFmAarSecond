package com.facilityone.wireless.componentservice.payment;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

import java.util.List;

/**
 * Created by: owen.
 * Date: on 2018/11/28 下午6:10.
 * Description:
 * email:
 */

public interface PaymentService extends IService{
    BaseFragment getPaymentInfoFragment(Long paymentId, String code, boolean edit);

    BaseFragment getPaymentCreateFragment(Long mWoId, LocationBean location);
}
