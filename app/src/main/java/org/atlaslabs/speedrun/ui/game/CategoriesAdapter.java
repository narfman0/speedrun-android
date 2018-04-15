package org.atlaslabs.speedrun.ui.game;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Category;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

class CategoriesAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private final PublishSubject<Category> onClickSubject = PublishSubject.create();
    private final List<Category> categories;

    CategoriesAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(category));
        holder.binding.categoryName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public Observable<Category> getClickedCategories(){
        return onClickSubject;
    }
}
