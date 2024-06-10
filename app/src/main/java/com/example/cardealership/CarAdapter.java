package com.example.cardealership;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private OnItemClickListener onItemClickListener;
    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.textViewModel.setText(car.getModel());
        holder.textViewYear.setText("Год: " + car.getYear());
        holder.textViewPrice.setText("Цена: $" + car.getPrice());
        Glide.with(context)
                .load(car.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.rectangle)) // Заглушка для случая, если изображение не загружено
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Кэширование изображения на устройстве
                .into(holder.imageViewCar);

        // Добавление слушателя щелчков на элемент списка
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Открыть CarDetailsActivity при выборе элемента списка
                Intent intent = new Intent(context, CarDetailsActivity.class);
                intent.putExtra(CarDetailsActivity.EXTRA_CAR_MODEL, car.getModel());
                intent.putExtra(CarDetailsActivity.EXTRA_CAR_YEAR, car.getYear());
                intent.putExtra(CarDetailsActivity.EXTRA_CAR_PRICE, car.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
        notifyDataSetChanged(); // Уведомить адаптер о том, что данные изменились
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModel;
        TextView textViewYear;
        TextView textViewPrice;
        ImageView imageViewCar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModel = itemView.findViewById(R.id.textViewModel);
            textViewYear = itemView.findViewById(R.id.textViewYear);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewCar = itemView.findViewById(R.id.imageViewCar);
        }
    }
}
