package com.nipunapps.cognent_user.fragments;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.nipunapps.cognent_user.databinding.FragmentProductDetailsBinding;
import com.nipunapps.cognent_user.utils.Product;
import com.nipunapps.cognent_user.viewmodels.ProductListViewModel;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding detailsBinding;
    private ProductListViewModel productListViewModel;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailsBinding = FragmentProductDetailsBinding.inflate(inflater, container, false);

        productListViewModel = new ViewModelProvider(
                requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(ProductListViewModel.class);
        observeFields();
        return detailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void observeFields() {
        productListViewModel.product.observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                initUi(result);
            }
        });
    }

    private void initUi(Product product) {
        Glide.with(requireContext()).load(Uri.parse(product.getImage1())).into(detailsBinding.productImage);
        detailsBinding.productName.setText(product.getName());
        detailsBinding.discountPercent.setText(
                getOffString(product.getDiscount_price(), product.getOriginal_price())
        );
        detailsBinding.discountPrice.setText(
                "\u20B9 "+product.getDiscount_price()
        );
        detailsBinding.actualPrice.setText(
                "\u20B9 " + product.getOriginal_price()
        );
        detailsBinding
                .actualPrice.setPaintFlags(detailsBinding.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private String getOffString(int discount, int actual) {
        double d = ((double) discount / (double) actual) * 100;
        int percent = (100 - (int) Math.round(d));
        return percent + "% off";
    }
}