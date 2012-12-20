package triaina.commons.proguard;

/**
 * Keep a class from being processed by Proguard.
 * You will need the configuration in your proguard-project.txt.
 * 
 * -keep class * implements triaina.commons.proguard.NoProguard {
 *   *;
 *  }
 *  
 * @author hnakagawa
 */
public interface NoProGuard {
}
