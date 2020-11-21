package tr.com.emiroglu.arackontrolkumanda;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Button btnBluetooth;
    TextView txtBluetoothKontrol;
    Button btnEslesmisCihazlariListele;
    ListView lstVwEslesmisCihazlar;

    Set<BluetoothDevice> eslesmisCihazlar;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBluetooth = (Button)findViewById(R.id.btnBluetooth);
        txtBluetoothKontrol = (TextView) findViewById(R.id.txtBluetoothKontrol);
        btnEslesmisCihazlariListele = (Button)findViewById(R.id.btnEslesmisCihazlariListele);
        lstVwEslesmisCihazlar = (ListView)findViewById(R.id.lstVwEslesmisCihazlar);

        bluetoothKontrol();

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                bluetoothKontrol();
            }
        });

        btnEslesmisCihazlariListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                eslesmisCihazlariListele();
            }
        });
    }

    private void bluetoothKontrol()
    {
        if(!bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.enable();
            txtBluetoothKontrol.setText("Bluetooth Açık");
            btnEslesmisCihazlariListele.setVisibility(View.VISIBLE);
            lstVwEslesmisCihazlar.setVisibility(View.VISIBLE);
        }
        else if(bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.disable();
            txtBluetoothKontrol.setText("Bluetooth Kapalı");
            btnEslesmisCihazlariListele.setVisibility(View.INVISIBLE);
            lstVwEslesmisCihazlar.setVisibility(View.INVISIBLE);
        }
    }

    private void eslesmisCihazlariListele()
    {
        eslesmisCihazlar = bluetoothAdapter.getBondedDevices();

        ArrayList list = new ArrayList();

        if(eslesmisCihazlar.size() > 0)
        {
            for(BluetoothDevice bd : eslesmisCihazlar)
            {
                list.add(bd.getName() + "\n" + bd.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Herhangi bir eşleşmiş cihaz yok!" , Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        lstVwEslesmisCihazlar.setAdapter(adapter);

        lstVwEslesmisCihazlar.setOnItemClickListener(cihazSecimi);
    }

    public AdapterView.OnItemClickListener cihazSecimi = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            String cihazIsmi = ((TextView) view).getText().toString();
            String cihazAdresi = cihazIsmi.substring(cihazIsmi.length()-17);

            Intent intent = new Intent(MainActivity.this, AracModuActivity.class);
            intent.putExtra("adres" , cihazAdresi);
            startActivity(intent);
        }
    };
}
