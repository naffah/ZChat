package app.naffah.zchat.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.naffah.zchat.Activities.ContactDetails;
import app.naffah.zchat.PhoneAuth.PhoneAuthToTabView;
import app.naffah.zchat.R;

/**
 * Created by Naffah Amin on 11/29/2017.
 */

public class ContactAdapter extends CursorRecyclerViewAdapter<ContactAdapter.ContactsViewHolder> {

    public ContactAdapter(Context context, Cursor cursor, String id) {
        super(context, cursor, id);
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_fragment_list_row,
                parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder viewHolder, Cursor cursor) {
        String username = cursor.getString(cursor.getColumnIndex(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data
                        .DISPLAY_NAME
        ));

        viewHolder.setContactUsername(username);
        long contactId = getItemId(cursor.getPosition());
        long photoId = cursor.getLong(cursor.getColumnIndex(
                ContactsContract.Data.PHOTO_ID
        ));

        if (photoId != 0) {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    contactId);
            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                    .CONTENT_DIRECTORY);
            viewHolder.imageViewContactDisplay.setImageURI(photoUri);
        } else
            viewHolder.imageViewContactDisplay.setImageResource(R.drawable.dummy_avatar);
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContactUsername;
        ImageView imageViewContactDisplay;

        public ContactsViewHolder(final View itemView) {
            super(itemView);

            textViewContactUsername = (TextView) itemView.findViewById(R.id.text_view_contact_username);
            imageViewContactDisplay = (ImageView) itemView.findViewById(R.id.image_view_contact_display);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Goes to contact details activity",
                            Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(itemView.getContext(), "Long click working", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }


        public void setContactUsername(String username) {
            textViewContactUsername.setText(username);
        }
    }
}
