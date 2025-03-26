package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ItemUserListBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.interfaces.UserListInterface;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.TrayModel;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TrayModel> trayModelArrayList;
    private UserListInterface userListInterface;

    public UserListAdapter(Context context2, ArrayList<TrayModel> arrayList, UserListInterface userListInterface2) {
        this.context = context2;
        this.trayModelArrayList = arrayList;
        this.userListInterface = userListInterface2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemUserListBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_user_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.binding.realName.setText(this.trayModelArrayList.get(i).getUser().getFull_name());
        Glide.with(this.context).load(this.trayModelArrayList.get(i).getUser().getProfile_pic_url()).thumbnail(0.2f).into(viewHolder.binding.storyIcon);
        viewHolder.binding.RLStoryLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                UserListAdapter.this.userListInterface.userListClick(i, (TrayModel) UserListAdapter.this.trayModelArrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        ArrayList<TrayModel> arrayList = this.trayModelArrayList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserListBinding binding;

        public ViewHolder(ItemUserListBinding itemUserListBinding) {
            super(itemUserListBinding.getRoot());
            this.binding = itemUserListBinding;
        }
    }
}