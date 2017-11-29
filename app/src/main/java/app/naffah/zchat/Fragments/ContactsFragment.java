package app.naffah.zchat.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import app.naffah.zchat.Activities.TabView;
import app.naffah.zchat.Adapters.ContactAdapter;
import app.naffah.zchat.BuildConfig;
import app.naffah.zchat.R;

/**
 * Created by Naffah Amin on 11/17/2017.
 */

public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int REQUEST_PERMISSION = 2001;
    private FloatingActionButton addContact;
    private static final int LOADER_ID = 1;
    private RecyclerView contactsRecycler;
    private ContactAdapter mContactAdapter;
    private View view;

    private static final String[] FROM_COLUMNS = {
            ContactsContract.Data.CONTACT_ID,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Data.DISPLAY_NAME_PRIMARY: ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_ID
    };
    private Bundle mBundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contacts_fragment, container, false);

        mBundle = savedInstanceState;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSION
            );
        }
        else
        {
            getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);
        }

        init();

        addContact = view.findViewById(R.id.addContactBtn);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION)
        {
            if (grantResults.length != 0)
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getLoaderManager().initLoader(LOADER_ID, mBundle, this);
                }
                else
                {
                    getActivity().finish();
                }
            }
        }
    }

    private void init() {
        contactsRecycler = (RecyclerView) view.findViewById(R.id.contacts_fragment_recycler);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRecycler.setHasFixedSize(true);

        mContactAdapter = new ContactAdapter(getContext(), null, ContactsContract.Data.CONTACT_ID);
        contactsRecycler.setAdapter(mContactAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id)
        {
            case LOADER_ID:
                return new CursorLoader(
                        getContext(),
                        ContactsContract.Data.CONTENT_URI,
                        FROM_COLUMNS,
                        null,
                        null,
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.Data.DISPLAY_NAME_PRIMARY: ContactsContract.Data.
                                DISPLAY_NAME) +
                                " ASC"
                );
            default:
                if (BuildConfig.DEBUG)
                    throw new IllegalArgumentException("ID Not handled");
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactAdapter.swapCursor(null);
    }


}
