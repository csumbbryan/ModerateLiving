package com.example.moderateliving;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.UserID;

import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Handles RecyclerView for user objects (UserID)
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserHolder> {

  View view;
  private final UserRecyclerViewInterface recyclerViewInterface;
  private Context context;
  private List<UserID> users;
  static int mSelectedItemPosition = RecyclerView.NO_POSITION;

  public UserRecyclerViewAdapter(Context context, List<UserID> users, UserRecyclerViewInterface recyclerViewInterface) {
    this.context = context;
    this.users = users;
    this.recyclerViewInterface = recyclerViewInterface;
  }

  @NonNull
  @Override
  public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
    return new UserHolder(view);
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  @Override
  public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
    UserID user = users.get(position);
    userHolder.displayUsers(user, this);
  }

  public class UserHolder extends RecyclerView.ViewHolder {
    TextView mTextViewUserManagementUsernameDisplay;
    TextView mTextViewUserManagementNameDisplay;
    TextView mTextViewUserManagementYesNo;
    TextView mTextViewUserManagementWeight;
    TextView mTextViewUserManagementBirthday;
    TextView mTextViewUserManagementPoints;


    public UserHolder (@NonNull View itemView) {
      super(itemView);
      mTextViewUserManagementUsernameDisplay = itemView.findViewById(R.id.textViewUserManagementUsernameDisplay);
      mTextViewUserManagementNameDisplay = itemView.findViewById(R.id.textViewUserManagementNameDisplay);
      mTextViewUserManagementYesNo = itemView.findViewById(R.id.textViewUserManagementYesNo);
      mTextViewUserManagementWeight = itemView.findViewById(R.id.textViewUserManagementWeightDisplay);
      mTextViewUserManagementBirthday = itemView.findViewById(R.id.textViewUserManagementBirthdayDisplay);
      mTextViewUserManagementPoints = itemView.findViewById(R.id.textViewUserManagementPointsDisplay);
    }

    public void displayUsers(UserID user, UserRecyclerViewAdapter mUserRecyclerViewAdapter) {
      String yesNo = user.getIsAdmin() ? "yes" : "no";
      String points = user.getPoints() + (user.getPoints() == 1 ? " Pt" : " Pts");
      this.mTextViewUserManagementUsernameDisplay.setText(user.getUsername());
      this.mTextViewUserManagementNameDisplay.setText(user.getName());
      this.mTextViewUserManagementYesNo.setText(yesNo);
      this.mTextViewUserManagementWeight.setText(String.valueOf(user.getWeight()));
      this.mTextViewUserManagementBirthday.setText(user.getBirthday());
      this.mTextViewUserManagementPoints.setText(points);
      this.itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
      if (mSelectedItemPosition == RecyclerView.NO_POSITION) {
        //mBackgroundColor = DEFAULT_COLOR;
        this.itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
      } else {
        if (mSelectedItemPosition == this.getAbsoluteAdapterPosition()) {
          //mBackgroundColor = SELECTED_COLOR;
          this.itemView.setBackgroundColor(context.getResources().getColor(R.color.yellowLight, null));
          recyclerViewInterface.clickToSelect(mSelectedItemPosition);
        } else {
          //mBackgroundColor = DEFAULT_COLOR;
          this.itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
        }
      }

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //logHolder.mBackgroundColor = LogHolder.SELECTED_COLOR;
          itemView.setBackgroundColor(context.getResources().getColor(R.color.yellowLight, null));
          //isSelected = true;
          mUserRecyclerViewAdapter.notifyItemChanged(getAbsoluteAdapterPosition());
          if(UserRecyclerViewAdapter.mSelectedItemPosition != getAbsoluteAdapterPosition()) {
            //logHolder.mBackgroundColor = LogHolder.DEFAULT_COLOR;
            itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
            mUserRecyclerViewAdapter.notifyItemChanged(UserRecyclerViewAdapter.mSelectedItemPosition);
            UserRecyclerViewAdapter.mSelectedItemPosition = getAbsoluteAdapterPosition();
            //isSelected = false;
          }
          //notifyItemChanged(selectedItemPosition);
        }
      });

    }

  }

}
