package com.nipunapps.cognent_user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nipunapps.cognent_user.R;
import com.nipunapps.cognent_user.adapters.ProductListAdapter;
import com.nipunapps.cognent_user.databinding.FragmentProductListBinding;
import com.nipunapps.cognent_user.utils.Product;
import com.nipunapps.cognent_user.viewmodels.ProductListViewModel;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding productListBinding;
    private ProductListViewModel productListViewModel;
    private ProductListAdapter productListAdapter;
    private NavController navController;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        productListBinding = FragmentProductListBinding.inflate(inflater, container, false);
        productListViewModel = new ViewModelProvider(
                requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(ProductListViewModel.class);
        return productListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        productListAdapter = new ProductListAdapter(requireContext());
        productListBinding.productRecyclerView.setAdapter(productListAdapter);
        productListAdapter.setOnItemClick(pid -> {
//            Bundle bundle = new Bundle();
//            bundle.putString("pid",pid);
            productListViewModel.setSelectedProduct(pid);
            view.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.from_right_exit));
            navController.navigate(R.id.goToProductDetails);
        });
        productListBinding.productRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        subscribeToObserver();
    }

    private void subscribeToObserver() {
        productListViewModel.isLoading.observe(getViewLifecycleOwner(), (result) -> {
            productListBinding.progressBar.setVisibility(
                    (result) ? View.VISIBLE : View.GONE
            );
        });

        productListViewModel.products.observe(getViewLifecycleOwner(), (result) -> {
            productListAdapter.setProducts(result);
        });
    }
}