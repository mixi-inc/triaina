package jp.mixi.triaina.commons.proguard;

/**
 * Keep class from Proguard
 * You need following config in proguard.cfg
 * 
 * -keep class * implements jp.mixi.android.commons.proguard.KeepClass {
 *   *;
 *  }
 *  
 * @author hnakagawa
 */
public interface NoProGuard {
}
