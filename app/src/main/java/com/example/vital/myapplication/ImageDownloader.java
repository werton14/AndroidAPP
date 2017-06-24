package com.example.vital.myapplication;

import android.util.Log;

import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

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
                List<DataSnapshot> snapshots = transformToDataSnaphsotList(iterator);
                sortDataSnapshots(snapshots);

                List<DatabaseReference> imageDbReferenceList = new ArrayList<DatabaseReference>();
                List<String> imageIds = new ArrayList<String>();
                for (int i = 0; i < snapshots.size() / 2; i++) {
                    unDownloadedData++;
                    unUpdatedViews++;
                    DatabaseReference imageViewsDbReference = snapshots.get(i).getRef();
                    imageIds.add(imageViewsDbReference.getKey());
                    updateImageViews(imageViewsDbReference);
                    imageDbReferenceList.add(getImageDbReference(imageViewsDbReference));
                }
                List<Image> images = new ArrayList<Image>();
                List<User> users = new ArrayList<User>();
                getImage(imageDbReferenceList, images, users, imageIds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImage(List<DatabaseReference> imageDbReferenceList, final List<Image> images, final List<User> users, final List<String> imageIds){
        for(DatabaseReference imageDbReference: imageDbReferenceList) {
            imageDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Image image = dataSnapshot.getValue(Image.class);
                    images.add(image);
                    getUser(image, images, users, imageIds);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getUser(final Image image, final List<Image> images, final List<User> users, final List<String> imageIds){
        firebaseInfo.getUsersDbReference().child(image.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);

                unDownloadedData--;
                if(unDownloadedData == 0){
                    onDataDownloadedListener.onDataDownloaded(images, users, imageIds);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    private List<DataSnapshot> transformToDataSnaphsotList(Iterator<DataSnapshot> iterator){
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
        public void onDataDownloaded(List<Image> images, List<User> users, List<String> imageIds);
    }

}
