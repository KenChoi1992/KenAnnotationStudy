package com.cyg.permissionlib;

import android.view.View;

import com.cyg.permissionlib.annotation.ClickInterval;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ClickAspect {

    private static long sLastClickTime = 0L;
    private View mLastView = null;

    @Pointcut("execution(@com.cyg.permissionlib.annotation.ClickInterval * android.view.View.OnClickListener+.onClick(..)) && @annotation(clickInterval)")
    public void click(ClickInterval clickInterval) {}

    @Around("click(clickInterval)")
    public void JudgeClickInterval(ProceedingJoinPoint joinPoint, ClickInterval clickInterval) {
        int clickIntervalTime = clickInterval.clickInterval();
        View target = (View) joinPoint.getArgs()[0];
        long currentTime = System.currentTimeMillis();
        if (currentTime - sLastClickTime > clickIntervalTime) {
            mLastView = target;
            sLastClickTime = currentTime;
            invoke(joinPoint);
        } else if (mLastView == null || target != mLastView) {
            mLastView = target;
            sLastClickTime = currentTime;
            invoke(joinPoint);
        }
    }

    private void invoke(ProceedingJoinPoint joinPoint) {
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
