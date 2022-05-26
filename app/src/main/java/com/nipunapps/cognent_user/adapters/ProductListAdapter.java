package com.nipunapps.cognent_user.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nipunapps.cognent_user.R;
import com.nipunapps.cognent_user.databinding.ProductRecylclerLayoutBinding;
import com.nipunapps.cognent_user.interfaces.OnItemClick;
import com.nipunapps.cognent_user.utils.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private ArrayList<Product> products = new ArrayList<>();
    private Context context;

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ProductListAdapter(Context context) {
        this.context = context;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products.clear();
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ProductRecylclerLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @SuppressLint({"StringFormatInvalid", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productRecylclerLayoutBinding.productName.setText(product.getName());
        holder.productRecylclerLayoutBinding.discountPrice.setText(
                "\u20B9 " + product.getDiscount_price()
        );
        Glide.with(context).load(Uri.parse(product.getImage1())).centerInside().into(holder.productRecylclerLayoutBinding.productImage);
        holder.productRecylclerLayoutBinding.discountPercent.setText(
                getOffString(product.getDiscount_price(), product.getOriginal_price())
        );
        holder.productRecylclerLayoutBinding.actualPrice.setText(
                "\u20B9 " + product.getOriginal_price()
        );
        holder.productRecylclerLayoutBinding
                .actualPrice.setPaintFlags(holder.productRecylclerLayoutBinding.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productRecylclerLayoutBinding.getRoot().setOnClickListener(v -> {
            onItemClick.onItemClick(position);
        });
    }

    private String getOffString(int discount, int actual) {
        double d = ((double) discount / (double) actual) * 100;
        int percent = (100 - (int) Math.round(d));
        return percent + "% off";
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ProductRecylclerLayoutBinding productRecylclerLayoutBinding;

        public ViewHolder(ProductRecylclerLayoutBinding productRecylclerLayoutBinding) {
            super(productRecylclerLayoutBinding.getRoot());
            this.productRecylclerLayoutBinding = productRecylclerLayoutBinding;
        }
    }
}
