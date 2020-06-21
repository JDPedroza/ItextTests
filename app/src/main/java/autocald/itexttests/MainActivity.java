package autocald.itexttests;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[]headers={"Id", "Nombre", "Apellido"};
    private String shortText="Prueba";
    private String longText="Este es un texto largo de prueba";
    private TemplatePDF templatePDF;
    private Button btnOpen;
    private Button btnSend;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpen = findViewById(R.id.buttonOpen);
        btnSend = findViewById(R.id.buttonSend);
        //permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,},1000);
        }
        templatePDF=new TemplatePDF(getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("clientes", "ventas", "Johan");
        templatePDF.addTitles("Tienda", "Clientes", "20/06/19");
        templatePDF.addParagraph(shortText);
        templatePDF.addParagraph(longText);
        templatePDF.createTable(headers, getClients());
        templatePDF.closeDocument();
        btnOpen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                appViewPDF();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                appSendPDF();
            }
        });
    }
    public void appViewPDF(){
        templatePDF.appViewPDF(this);
    }
    private ArrayList<String[]>getClients(){
        ArrayList<String[]> rows= new ArrayList<>();
        rows.add(new String[]{"1", "Pedro", "Lopez"});
        rows.add(new String[]{"2", "Juan", "Sanchez"});
        rows.add(new String[]{"3", "Johan", "Pedroza"});
        rows.add(new String[]{"4", "Lucas", "Gomez"});
        return rows;
    }
    public void appSendPDF(){
        String[] mailto = {""};
        Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", templatePDF.getFile());
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Calc PDF Report");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hi PDF is attached in this mail. ");
        emailIntent.setType("application/pdf");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send email using:"));
    }

    }
