package com.cyg.permissionlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cyg.permissionlib.annotation.PermissionCancel;
import com.cyg.permissionlib.annotation.PermissionDenied;
import com.cyg.permissionlib.annotation.PermissionGranted;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PermissionUtil {

    public static PermissionRequestCallback sCallback;

    public static void launchActivity(Context context, int requestCode, String[] permissions,
                                      PermissionRequestCallback callback) {
        sCallback = callback;
        Intent intent = new Intent(context, TransparentActivity.class);
        intent.putExtra(TransparentActivity.KEY_REQUEST_CODE, requestCode);
        intent.putExtra(TransparentActivity.KEY_PERMISSIONS, permissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean permissionGranted(int[] grantedResults) {
        for (int grantedResult : grantedResults) {
            if (grantedResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 反射调用被注解类修饰的方法
     *
     * @param object
     * @param annotationClass
     * @param requestCode
     */
    public static void invokeAnnotation(Object object, Class annotationClass, int requestCode) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);
            if (annotationPresent) {
                Annotation annotation = method.getAnnotation(annotationClass);
                int annotationRequestCode = -1;
                if (annotationClass == PermissionGranted.class) {
                    PermissionGranted permissionGranted = (PermissionGranted) annotation;
                    annotationRequestCode = permissionGranted.requestCode();
                } else if (annotationClass == PermissionDenied.class) {
                    PermissionDenied permissionDenied = (PermissionDenied) annotation;
                    annotationRequestCode = permissionDenied.requestCode();
                } else if (annotationClass == PermissionCancel.class) {
                    PermissionCancel permissionCancel = (PermissionCancel) annotation;
                    annotationRequestCode = permissionCancel.requestCode();
                }
                if (annotationRequestCode == requestCode) {
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
