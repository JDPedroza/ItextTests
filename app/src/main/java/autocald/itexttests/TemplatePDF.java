package autocald.itexttests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressLint("Registered")
public class TemplatePDF extends FileProvider{
    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    //List Format
    private Font fTextGenericTitle = new Font(Font.FontFamily.HELVETICA, 11);
    private Font fTextGenericTitle1 = new Font(Font.FontFamily.HELVETICA, 5);
    private Font fTextGenericTitle2 = new Font(Font.FontFamily.HELVETICA, 10);
    private Font fTextGenericTitle3 = new Font(Font.FontFamily.HELVETICA, 6);
    private Font fTextGenericTitle4 = new Font(Font.FontFamily.HELVETICA, 8);
    private Font fTextGenericTitle5 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.RED);
    //List Data
    private String[]dataClient;
    private String[]dataTechnical;
    private String[][]dataComputer;
    private String[][]dataBurnerAssembly;
    private String[][]dataControlSecurity;
    private String[][]dataWaterLevelControl;
    private String[][]dataCondensateTank;
    private String[][]dataWaterPump;
    private String[][]dataSecurityTest;
    private String[][]dataBody;
    private String[][]dataElectricalPanelControl;
    private String[][]dataElectricalMotors;
    private String[][]dataPipesAccessories;
    private String[]dataObservations;
    private String[]dataRecommendations;
    //builder
    public TemplatePDF(Context context) {
        this.context=context;
        loadData();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createFile(){
        File folder=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "PDF");
        if(!folder.exists()){
            folder.mkdir();
            pdfFile=new File(folder, "TemplatePDF.pdf");
        }
        pdfFile=new File(folder, "TemplatePDF.pdf");
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openDocument(){
        createFile();
        try {
            document=new Document(PageSize.A4, 20, 20, 20, 20);
            pdfWriter=PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e){
            Log.e("OpenDocument", e.toString());
        }

    }
    public void closeDocument(){
        document.close();
    }
    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }
    public void createTable(){
        try {
        PdfPTable table = customizeTabla();
            //1 - 4
            table = dataClient(table);
            //5 row
            table.addCell(customizeCell("ESTADO DE LOS ELEMENTOS DE LA CALDERA", 0, true, true, 0, 10));
            //6-58
            table = dataModules(table);
            //59-relativo
            table = dataObservationsRecommendations(table);
            //relativo+4
            table = dataTechnical(table);
            //+2
            table = dataOthers(table);
        document.add(table);
        }catch (Exception e){
            Log.e("createTable", e.toString());
        }
    }
    private PdfPTable customizeTabla() throws DocumentException {
        PdfPTable table = new PdfPTable(10);
        table.setWidths(new float[] { 17, 3, 3, 3, 17, 20, 3,3,3, 19});
        table.setWidthPercentage(100);
        return table;
    }
    private PdfPCell customizeCell(String text, int font, boolean title, boolean center,int rowSpan, int colSpan){
        PdfPCell cell = new PdfPCell();
        Paragraph paragraph = new Paragraph();
        //add text and format
        if(font==0){
            paragraph = new Paragraph(text, fTextGenericTitle);
        }else if(font==1){
            paragraph = new Paragraph(text, fTextGenericTitle1);
        }else if(font==2){
            paragraph = new Paragraph(text, fTextGenericTitle2);
        }else if(font==3){
            paragraph = new Paragraph(text, fTextGenericTitle3);
        }else if(font==4){
            paragraph = new Paragraph(text, fTextGenericTitle4);
        }else {
            paragraph = new Paragraph(text, fTextGenericTitle5);
        }
        //add center
        if(center){
            paragraph.setAlignment(Element.ALIGN_CENTER);
        }
        //add text
        cell.addElement(paragraph);
        //add title
        if(title){
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        //add rowSpan
        if(rowSpan!=0){
            cell.setRowspan(rowSpan);
        }
        //add colSpan
        if(colSpan!=0){
            cell.setColspan(colSpan);
        }
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
    private PdfPTable dataClient(PdfPTable table){
        /*
        0 = private Font fTextGenericTitle = new Font(Font.FontFamily.HELVETICA, 11);
        1 = private Font fTextGenericTitle1 = new Font(Font.FontFamily.HELVETICA, 5);
        2 = private Font fTextGenericTitle2 = new Font(Font.FontFamily.HELVETICA, 10);
        3 = private Font fTextGenericTitle3 = new Font(Font.FontFamily.HELVETICA, 6);
        4 = private Font fTextGenericTitle4 = new Font(Font.FontFamily.HELVETICA, 8);
        5 = private Font fTextGenericTitle5 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.RED);
         */
        //primera fila
        PdfPCell cell = new PdfPCell();
        cell.addElement(getImage());
        cell.setColspan(5);
        cell.setRowspan(4);
        table.addCell(cell);
        table.addCell(customizeCell("CONTROL DE SERVICIO", 4, true, true,0, 0));
        table.addCell(customizeCell("FECHA:(d/m/a)", 1, true, true,0, 3));
        table.addCell(customizeCell("TIPO DE SERVICIO", 0, true, true, 0, 0));

        //segunda fila
        for(int i=0; i<3; i++){
            if(i==0){
                table.addCell(customizeCell(getData(1, i, 0), 0, false, true, 0, 0));
            }
            if(i==1){
                table.addCell(customizeCell(getData(1, i, 0), 2, false, true, 0, 3));
            }
            if(i==2){
                table.addCell(customizeCell(getData(1, i, 0), 3, false, true, 0, 0));
            }
        }

        //tercera fila
            table.addCell(customizeCell("CLIENTE", 0, true, true,0, 0));
            table.addCell(customizeCell("CAPACIDAD", 1, true, true,0, 3));
            table.addCell(customizeCell("MODELO / AÑO", 3, true, true, 0, 0));

        //cuarta fila
        for(int i=3; i<6; i++){
            if(i==3){
                table.addCell(customizeCell(getData(1, i, 0), 0, false, true, 0, 0));
            }
            if(i==4){
                table.addCell(customizeCell(getData(1, i, 0), 0, false, true, 0, 3));
            }
            if(i==5){
                table.addCell(customizeCell(getData(1, i, 0), 4, false, true, 0, 0));
            }
        }
        return table;
    }
    private PdfPTable dataModules(PdfPTable table){

        //6 row
        table = addTitleModule(table,1);
        table = addTitleModule(table,7);
        //7 -17 row
        int cModule2=0;

        int cModule4=0;
        int cModule5=0;
        int cModule6=0;
        int cModule7=0;
        int cModule8=0;
        int cModule9=0;
        int cModule10=0;
        int cModule11=0;
        int cModule12=0;
        int cModule13=0;
        int cModule14=0;
        for(int i=0; i<=51; i++){
            //conjunto quemador
            if(i<=10){
                table = addDataModule(table, 4, cModule4);
                cModule4++;

                table = addDataModule(table, 10, cModule10);
                cModule10++;
            }else if(i==11){
                table = addTitleModule(table,5);

                table = addDataModule(table, 10, cModule10);
                cModule10++;
            }else if(i==12){
                table = addDataModule(table, 5, cModule5);
                cModule5++;

                table = addTitleModule(table,11);
            }else if(i<=19){
                table = addDataModule(table, 5, cModule5);
                cModule5++;

                table = addDataModule(table, 11, cModule11);
                cModule11++;
            }else if(i==20){
                table = addTitleModule(table,6);

                table = addDataModule(table, 11, cModule11);
                cModule11++;
            }else if(i<=25){
                table = addDataModule(table, 6, cModule6);
                cModule6++;

                table = addDataModule(table, 11, cModule11);
                cModule11++;
            }else if(i==26){
                table = addDataModule(table, 6, cModule6);
                cModule6++;

                table = addTitleModule(table,12);
            }else if(i==27){
                table = addTitleModule(table,7);

                table = addDataModule(table, 12, cModule12);
                cModule12++;
            }else if(i<=31){
                table = addDataModule(table, 7, cModule7);
                cModule7++;

                table = addDataModule(table, 12, cModule12);
                cModule12++;
            }else if(i==32){
                table = addDataModule(table, 7, cModule7);
                cModule7++;

                table = addTitleModule(table,13);
            }else if(i<=34){
                table = addDataModule(table, 7, cModule7);
                cModule7++;

                table = addDataModule(table, 13, cModule13);
                cModule13++;
            }else if(i==35){
                table = addTitleModule(table, 8);

                table = addDataModule(table, 13, cModule13);
                cModule13++;
            }else if(i<=39){
                table = addDataModule(table, 8, cModule8);
                cModule8++;

                table = addDataModule(table, 13, cModule13);
                cModule13++;
            }else if(i==40){
                table = addTitleModule(table, 9);

                table = addDataModule(table, 13, cModule13);
                cModule13++;
            }else if(i==41){
                table = addDataModule(table, 9, cModule9);
                cModule9++;

                table.addCell(customizeCell("DATOS  DEL EQUIPO", 3, true, false, 0, 0));
                table.addCell(customizeCell("DATO",3, true, true, 0, 3));
                table.addCell(customizeCell("OBSERVACIONES", 3, true, true, 0, 0 ));
            }else
                //i<=51
                {
                table = addDataModule(table, 9, cModule9);
                cModule9++;

                table = dataComputer(table, cModule2);
                cModule2++;
            }/*else if(i==52){
                table.addCell(customizeCell("OBSERVACIONES GENERALES", 3, true, false, 0, 5));

                table = dataComputer(table, cModule2);
                cModule2++;
            }else if(i==53){
                table.addCell(customizeCell(getData(14, cModule14, 0), 3, false, false, 0, 5));
                cModule14++;

                table = dataComputer(table, cModule2);
                cModule2++;
            }else if(i==54){
                table.addCell(customizeCell(getData(14, cModule14, 0), 3, false, false, 0, 5));
                cModule14++;

                table.addCell(customizeCell("DIAGRAMAS", 3, true, false, 0, 5));
            }
            */
        }
        return table;
    }
    private PdfPTable dataComputer(PdfPTable table, int counter){

        table.addCell(customizeCell(getData(2, counter,0), 3, false, false, 0, 0));
        table.addCell(customizeCell(getData(2, counter,1), 4, false, true, 0, 3));
        table.addCell(customizeCell(getData(2, counter,2), 3, false, true, 0, 0 ));

        return table;
    }
    private PdfPTable dataObservationsRecommendations(PdfPTable table){
        int cModule2=10;
        int cModule14=0;
        int cModule15=0;
        int rowSpan = 0;
        int cIObservations = 0;
        int cIRecommendations = 0;
        for(int i=52;i<52+getLength(14)+getLength(15)+2; i++){
            if(i==52){
                table.addCell(customizeCell("OBSERVACIONES GENERALES", 3, true, true, 0, 5));

                table = dataComputer(table, cModule2);
                cModule2++;
            }else if(i==53){
                table.addCell(customizeCell(getData(14, cModule14, 0), 3, false, false, 0, 5));
                cModule14++;

                table = dataComputer(table, cModule2);
                cModule2++;
            }else if(i==54){
                table.addCell(customizeCell(getData(14, cModule14, 0), 3, false, false, 0, 5));
                cModule14++;

                table.addCell(customizeCell("DIAGRAMAS", 3, true, false, 0, 5));
            }else if(rowSpan==0){
                table.addCell(customizeCell(getData(14, cModule14, 0), 3, false, false, 0, 5));
                cModule14++;

                rowSpan = ((getLength(14)-(cModule14-1))+getLength(15)+1);

                table.addCell(customizeCell("", 3, false, false, rowSpan, 5));
                cIObservations=i;
            }else if(i<(cIObservations+getLength(14))-2){
                table.addCell(customizeCell(getData(14, cModule14, 0), 3, false, false, 0, 5));
                cModule14++;
                cIRecommendations = i;
            }else if(i==cIRecommendations+1){
                table.addCell(customizeCell("Recomendaciones", 3, true, true, 0, 5));
            }else if(i<(cIRecommendations+getLength(15)+2)){
                table.addCell(customizeCell(getData(15, cModule15, 0), 3, false, false, 0, 5));
                cModule15++;
            }
        }
        return table;
    }
    private PdfPTable dataTechnical(PdfPTable table){
        for (int i = 0; i<=3; i++){
            if(i==0){
                table.addCell(customizeCell("Nombre del Tecnico", 3, true, true, 0, 5));
                table.addCell(customizeCell("Hora de Llegada: " + getData(3, i, 1), 0, false, false, 0, 5));
            }else if(i==1){
                table.addCell(customizeCell(getData(3, i, 0),3, false, true, 0, 5));
                table.addCell(customizeCell("Hora de Salida: " + getData(3, i, 2), 0, false, false, 0, 5));
            }else if(i==2){
                table.addCell(customizeCell("Revisado:", 3, true, true, 0, 5));
                table.addCell(customizeCell("Horas Hombre: " + getData(3,i, 3), 0, false,false, 2, 5));
            }else if(i==3){
                table.addCell(customizeCell(getData(3, i, 4),3, false, true, 0, 5));
            }
        }
        return table;
    }
    private PdfPTable dataOthers(PdfPTable table){
        table.addCell(customizeCell("Aceptado:", 3, true, true, 0, 5));
        table.addCell(customizeCell("SELLO DE ACEPTACION", 0, false, false, 0, 5));
        table.addCell(customizeCell("Prueba", 0, false, false, 3, 5));
        table.addCell(customizeCell("Prueba", 0, false, false, 3, 5));
        return table;
    }
    private String getData(int module, int i, int j){
        String[][]data={{}};
        if(module==1){
            return this.dataClient[i];
        }else
        if(module==2){
            return this.dataComputer[i][j];
        }else
        if(module==3){
            return this.dataTechnical[j];
        }else
        if(module==4){
            return this.dataBurnerAssembly[i][j];
        }else
        if(module==5){
            return this.dataControlSecurity[i][j];
        }else
        if(module==6){
            return this.dataWaterLevelControl[i][j];
        }else
        if(module==7){
            return this.dataCondensateTank[i][j];
        }else
        if(module==8){
            return this.dataWaterPump[i][j];
        }else
        if(module==9){
            return this.dataSecurityTest[i][j];
        }else
        if(module==10){
            return this.dataBody[i][j];
        }else
        if(module==11){
            return this.dataElectricalPanelControl[i][j];
        }else
        if(module==12){
            return this.dataElectricalMotors[i][j];
        }else
        if(module==13){
            return this.dataPipesAccessories[i][j];
        }else
        if(module==14){
            return dataObservations[i];
        }else
        if(module==15){
            return dataRecommendations[i];
        }
        return data[i][j];
    }
    private Image getImage(){
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = null;
        byte[] byteArray = stream.toByteArray();
        try {
            img = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    private PdfPTable addTitleModule(PdfPTable table, int module){
        module = module-1;
        String[]titles= {"", "DATOS  DEL EQUIPO", "", "", "CONJUNTO QUEMADOR", "CONTROLES DE SEGURIDAD", "CONTROL DE NIVEL DE AGUA", "TANQUE DE CONDENSADOS", "BOMBA DE AGUA", "PRUEBAS DE SEGURIDAD", "CUERPO", "TABLERO ELECTRICO Y CONTROL", "MOTORES ELECTRICOS", "TUBERIAS - ACCESORIOS", "DATOS  DEL EQUIPO", "OBSERVACIONES GENERALES", "DIAGRAMAS"};
        table.addCell(customizeCell(titles[module], 3, true, false, 0, 0 ));
        table.addCell(customizeCell("B", 4, true, true, 0, 0 ));
        table.addCell(customizeCell("R", 4, true, true, 0, 0 ));
        table.addCell(customizeCell("M", 4, true, true, 0, 0 ));
        table.addCell(customizeCell("OBSERVACIONES", 3, true, true, 0, 0 ));
        return table;
    }
    private PdfPTable addDataModule(PdfPTable table, int module, int counter){
        //subTitle  module
        table.addCell(customizeCell(getData(module, counter, 0), 3, false, false, 0, 0));
        //B,R,M
        switch (getData(module, counter, 1)) {
                case "1":
                    table = optionsBRM(1, table);
                    break;
                case "2":
                    table = optionsBRM(2, table);
                    break;
                case "3":
                    table = optionsBRM(3, table);
                    break;
                default:
                    table = optionsBRM(0, table);
                    break;
            }
        //observation
        table.addCell(customizeCell(getData(module, counter, 2), 3, false, true, 0, 0));
        return table;
    }
    private PdfPTable optionsBRM(int i, PdfPTable table){
        if(i==1){
            table.addCell(customizeCell("X", 3, false, true, 0, 0));
            table.addCell(customizeCell("", 3, false, true, 0, 0));
            table.addCell(customizeCell("", 3, false, true, 0, 0));
        }else if(i==2){
            table.addCell(customizeCell("", 3, false, true, 0, 0));
            table.addCell(customizeCell("X", 3, false, true, 0, 0));
            table.addCell(customizeCell("", 3, false, true, 0, 0));
        }else if(i==3){
            table.addCell(customizeCell("", 3, false, true, 0, 0));
            table.addCell(customizeCell("", 3, false, true, 0, 0));
            table.addCell(customizeCell("X", 3, false, true, 0, 0));
        }else {
            table.addCell(customizeCell("", 3, false, true, 0, 0));
            table.addCell(customizeCell("", 3, false, true, 0, 0));
            table.addCell(customizeCell("", 3, false, true, 0, 0));
        }
        return table;
    }
    private int getLength(int module){
        if(module==14){
            return this.dataObservations.length;
        }else if(module==15){
            return this.dataRecommendations.length;
        }
        return 0;
    }
    private void loadData(){
        //1
        this.dataClient = new String[]{"366", "13/09/2019", "Mant General y Correctivo", "ALSAEM", "80 BHP", "DISTRAL"};
        //2
        this.dataComputer = new String[][]{
                {"Presion de Vapor", "100", ""},
                {"Atomizacion de aire", "-", ""},
                {"Atomizacion de vapor", "-", ""},
                {"Temperatura de combustible", "-", ""},
                {"Presion de combustible.", "-", ""},
                {"Presion de Gas Linea", "20 psi", ""},
                {"Presion de Gas Tren", "22 wca", ""},
                {"Temperatura de Gases", "200C", ""},
                {"Temperatura de Agua", "16 C", ""},
                {"Nivel de Caldera", "Ok", ""},
                {"Nivel de  Tanq Condensado", "Ok", ""},
                {"Nivel de Tanq Combustible", "Ok", ""},
        };
        //3
        this.dataTechnical = new String[]{"Carlos Martinez - Santiago Sanchez", "0", "0", "0", "Ivan Fernando Camargo"};
        //4
        this.dataBurnerAssembly = new String[][]{
                {"Valvula de Combustible", "2", "Presenta fuga de aceite"},
                {"Ventilador", "1", "Se reviso"},
                {"Trasformador de Ignicion", "1", "Se reviso"},
                {"Electrodos", "1", "Se limpiaron"},
                {"Cable de Alta", "1", "OK"},
                {"Cable de Alta", "1", "Se limpio"},
                {"Boquilla de combustible", "2", "Presenta venteo"},
                {"Filtro de combustible", "0", "NA"},
                {"Tuberia de combustible", "1", ""},
                {"Manometros de Combustible", "2", "manometro en valvula dañado"},
                {"Swich de Presion", "1", "OK"}
        };
        //5
        this.dataControlSecurity = new String[][]{
                {"Control de Presion", "1", ""},
                {"Termostato", "0", "NA"},
                {"Warrick( Bajo nivel Aux)", "1", "Se reviso"},
                {"Modutrol", "1", "Se limpiaron"},
                {"Control de Modulacion", "2", "OK"},
                {"Manometros", "1", "Se limpio"},
                {"Valvulas de seguridad", "1", "Presenta venteo"},
                {"Swich de Aire Ventilador", "1", "NA"},
        };
        //6
        this.dataWaterLevelControl = new String[][]{
                {"Grifos de Purga", "2", "Presenta fuga de aceite"},
                {"Vidrio de nivel", "1", "Se reviso"},
                {"Empaques", "1", "Se reviso"},
                {"Flotador", "1", "Se limpiaron"},
                {"Ampolletas o Micros", "1", "OK"},
                {"Grifos de nivel", "1", "Se limpio"},
        };
        //6
        this.dataCondensateTank = new String[][]{
                {"Aspecto general", "1", ""},
                {"Flotador", "0", "NA"},
                {"Vidrio de nivel", "1", "Se reviso"},
                {"Filtro de Agua", "1", "Se limpiaron"},
                {"Tuberias", "2", "OK"},
                {"Valvulas ", "1", "Se limpio"},
                {"Termometro", "1", "Presenta venteo"},
        };
        //7
        this.dataWaterPump = new String[][]{
                {"Aspecto general", "1", ""},
                {"Acople", "0", "NA"},
                {"Presion", "1", "Se reviso"},
                {"Accesorios", "1", "Se limpiaron"},
        };
        //8
        this.dataSecurityTest = new String[][]{
                {"Falla de Llama", "1", ""},
                {"Bajo Nivel Macdonell", "0", "NA"},
                {"Bajo Nivel Warrick", "1", "Se reviso"},
                {"Alta Presion", "1", "Se reviso"},
                {"Baja presion", "2", "OK"},
                {"Aire de Combustion", "1", "Se limpio"},
                {"Alta presion de Vapor", "1", "Presenta venteo"},
                {"Prueba Hidrostatica", "1", "NA"},
                {"Valvulas de seguridad", "1", "NA"},
                {"Temperatura (Alta y Baja)", "1", "NA"},
                {"Otros", "1", "NA"},
        };
        //9
        this.dataBody = new String[][]{
                {"Shell", "2", "Presenta fuga de aceite"},
                {"Camara Humeda", "1", "Se reviso"},
                {"Camara seca", "1", "Se reviso"},
                {"Refractarios", "1", "Se limpiaron"},
                {"Tuberia interna", "1", "OK"},
                {"Soldaduras", "1", "Se limpio"},
                {"Tapas Frontal y Trasera", "2", "Presenta venteo"},
                {"Manhole", "0", "NA"},
                {"Handhole", "1", ""},
                {"Empaques", "2", "manometro en valvula dañado"},
                {"Pintura", "1", "OK"},
                {"Aislamiento", "1", "OK"}
        };
        //10
        this.dataElectricalPanelControl = new String[][]{
                {"Programador", "2", "Presenta fuga de aceite"},
                {"Sensor de Llama", "1", "Se reviso"},
                {"Fotocelda", "1", "Se reviso"},
                {"Cableado general", "1", "Se limpiaron"},
                {"Corazas electricas", "1", "OK"},
                {"Breaker", "1", "Se limpio"},
                {"Fusibles", "2", "Presenta venteo"},
                {"Contactores", "0", "NA"},
                {"Reles", "1", ""},
                {"Terminales", "2", "manometro en valvula dañado"},
                {"Organización", "1", "OK"},
                {"Placas de señalizacion", "1", "OK"},
                {"Bombillos", "1", "OK"}
        };
        //11
        this.dataElectricalMotors = new String[][]{
                {"Ventilador", "2", "Presenta fuga de aceite"},
                {"Compresor", "1", "Se reviso"},
                {"Bomba de Agua", "1", "Se reviso"},
                {"Bomba de Combustible", "1", "Se limpiaron"},
                {"Rodamientos", "1", "OK"},
        };
        //12
        this.dataPipesAccessories = new String[][]{
                {"Aspecto general", "2", "Presenta fuga de aceite"},
                {"Soporteria", "1", "Se reviso"},
                {"Aislamiento", "1", "Se reviso"},
                {"Valvulas", "1", "Se limpiaron"},
                {"Trampas", "1", "OK"},
                {"Cheques", "1", "Se limpio"},
                {"Purgas", "2", "Presenta venteo"},
                {"Distribuidor", "0", "NA"},
        };
        //13
        this.dataObservations = new String[]{
                "* Se realizo Mantenimiento General, deshollinado y lavado interno",
                "*se reviso el sistema de gas natural , valvulas, swich de presion",
                "*Seinstalaron impelentes pero faltan algunos ya dañados",
                "* Se realizo mto a Macdonell y se cambio juego de nivel empa",
                "* Se bajo la unidad de encendido para mantenimiento",
                "* Se limpio fotocelda y demas elementos de encendido",
                "*Se reparo la tapa de manhole ya tenia desgaste",
                "* Se cambiaron los empaques de Hadhole ",
                "* Se cambiaro los empaques de Macdonell y gjuego de nivel",
                "* Se pinto el cuerpo y demas elementos de control",
                "*Se realizo ",
                "* se realizo analises de gases y calibracion de combustion",
                "* se pinto el cuerpo y controles",
                "* Se realizaron pruebas de seguridad.",
                "*Falla de llama, bajo nivel,alta baja presion de gas, aire de combustion.",

        };
        this.dataRecommendations = new String[]{
                "*Se recominda cambio de control de modulacion, impelentes",
                "* se recomienda cambio de manometros de presion de gas",
                "* se recomienda cambio de valvula de purga",
                "* se recomienda en un futuro cambio de regulador de gas",
                "* se recomienda realizar mantenimiento periodico y purgas",
                "* La caldera queda operando en perfectas condiciones",
                "* La Operación corre por cuenta del cliente",

        };
    }
    public void appViewPDF(Activity activity){
        if(pdfFile.exists()){
                Uri uri = FileProvider.getUriForFile(activity.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", getFile());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                try {
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                    Toast.makeText(activity.getApplicationContext(), "No cuentas con una aplicacion para visualizar PDF", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(activity.getApplicationContext(), "El archivo no se encontro", Toast.LENGTH_LONG).show();
        }
    }
    public File getFile() {
        if(pdfFile.exists()){
            return pdfFile;
        }
        return null;
    }
}
