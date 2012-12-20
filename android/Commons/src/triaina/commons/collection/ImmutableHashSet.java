package triaina.commons.collection;

import java.util.HashSet;
import java.util.Set;

/**
 *  ImmutableHashMap 
 *  The purpose of this class that define immutable this class instance for clarity 
 * @author hnakagawa
 * 
 * @param <E>
 */
public class ImmutableHashSet <E> extends AbstractImmutableSet<E> {
	public ImmutableHashSet(Set<E> set) {
		super(new HashSet<E>(set));
	}
}
