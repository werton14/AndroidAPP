package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ImageDownloader {

    private FirebaseInfo firebaseInfo;
    private int unDownloadedData = 0;
    private int unUpdatedViews = 0;
    private int unCompletedTasks = 0;
    private OnDataDownloadedListener onDataDownloadedListener;
    private OnSuccessListener<Void> onTimeStampUpdated;


    public ImageDownloader(){

        firebaseInfo = FirebaseInfo.getInstance();

        onTimeStampUpdated = new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                unUpdatedViews--;
                if(unUpdatedViews == 0 && unCompletedTasks > 0){
                    getData();
                }
            }
        };

    }

    public void findImage(){
        if(unUpdatedViews == 0){
            getData();
        }else {
            unCompletedTasks++;
        }
    }

    private void getData(){
        firebaseInfo.getViewsDbReference().orderByChild("time").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<DataSnapshot> snapshots = transformToDataSnapshotList(iterator);
                sortDataSnapshots(snapshots);

                List<DatabaseReference> imageDbReferenceList = new ArrayList<DatabaseReference>();
                ImageData data = new ImageData();

                for (int i = 0; i < snapshots.size() / 2; i++) {
                    unDownloadedData++;
                    unUpdatedViews++;
                    DatabaseReference imageViewsDbReference = snapshots.get(i).getRef();
                    data.addImageId(imageViewsDbReference.getKey());
                    updateImageViews(imageViewsDbReference);
                    imageDbReferenceList.add(getImageDbReference(imageViewsDbReference));
                }

                getImage(imageDbReferenceList, data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImage(List<DatabaseReference> imageDbReferenceList, final ImageData data){
        for(DatabaseReference imageDbReference: imageDbReferenceList) {
            imageDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Image image = dataSnapshot.getValue(Image.class);
                    data.addImage(image);
                    getUser(image, data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getUser(final Image image, final ImageData data){
        firebaseInfo.getUsersDbReference().child(image.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                data.addUser(user);

                getImageUri(image, user, data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImageUri(Image image, final User user, final ImageData data){
        firebaseInfo.getImagesSReference().child(image.getCompetitiveImageFileName()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                data.addImageUri(uri);

                getProfileUri(user, data);
            }
        });
    }

    private void getProfileUri(User user, final ImageData data){
        firebaseInfo.getProfileImagesSReference().child(user.getProfileImageFileName()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                data.addProfileUri(uri);
                unDownloadedData--;
                if(unDownloadedData == 0){
                    onDataDownloadedListener.onDataDownloaded(data);
                }
            }
        });
    }

    private void iterateImageViews(DatabaseReference imageViewsDbReference){
        imageViewsDbReference.child("view").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long viewsCount = (long) mutableData.getValue();
                viewsCount++;
                mutableData.setValue(viewsCount);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private List<DataSnapshot> transformToDataSnapshotList(Iterator<DataSnapshot> iterator){
        List<DataSnapshot> snapshots = new ArrayList<DataSnapshot>();
        while (iterator.hasNext()){
            snapshots.add(iterator.next());
        }
        return snapshots;
    }

    private void sortDataSnapshots(List<DataSnapshot> snapshots){
        Collections.sort(snapshots, new Comparator<DataSnapshot>() {
            @Override
            public int compare(DataSnapshot o1, DataSnapshot o2) {
                return o1.child("view").getValue(long.class) < o2.child("view").getValue(long.class) ? -1 :
                        (o1.child("view").getValue(long.class) > o2.child("view").getValue(long.class)) ? 1 : 0;
            }
        });
    }

    private void updateImageViews(DatabaseReference imageViewsDbReference){
        imageViewsDbReference.child("time").setValue(ServerValue.TIMESTAMP)
                .addOnSuccessListener(onTimeStampUpdated);
        iterateImageViews(imageViewsDbReference);
    }

    private DatabaseReference getImageDbReference(DatabaseReference imageViewsDbReference){
        String str = imageViewsDbReference.getKey();
        return firebaseInfo.getImagesDbReference().child(str);
    }

    public void setOnDataDownloadedListener(OnDataDownloadedListener onDataDownloadedListener) {
        this.onDataDownloadedListener = onDataDownloadedListener;
    }

    public interface OnDataDownloadedListener{
        public void onDataDownloaded(ImageData data);
    }

}
