package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.content.Context;

import java.lang.reflect.Field;

import mortar.MortarScope;

class UpdateMortarScopeUtil {
    private static Class<?> contextWrapperClazz;
    private static Field mortarScopeField;

   static void updateMortarScopeForContext(Context context, MortarScope scope) {
       try {
           if (contextWrapperClazz == null) {
               contextWrapperClazz = Class.forName("mortar.MortarContextWrapper");
               mortarScopeField = contextWrapperClazz.getDeclaredField("scope");
               mortarScopeField.setAccessible(true);
           }
           if (!contextWrapperClazz.isInstance(context)) return;
           mortarScopeField.set(context, scope);

       } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
           throw new RuntimeException(e);
       }
   }
}
