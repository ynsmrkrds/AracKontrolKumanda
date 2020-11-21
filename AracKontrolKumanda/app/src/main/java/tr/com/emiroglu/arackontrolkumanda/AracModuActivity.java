package tr.com.emiroglu.arackontrolkumanda;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class AracModuActivity extends AppCompatActivity
{
    String cihazAdresi = null;
    private ProgressDialog progressDialog;

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    BluetoothDevice bluetoothDevice;
    BluetoothServerSocket bluetoothServerSocket;

    private boolean isBluetoothConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Button btnCizgiTakipEdenAracModu;
    Button btnEngeldenKacanAracModu;
    Button btnOzgurAracModu;
    TextView txtVwSecilenAracModu;
    LinearLayout lnrLytEngeldenKacanAracModu;
    LinearLayout lnrLytYonTuslari;
    Button btnBaslatDurdur;
    TextView txtVwBilgilendirme;
    boolean iconKontrol = true;
    String modKontrol;

    Button btnIleri;
    Button btnSag;
    Button btnSol;
    Button btnGeri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arac_modu);

        Intent intent = getIntent();
        new BTbaglan().execute();
        cihazAdresi = intent.getStringExtra("adres");

        btnCizgiTakipEdenAracModu = (Button)findViewById(R.id.btnCizgiTakipEdenAracModu);
        btnEngeldenKacanAracModu = (Button)findViewById(R.id.btnEngeldenKacanAracModu);
        btnOzgurAracModu = (Button)findViewById(R.id.btnOzgurAracModu);
        txtVwSecilenAracModu = (TextView)findViewById(R.id.txtVwSeciliAracModu);
        lnrLytEngeldenKacanAracModu = (LinearLayout)findViewById(R.id.lnrLytEngeldenKacanAracModu);
        lnrLytYonTuslari = (LinearLayout)findViewById(R.id.lnrLytYonTuslari);
        btnBaslatDurdur = (Button)findViewById(R.id.btnBaslatDurdur);
        txtVwBilgilendirme = (TextView)findViewById(R.id.txtVwBilgilendirme);
        btnIleri = (Button)findViewById(R.id.btnIleri);
        btnSag = (Button)findViewById(R.id.btnSag);
        btnSol = (Button)findViewById(R.id.btnSol);
        btnGeri = (Button)findViewById(R.id.btnGeri);

        lnrLytEngeldenKacanAracModu.setVisibility(View.INVISIBLE);
        lnrLytYonTuslari.setVisibility(View.INVISIBLE);

        btnCizgiTakipEdenAracModu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                modKontrol = "CIZGI";

                txtVwSecilenAracModu.setText("ÇİZGİ TAKİP EDEN ARAÇ MODU");

                txtVwBilgilendirme.setText("ARACI,SENSÖRLER ÇİZGİYİ ALGILAYANA KADAR PARKURA YAKLAŞTIRINIZ!");
                lnrLytEngeldenKacanAracModu.setVisibility(View.VISIBLE);
                lnrLytYonTuslari.setVisibility(View.VISIBLE);
                //çizigi algılandığında bilgi verilecek daha sonra başlatması için müsade edilecek
            }
        });

        btnEngeldenKacanAracModu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                modKontrol = "ENGEL";

                txtVwSecilenAracModu.setText("ENGELDEN KAÇAN ARAÇ MODU");

                txtVwBilgilendirme.setText("ARACI İSTEDİĞİNİZ KONUMA GETİRDİKTEN SONRA OYUNU BAŞLATABİLİRSİNİZ.");
                lnrLytEngeldenKacanAracModu.setVisibility(View.VISIBLE);
                lnrLytYonTuslari.setVisibility(View.VISIBLE);
            }
        });


        btnOzgurAracModu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                txtVwSecilenAracModu.setText("ÖZGÜR ARAÇ MODU");

                lnrLytEngeldenKacanAracModu.setVisibility(View.INVISIBLE);
                lnrLytYonTuslari.setVisibility(View.VISIBLE);
            }
        });

        btnBaslatDurdur.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(bluetoothSocket != null)
                {
                    try
                    {
                        if(modKontrol.equals("CIZGI"))
                        {
                            bluetoothSocket.getOutputStream().write("1".toString().getBytes());
                        }
                        else if(modKontrol.equals("ENGEL"))
                        {
                            bluetoothSocket.getOutputStream().write("0".toString().getBytes());
                        }
                    }
                    catch (IOException e)
                    {

                    }
                }

                if(iconKontrol == true)
                {
                    btnBaslatDurdur.setBackgroundResource(R.drawable.durdur);
                    iconKontrol = false;
                    lnrLytYonTuslari.setVisibility(View.INVISIBLE);
                }
                else
                {
                    btnBaslatDurdur.setBackgroundResource(R.drawable.baslat);
                    iconKontrol = true;
                    lnrLytYonTuslari.setVisibility(View.VISIBLE);
                }
            }
        });

        btnIleri.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(bluetoothSocket != null)
                {
                    switch (motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("8".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }

                            break;

                        case MotionEvent.ACTION_UP:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("5".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }
                            break;
                    }
                }

                return false;
            }
        });

        btnSag.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(bluetoothSocket != null)
                {
                    switch (motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("6".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }

                            break;

                        case MotionEvent.ACTION_UP:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("5".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }
                            break;
                    }
                }

                return false;
            }
        });

        btnSol.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(bluetoothSocket != null)
                {
                    switch (motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("4".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }

                            break;

                        case MotionEvent.ACTION_UP:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("5".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }
                            break;
                    }
                }

                return false;
            }
        });

        btnGeri.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(bluetoothSocket != null)
                {
                    switch (motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("2".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }

                            break;

                        case MotionEvent.ACTION_UP:
                            try
                            {
                                bluetoothSocket.getOutputStream().write("5".toString().getBytes());
                            }
                            catch (IOException e)
                            {

                            }
                            break;
                    }
                }

                return false;
            }
        });
    }

    private void Disconnect()
    {
        if(bluetoothSocket != null)
        {
            try
            {
                bluetoothSocket.close();
            }
            catch(IOException e)
            {

            }
        }
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Disconnect();
    }

    private class BTbaglan extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(AracModuActivity.this, "Baglanıyor...", "Lütfen Bekleyin");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (bluetoothSocket == null || !isBluetoothConnected)
                {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(cihazAdresi);
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(), "Bağlantı Hatası Tekrar Deneyin", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Bağlantı Başarılı", Toast.LENGTH_SHORT).show();

                isBluetoothConnected = true;
            }
            progressDialog.dismiss();
        }
    }
}
