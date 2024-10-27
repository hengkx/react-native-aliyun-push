/**
 * Copyright (c) 2017-present, Wonday (@wonday.org)
 * All rights reserved.
 * <p>
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

 package org.wonday.aliyun.push;

 import androidx.annotation.NonNull;
 
 import com.facebook.react.bridge.ReactApplicationContext;
 import com.facebook.react.bridge.ReactContextBaseJavaModule;
 import com.facebook.react.bridge.ReactMethod;
 import com.facebook.react.bridge.Callback;
 import com.facebook.react.bridge.LifecycleEventListener;
 import com.facebook.react.bridge.ReadableArray;
 import com.facebook.react.bridge.Promise;
 import com.facebook.react.common.ReactConstants;
 import com.facebook.common.logging.FLog;
 
 import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
 import com.alibaba.sdk.android.push.CommonCallback;
 
 
 public class AliyunPushModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
   private final ReactApplicationContext context;
   private int badgeNumber;
 
   public AliyunPushModule(ReactApplicationContext reactContext) {
     super(reactContext);
     this.context = reactContext;
     this.badgeNumber = 0;
     AliyunPushMessageReceiver.context = reactContext;
     ThirdPartMessageActivity.context = reactContext;
 
     context.addLifecycleEventListener(this);
 
   }
 
   //module name
   @NonNull
   @Override
   public String getName() {
     return "AliyunPush";
   }
 
   @ReactMethod
   public void getDeviceId(final Promise promise) {
     String deviceID = PushServiceFactory.getCloudPushService().getDeviceId();
     if (deviceID != null && !deviceID.isEmpty()) {
       promise.resolve(deviceID);
     } else {
       // 或许还没有初始化完成，等3秒钟再次尝试
       try {
         Thread.sleep(3000);
         deviceID = PushServiceFactory.getCloudPushService().getDeviceId();
 
         if (deviceID != null && !deviceID.isEmpty()) {
           promise.resolve(deviceID);
           return;
         }
       } catch (Exception e) {
 
       }
 
       promise.reject("-1", "getDeviceId() failed.");
     }
   }
 
   @ReactMethod
   public void setApplicationIconBadgeNumber(int badgeNumber, final Promise promise) {
 
     FLog.d(ReactConstants.TAG, "setApplicationIconBadgeNumber for normal");
     try {
       PushServiceFactory.getCloudPushService().setBadgeNum(this.context, badgeNumber);
       this.badgeNumber = badgeNumber;
       promise.resolve("");
     } catch (Exception e) {
       promise.reject(e.getMessage());
     }
 
   }
 
   @ReactMethod
   public void getApplicationIconBadgeNumber(Callback callback) {
     callback.invoke(this.badgeNumber);
   }
 
   @ReactMethod
   public void bindAccount(String account, final Promise promise) {
     PushServiceFactory.getCloudPushService().bindAccount(account, new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void unbindAccount(final Promise promise) {
     PushServiceFactory.getCloudPushService().unbindAccount(new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void bindTag(int target, ReadableArray tags, String alias, final Promise promise) {
 
     String[] tagStrs = new String[tags.size()];
     for (int i = 0; i < tags.size(); i++) tagStrs[i] = tags.getString(i);
 
     PushServiceFactory.getCloudPushService().bindTag(target, tagStrs, alias, new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void unbindTag(int target, ReadableArray tags, String alias, final Promise promise) {
 
     String[] tagStrs = new String[tags.size()];
     for (int i = 0; i < tags.size(); i++) tagStrs[i] = tags.getString(i);
 
     PushServiceFactory.getCloudPushService().unbindTag(target, tagStrs, alias, new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void listTags(int target, final Promise promise) {
     PushServiceFactory.getCloudPushService().listTags(target, new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void addAlias(String alias, final Promise promise) {
     PushServiceFactory.getCloudPushService().addAlias(alias, new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void removeAlias(String alias, final Promise promise) {
     PushServiceFactory.getCloudPushService().removeAlias(alias, new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }
 
   @ReactMethod
   public void listAliases(final Promise promise) {
     PushServiceFactory.getCloudPushService().listAliases(new CommonCallback() {
       @Override
       public void onSuccess(String response) {
         promise.resolve(response);
       }
 
       @Override
       public void onFailed(String code, String message) {
         promise.reject(code, message);
       }
     });
   }

   @Override
   public void onHostResume() {
     if (getCurrentActivity() != null) {
       ThirdPartMessageActivity.mainClass = getCurrentActivity().getClass();
     } else {
       // 可以记录日志或进行其他处理
       FLog.d(ReactConstants.TAG, "Current activity is null in onHostResume");
     }
   }
   @Override
   public void onHostPause() {
   }
 
   @Override
   public void onHostDestroy() {
   }
 
   @ReactMethod
   public void getInitialMessage(final Promise promise) {
     promise.resolve(AliyunPushMessageReceiver.initialMessage);
   }
 
   @ReactMethod
   public void addListener(String eventName) {
     // Keep: Required for RN built in Event Emitter Calls.
   }
 
   @ReactMethod
   public void removeListeners(Integer count) {
     // Keep: Required for RN built in Event Emitter Calls.
   }
 }