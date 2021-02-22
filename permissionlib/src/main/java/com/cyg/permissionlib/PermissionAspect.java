package com.cyg.permissionlib;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.cyg.permissionlib.annotation.PermissionCancel;
import com.cyg.permissionlib.annotation.PermissionDenied;
import com.cyg.permissionlib.annotation.PermissionGranted;
import com.cyg.permissionlib.annotation.PermissionRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect {

    /**
     * 声明切入点
     * JointPoint 类型有：Method Call，Method execution，Constructor call，
     * Constructor execution, Field get, Field set, Handler
     * JointPoint                   PointCut 表达式：
     * Method Call ->               call(methodPattern)
     * Method execution ->          execution(methodPattern)
     * Constructor call ->          call(ConstructorPattern)
     * Constructor execution        execution(ConstructorPattern)
     * Static initialization        staticInitialization(TypePattern)
     * Field get                    get(FieldPattern)
     * Field set                    set(FieldPattern)
     * Handler                      handler(TypePattern)
     * <p>
     * Pattern 类型：
     * MethodPattern： [!][@Annotation][public/protected/private][static/final]返回值类型[类名.]方法名(参数类型列表)[throws 异常类型]
     * ConstructorPattern [!][@Annotation][public/protected/private][static/final][类名.]new(参数类型列表)[throws 异常类型]
     * FieldPattern [!][@Annotation][public/protected/private][static/final]属性类型[类名.]属性名
     * TypePattern 其他 Pattern 涉及到的类型规则也是一样，可以使用 '!'、''、'..'、'+'，'!' 表示取反，
     * '' 匹配除 . 外的所有字符串，'*' 单独使用事表示匹配除了 '.' 以外的任意类型，'..' 表示任意子 package，
     * '..' 单独使用时表示匹配任意长度任意类型，'+' 匹配其自身及子类，还有一个 '...'表示不定个数
     *
     * @param permissionRequest
     */
    @Pointcut("execution(@com.cyg.permissionlib.annotation.PermissionRequest * *(..))&& @annotation(permissionRequest)")
    public void getPermission(PermissionRequest permissionRequest) {
    }

    @Around("getPermission(permissionRequest)")
    public void handleRequestPermission(final ProceedingJoinPoint point, PermissionRequest permissionRequest) throws Throwable {
        Log.e("test", "handleRequestPermission");
        Context context = null;
        //获取当前对象
        final Object obj = point.getThis();
        if (obj instanceof Context) {
            context = (Context) obj;
        } else if (obj instanceof Fragment) {
            context = ((Fragment) obj).getContext();
        }
        String[] permissions = permissionRequest.value();
        if (context == null || permissions.length == 0) {
            return;
        }
        final int requestCode = permissionRequest.requestCode();
        PermissionUtil.launchActivity(context, requestCode, permissions, new PermissionRequestCallback() {

            @Override
            public void permissionGranted() {
                try {
                    point.proceed();
                    PermissionUtil.invokeAnnotation(obj, PermissionGranted.class, requestCode);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void permissionDenied(int requestCode) {
                PermissionUtil.invokeAnnotation(obj, PermissionDenied.class, requestCode);
            }

            @Override
            public void permissionCancel(int requestCode) {
                PermissionUtil.invokeAnnotation(obj, PermissionCancel.class, requestCode);
            }
        });
    }
}
