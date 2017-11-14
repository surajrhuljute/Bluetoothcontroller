
package nareshit.com.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void turnOn(View view) {
        if(adapter.isEnabled()==false){
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, 2222);
            Toast.makeText(this, "Turned On", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Already turned on", Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOff(View view) {
        if(adapter.isEnabled())
            adapter.disable();

        Toast.makeText(this, "Turned off", Toast.LENGTH_SHORT).show();
    }

    public void showPairedDevices(View view) {
        Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();

        String deviceInfo = "";

        for(BluetoothDevice device: bondedDevices){
            deviceInfo = deviceInfo+device.getName()+"\n";
        }

        textView.setText(deviceInfo);

       /* ArrayList<BluetoothDevice> devices = new ArrayList<>(bondedDevices);

        String deviceInfo = "";

        for (int i=0;i<devices.size();i++){
            BluetoothDevice device = devices.get(i);

            deviceInfo = deviceInfo+device.getName()+"\n";
        }

        textView.setText(deviceInfo);*/
    }

    public void startScan(View view) {
        adapter.startDiscovery();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);

        BluetoothReceiver receiver = new BluetoothReceiver();
        registerReceiver(receiver, filter);
    }

    class BluetoothReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            textView.setText(device.getName()+"  "+device.getAddress());
        }
    }
}
