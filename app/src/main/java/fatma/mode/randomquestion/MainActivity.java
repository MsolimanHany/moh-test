package fatma.mode.randomquestion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
Button random ;
TextView answer;
    ArrayList<String> arrayList;
    ArrayList<Integer> arrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = (Button) findViewById(R.id.random);
        answer = (TextView) findViewById(R.id.que);
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();


        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("question");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> products = new ArrayList<String>();
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String product = productSnapshot.getValue(String.class);
                        products.add(product);
                    }
                    System.out.println(products);
                    arrayList = products;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });

            random.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int length = arrayList.size() - 1;
                    int rand = getRandomNumber(0, length);
                    if (arrayList2.size() == arrayList.size()) {
                        answer.setText("انتهت الاسئله");
                    } else {
                        while (arrayList2.contains(rand)) {
                            rand = getRandomNumber(0, length);
                        }
                        answer.setText(arrayList.get(rand) + "");
                        arrayList2.add(rand);

                    }

                }
            });
        }
        else
        {
            answer.setText("لا يوجد انترنت حاليا");
        }

    }
    public int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }
}
