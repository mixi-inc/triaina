package triaina.commons.workerservice;

import android.content.Context;
import android.os.Handler;

public interface Abortable {
	public void confirm(Context context, Handler handler);
	
	public void onAbort(Context context, Handler handler);
}
