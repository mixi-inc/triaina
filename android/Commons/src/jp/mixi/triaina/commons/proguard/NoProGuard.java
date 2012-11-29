package jp.mixi.triaina.commons.proguard;

/**
 * Keep a class from being processed by Proguard.
 * You will need the configuration in your proguard-project.txt.
 * 
 * -keep class * implements jp.mixi.triaina.commons.proguard.KeepClass {
 *   *;
 *  }
 *  
 * @author hnakagawa
 */
public interface NoProGuard {
}
