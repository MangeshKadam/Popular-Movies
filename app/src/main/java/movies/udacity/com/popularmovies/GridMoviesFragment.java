package movies.udacity.com.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieDetail;
import movies.udacity.com.popularmovies.network.MovieList;
import movies.udacity.com.popularmovies.uiutils.MovieDetailAdapter;
import movies.udacity.com.popularmovies.uiutils.SpacesItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridMoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridMoviesFragment extends Fragment implements MovieDetailAdapter.OnMovieClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    RecyclerView mRecyclerView;

    MovieDetailAdapter adapter;

    private View mView;

    public GridMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridMoviesFragment newInstance(String param1, String param2) {
        GridMoviesFragment fragment = new GridMoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_grid_movies, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.moivies_grid);
        initRecyclerView();
        getMovieList(Constants.ORDER_POPULARITY);
        return mView;
    }

    private void getMovieList(final String order) {
        BaseClient.getInstance().getMoviesList(order, new APICallBack<MovieList>() {
            @Override
            public void success(MovieList movieList) {
                if (adapter == null) {
                    adapter = new MovieDetailAdapter(getActivity(), GridMoviesFragment.this, movieList.getResults());
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.setMovieDetailsList(movieList.getResults());
                    adapter.notifyDataSetChanged();
                }
                if (TextUtils.equals(order, Constants.ORDER_POPULARITY)) {
                    Constants.isOrderedByPopularity = true;
                } else {
                    Constants.isOrderedByPopularity = false;
                }
            }

            @Override
            public void error(APIError error) {
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(10, 2);
        mRecyclerView.addItemDecoration(decoration);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMovieClicked(MovieDetail movieDetail) {
        mListener.onFragmentInteraction(movieDetail);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(MovieDetail movieDetail);
    }
}