package triaina.injector.fragment;

import triaina.injector.TriainaApplication;
import triaina.injector.TriainaInjector;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class AbstractTriainaFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TriainaInjector injector = ((TriainaApplication)getActivity().getApplication()).getInjector(getActivity());
    	injector.injectMembersWithoutViews(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TriainaInjector injector = ((TriainaApplication)getActivity().getApplication()).getInjector(getActivity());
        injector.injectViewMembers(this);
    }
}
