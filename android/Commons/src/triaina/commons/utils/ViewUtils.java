package triaina.commons.utils;

import android.view.View;
import android.widget.ViewAnimator;

/**
 * An utility class for handling view components.
 * 
 * @author yuki.fujisaki
 * 
 */
public class ViewUtils {

	/**
	 * Hide a view of specified id from the layout. If no view associated to id,
	 * nothing happens.
	 * 
	 * @param layout
	 *            parent ViewGroup.
	 * @param id
	 *            Layout ID to be hidden.
	 */
	public static void hideView(View layout, int id) {
		View v = layout.findViewById(id);
		if (v != null)
			v.setVisibility(View.GONE);
	}
	
	/**
	 * Set the displayed child of an animator view by the view resource ID.
	 * If the animator has no child with the given resource ID, nothing happens.
	 * @param animator the animator the view to display is in. Must not be null
	 * @param viewResource the resource ID of the view to display
	 */
	public static void setAnimatorDisplayedChild (final ViewAnimator animator, final int viewResource) {
		final View view = animator.findViewById(viewResource);
		if (view != null) {
			final int viewIndexInParent = animator.indexOfChild(view);
			if (viewIndexInParent >= 0)
				animator.setDisplayedChild(viewIndexInParent);
		}
	}

}
