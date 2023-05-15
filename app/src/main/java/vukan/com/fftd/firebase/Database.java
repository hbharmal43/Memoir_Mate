package vukan.com.fftd.firebase;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import vukan.com.fftd.callbacks.CategoriesCallback;
import vukan.com.fftd.callbacks.CategoryCallback;
import vukan.com.fftd.callbacks.CommentsCallback;
import vukan.com.fftd.callbacks.FavoriteCallback;
import vukan.com.fftd.callbacks.FavoritesCallback;
import vukan.com.fftd.callbacks.MessageCallback;
import vukan.com.fftd.callbacks.MessagesCallback;
import vukan.com.fftd.callbacks.PostCallback;
import vukan.com.fftd.callbacks.PostImagesCallback;
import vukan.com.fftd.callbacks.PostsCallback;
import vukan.com.fftd.callbacks.RatingCallback;
import vukan.com.fftd.callbacks.UserCallback;
import vukan.com.fftd.models.Comment;
import vukan.com.fftd.models.Conv;
import vukan.com.fftd.models.FavoritePost;
import vukan.com.fftd.models.Message;
import vukan.com.fftd.models.Post;
import vukan.com.fftd.models.PostCategory;
import vukan.com.fftd.models.PostImage;
import vukan.com.fftd.models.ReportPost;
import vukan.com.fftd.models.ReportUser;
import vukan.com.fftd.models.User;

public class Database {
    private final FirebaseFirestore firestore;
    private List<Post> Posts;
    private List<PostCategory> categories;
    private List<FavoritePost> favouritesPosts;
    private List<PostImage> PostImages;
    private List<Post> userPosts;
    private List<Message> userMessages;
    private List<Message> userallMessages;
    private List<Conv> allUserConv;
    private List<Comment> userComments;
    private List<String> listOfsenders;
    private List<String> listOfPostid;
    private final FirebaseUser firebaseUser;
    private final Storage storage;

    public Database() {
        firestore = FirebaseFirestore.getInstance();
        Posts = new ArrayList<>();
        favouritesPosts = new ArrayList<>();
        PostImages = new ArrayList<>();
        userPosts = new ArrayList<>();
        userMessages = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = new Storage();
    }

    public void deleteConversation(Conv conv) {
        for (Message m : conv.getMessages()) {
            firestore.collection("messages").whereEqualTo("PostID", m.getPostID()).whereEqualTo("senderID", m.getSenderID()).whereEqualTo("receiverID", m.getReceiverID()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                        document.getReference().delete();
                }
            });
        }
    }

    public void deletePostImage(String url) {
        firestore.collection("PostsImages").whereEqualTo("imageUrl", url).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });
    }

    public void updateProfilePicture(Uri imageUrl) {
        storage.updateProfilePicture(firebaseUser.getUid(), imageUrl);
    }

    public void updateProfilePictureBitmap(Bitmap imageBitmap) {
        storage.updateProfilePictureBitmap(firebaseUser.getUid(), imageBitmap);
    }

    public void deleteUserData(String userID) {
        firestore.collection("Posts").whereEqualTo("userID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });

        firestore.collection("messages").whereEqualTo("receiverID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });

        firestore.collection("messages").whereEqualTo("senderID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });
    }

    public void deletePost(String id) {
        firestore.collection("Posts").document(id).delete();
    }

    public void getPostUser(String id, UserCallback callback) {
        firestore.collection("users").whereEqualTo("userID", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    User user = new User();
                    user.setUsername(document.getString("username"));
                    user.setImageUrl(document.getString("imageUrl"));
                    user.setPhone(document.getString("phone"));
                    user.setLocation(document.getString("location"));
                    callback.onCallback(user);
                }
            }
        });
    }

    public void isFavourite(String PostID, String userID, FavoriteCallback callback) {
        firestore.collection("favouritePosts").document(PostID + userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCallback(Objects.requireNonNull(task.getResult()).exists());
            }
        });
    }

    public void addUser(FirebaseUser user) {
        final DocumentReference doc = firestore.collection("users").document(user.getUid());
        firestore.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(doc);

            if (!snapshot.exists()) {
                User databaseUser = new User();
                databaseUser.setUsername(user.getDisplayName());
                databaseUser.setUserID(user.getUid());
                databaseUser.setImageUrl(Objects.requireNonNull(user.getPhotoUrl()).toString());
                firestore.collection("users").document(databaseUser.getUserID()).set(databaseUser, SetOptions.merge());
            }

            return null;
        });
    }

    public void sendMessage(Message m) {
        firestore.collection("messages").whereEqualTo("PostID", m.getPostID()).whereEqualTo("senderID", m.getSenderID()).whereEqualTo("receiverID", m.getReceiverID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                    Message emptyMessage = new Message();
                    emptyMessage.setReceiverID(m.getSenderID());
                    emptyMessage.setPostID(m.getPostID());
                    emptyMessage.setSenderID(m.getReceiverID());
                    emptyMessage.setDateTime(new Timestamp(new Date(1)));
                    emptyMessage.setContent("");
                    firestore.collection("messages").add(emptyMessage);
                }

                firestore.collection("messages").add(m);
            }
        });
    }

    public String addPost(Post p, String PostID) {
        Map<String, Object> Post = new HashMap<>();
        DocumentReference newPostRef;

        if (PostID.equals("0"))
            newPostRef = firestore.collection("Posts").document();
        else newPostRef = firestore.collection("Posts").document(PostID);

        Post.put("categoryID", p.getCategoryID());
        Post.put("datetime", p.getDatetime());
        Post.put("description", p.getDescription());
        Post.put("homePhotoUrl", p.getHomePhotoUrl());
        Post.put("name", p.getName());
        Post.put("price", p.getPrice());
        Post.put("PostID", newPostRef.getId());
        Post.put("seen", p.getSeen());
        Post.put("userID", p.getUserID());
        newPostRef.set(Post, SetOptions.merge());
        return newPostRef.getId();
    }

    public void addPostImage(PostImage pi) {
        if (pi.getImageUrl() != null)
            firestore.collection("PostsImages").document(pi.getImageUrl()).set(pi, SetOptions.merge());
    }

    public void getUserMessages(String senderId, String receiverId, String PostID, MessageCallback callback) {
        userMessages = new ArrayList<>();

        firestore.collection("messages").orderBy("dateTime").whereEqualTo("PostID", PostID).whereIn("senderID", Arrays.asList(receiverId, senderId)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (Objects.requireNonNull(document.getString("senderID")).equals(receiverId)) {
                        if (!(Objects.requireNonNull(document.getString("receiverID")).equals(senderId)))
                            continue;
                    }

                    Message message = new Message();
                    message.setContent(document.getString("content"));
                    message.setDateTime(document.getTimestamp("dateTime"));
                    message.setSenderID(document.getString("senderID"));
                    message.setPostID(document.getString("PostID"));
                    message.setReceiverID(document.getString("receiverID"));
                    userMessages.add(message);
                }

                callback.onCallback(userMessages);
            }
        });
    }

    public void getAllUserMessages(String user, MessagesCallback callback) {
        userallMessages = new ArrayList<>();
        allUserConv = new ArrayList<>();
        listOfsenders = new ArrayList<>();
        listOfPostid = new ArrayList<>();

        firestore.collection("messages").orderBy("dateTime").whereEqualTo("receiverID", user).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String sender;
                    sender = document.getString("senderID");
                    if (!listOfsenders.contains(sender)) listOfsenders.add(sender);
                    String Postid;
                    Postid = document.getString("PostID");
                    if (!listOfPostid.contains(Postid)) listOfPostid.add(Postid);
                }
            }

            firestore.collection("messages").orderBy("dateTime").get().addOnCompleteListener(taskk -> {
                if (taskk.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(taskk.getResult())) {
                        Message message = new Message();
                        message.setContent(document.getString("content"));
                        message.setDateTime(document.getTimestamp("dateTime"));
                        message.setSenderID(document.getString("senderID"));
                        message.setPostID(document.getString("PostID"));
                        message.setReceiverID(document.getString("receiverID"));
                        userallMessages.add(message);
                    }
                }

                for (String prodid : listOfPostid) {
                    for (String sendid : listOfsenders) {
                        Conv jednakonverzacija = new Conv();

                        for (Message message : userallMessages) {
                            if (message.getPostID().equals(prodid) && ((message.getReceiverID().equals(sendid) && message.getSenderID().equals(user)) || (message.getSenderID().equals(sendid) && message.getReceiverID().equals(user))))
                                jednakonverzacija.getMessages().add(message);
                        }

                        if (jednakonverzacija.getMessages().size() > 0)
                            allUserConv.add(jednakonverzacija);
                    }
                }

                for (Conv c : allUserConv) {
                    String id = c.getMessages().get(0).getReceiverID();

                    if (id.equals(Objects.requireNonNull(firebaseUser).getUid()))
                        id = c.getMessages().get(0).getSenderID();

                    firestore.collection("users").whereEqualTo("userID", id).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task1.getResult())) {
                                c.setUserName(document.getString("username"));
                            }

                            firestore.collection("Posts").whereEqualTo("PostID", c.getMessages().get(0).getPostID()).get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task2.getResult())) {
                                        c.setPostName(document.getString("name"));
                                    }

                                    callback.onCallback(allUserConv);
                                }
                            });
                        }
                    });
                }
            });
        });
    }

    public void getPosts(PostsCallback callback) {
        Posts = new ArrayList<>();

        firestore.collection("Posts").orderBy("datetime", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Post Post = new Post();
                    Post.setName(document.getString("name"));
                    Post.setDescription(document.getString("description"));
                    Post.setHomePhotoUrl(document.getString("homePhotoUrl"));
                    Post.setPostID(document.getString("PostID"));
                    Post.setUserID(document.getString("userID"));
                    Posts.add(Post);
                    callback.onCallback(Posts);
                }

                if (task.getResult().getDocuments().isEmpty()) callback.onCallback(Posts);
            }
        });
    }

    public void getCategories(CategoriesCallback callback) {
        categories = new ArrayList<>();

        firestore.collection("PostCategories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    PostCategory category = new PostCategory();
                    category.setName(document.getString("name"));
                    category.setCategoryID(document.getString("categoryID"));
                    categories.add(category);
                }

                callback.onCallback(categories);
            }
        });
    }

    public void getCategory(String categoryID, CategoryCallback callback) {
        firestore.collection("PostCategories").whereEqualTo("categoryID", categoryID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    PostCategory category = new PostCategory();
                    category.setName(document.getString("name"));
                    category.setCategoryID(document.getString("categoryID"));
                    callback.onCallback(category);
                }
            }
        });
    }

    public void getPost(String id, PostCallback callback) {
        firestore.collection("Posts").whereEqualTo("PostID", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Post Post = new Post();
                    Post.setName(document.getString("name"));
                    Post.setPostID(document.getString("PostID"));
                    Post.setHomePhotoUrl(document.getString("homePhotoUrl"));
                    callback.onCallback(Post);
                }
            }
        });
    }

    public void getFavouritePosts(String userID, FavoritesCallback callback) {
        favouritesPosts = new ArrayList<>();

        firestore.collection("favouritePosts").whereEqualTo("userID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    FavoritePost Post = new FavoritePost();
                    Post.setUserID(userID);
                    Post.setPostID(document.getString("PostID"));
                    favouritesPosts.add(Post);
                }

                callback.onCallback(favouritesPosts);
            }
        });
    }

    public void filterPosts(String[] filters, PostsCallback callback) {
        Posts = new ArrayList<>();
        Query query = firestore.collection("Posts");

        if (filters[0] == null && filters[1] == null && filters[2] == null && filters[3] == null) {
            query = query.orderBy("datetime", Query.Direction.DESCENDING);
        }

        if (filters[0] != null && !filters[0].isEmpty())
            query = query.whereGreaterThanOrEqualTo("price", Double.valueOf(filters[0]));

        if (filters[1] != null && !filters[1].isEmpty()) {
            query = query.whereLessThanOrEqualTo("price", Double.valueOf(filters[1]));
        }

        if (filters[2] != null && !filters[2].isEmpty()) {
            if (filters[2].equals("opadajuce"))
                query = query.orderBy("price", Query.Direction.DESCENDING);
            else query = query.orderBy("price", Query.Direction.ASCENDING);
        }

        if (filters[3] != null && !filters[3].isEmpty())
            query = query.whereEqualTo("categoryID", filters[3]);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Post Post = new Post();
                    Post.setName(document.getString("name"));
                    Post.setHomePhotoUrl(document.getString("homePhotoUrl"));
                    Post.setPostID(document.getString("PostID"));
                    Post.setUserID(document.getString("userID"));
                    Posts.add(Post);
                    callback.onCallback(Posts);
                }

                if (task.getResult().getDocuments().isEmpty()) callback.onCallback(Posts);
            }
        });
    }

    public void incrementCounter(String PostID, String id) {
        final DocumentReference doc = firestore.collection("Posts").document(PostID);
        firestore.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(doc);

            if (!Objects.requireNonNull(snapshot.getString("userID")).equals(id)) {
                Long seen = Objects.requireNonNull(snapshot.getLong("seen")) + 1;
                transaction.update(doc, "seen", seen);
            }

            return null;
        });
    }

    public void getPostImages(String id, PostImagesCallback callback) {
        PostImages = new ArrayList<>();

        firestore.collection("PostsImages").whereEqualTo("PostID", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    PostImage PostImage = new PostImage();
                    PostImage.setImageUrl(document.getString("imageUrl"));
                    PostImages.add(PostImage);
                }

                callback.onCallback(PostImages);
            }
        });
    }

    public void getPostDetails(String id, PostCallback callback) {
        firestore.collection("Posts").whereEqualTo("PostID", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Post Post = new Post();
                    Post.setHomePhotoUrl(document.getString("homePhotoUrl"));
                    Post.setCategoryID(document.getString("categoryID"));
                    Post.setDescription(document.getString("description"));
                    Post.setName(document.getString("name"));
                    Post.setPrice(document.getDouble("price"));
                    Post.setPostID(document.getString("PostID"));
                    Post.setDatetime(document.getTimestamp("datetime"));
                    Post.setSeen(document.getLong("seen"));
                    Post.setUserID(document.getString("userID"));
                    callback.onCallback(Post);
                }
            }
        });
    }

    public void getUser(String userID, UserCallback callback) {
        if (userID != null) {
            firestore.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        User user = new User();
                        user.setUserID(document.getString("userID"));
                        user.setUsername(document.getString("username"));
                        user.setLocation(document.getString("location"));
                        user.setPhone(document.getString("phone"));
                        user.setImageUrl(document.getString("imageUrl"));
                        callback.onCallback(user);
                    }
                }
            });
        }
    }

    public void getUserPosts(String userID, PostsCallback callback) {
        userPosts = new ArrayList<>();

        firestore.collection("Posts").whereEqualTo("userID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Post Post = new Post();
                    Post.setName(document.getString("name"));
                    Post.setHomePhotoUrl(document.getString("homePhotoUrl"));
                    Post.setPostID(document.getString("PostID"));
                    Post.setDatetime(document.getTimestamp("datetime"));
                    userPosts.add(Post);
                }

                callback.onCallback(userPosts);
            }
        });
    }

    public void addUserComment(Comment newComment) {
        Map<String, Object> comments = new HashMap<>();
        DocumentReference newCommentRef = firestore.collection("comments").document();
        comments.put("fromUserID", newComment.getFromUserID());
        comments.put("toUserID", newComment.getToUserID());
        comments.put("comment", newComment.getComment());
        comments.put("grade", newComment.getGrade());
        newCommentRef.set(comments);
    }

    public void getUserComments(String userID, CommentsCallback callback) {
        userComments = new ArrayList<>();

        firestore.collection("comments").whereEqualTo("toUserID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Comment comment = new Comment();
                    comment.setToUserID(userID);
                    comment.setFromUserID(document.getString("fromUserID"));
                    comment.setComment(document.getString("comment"));
                    comment.setGrade(Float.valueOf(String.valueOf(document.getDouble("grade"))));
                    userComments.add(comment);
                }

                callback.onCallback(userComments);
            }
        });
    }

    public void getUserRating(String userID, RatingCallback callback) {
        ArrayList<Double> sums = new ArrayList<>();

        firestore.collection("comments").whereEqualTo("toUserID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Double value = document.getDouble("grade");
                    sums.add(value);
                }
            }

            int count = 0;
            double sum = 0.0;

            while (sums.size() > count) {
                sum = sum + sums.get(count);
                count++;
            }

            if (count == 0) callback.onCallback((float) 0);
            float grade = (float) sum / count;
            callback.onCallback(grade);
        });
    }

    public void addPostToFavourites(String PostID, String userID) {
        FavoritePost Post = new FavoritePost();
        Post.setPostID(PostID);
        Post.setUserID(userID);
        firestore.collection("favouritePosts").document(PostID + userID).set(Post);
    }

    public void removePostFromFavourites(String PostID, String userID) {
        firestore.collection("favouritePosts").document(PostID + userID).delete();
    }

    public void editUserInfo(User user) {
        Objects.requireNonNull(firebaseUser).updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .build());

        firestore.collection("users").document(user.getUserID()).update("phone", user.getPhone(), "username", user.getUsername(), "location", user.getLocation());
    }

    public void reportAd(String PostID) {
        ReportPost reportPost = new ReportPost();
        reportPost.setUserID(firebaseUser.getUid());
        reportPost.setPostID(PostID);
        firestore.collection("reportsPosts").document().set(reportPost, SetOptions.merge());
    }

    public void reportUser(String userID) {
        ReportUser reportUser = new ReportUser();
        reportUser.setReporterUserID(firebaseUser.getUid());
        reportUser.setReportedUserID(userID);
        firestore.collection("reportsUsers").document().set(reportUser, SetOptions.merge());

        firestore.collection("messages").whereEqualTo("senderID", reportUser.getReportedUserID()).whereEqualTo("receiverID", reportUser.getReporterUserID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });

        firestore.collection("messages").whereEqualTo("senderID", reportUser.getReporterUserID()).whereEqualTo("receiverID", reportUser.getReportedUserID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });

        firestore.collection("comments").whereEqualTo("fromUserID", reportUser.getReporterUserID()).whereEqualTo("toUserID", reportUser.getReportedUserID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });

        firestore.collection("messages").whereEqualTo("fromUserID", reportUser.getReportedUserID()).whereEqualTo("toUserID", reportUser.getReporterUserID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    document.getReference().delete();
            }
        });

        firestore.collection("Posts").whereEqualTo("userID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    firestore.collection("favouritePosts").whereEqualTo("userID", firebaseUser.getUid()).whereEqualTo("PostID", document.getString("PostID")).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : Objects.requireNonNull(task1.getResult()))
                                document1.getReference().delete();
                        }
                    });
                }
            }
        });
    }
}