package autocald.itexttests;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF extends FileProvider{
    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle= new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fSubTitle= new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText= new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fHighText= new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);
    public TemplatePDF(Context context) {
        this.context=context;
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
            document=new Document(PageSize.A4);
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
    public void addTitles(String title, String subTitle, String date){
        try {
        paragraph=new Paragraph();
        addChild(new Paragraph(title, fTitle));
        addChild(new Paragraph(subTitle, fSubTitle));
        addChild(new Paragraph("Generado el" + date, fHighText));
        paragraph.setSpacingAfter(30);
        document.add(paragraph);
        }catch (Exception e){
            Log.e("addTitles", e.toString());
        }
    }
    private void addChild(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }
    public void addParagraph(String text){
        try {
        paragraph=new Paragraph(text, fText);
        paragraph.setSpacingAfter(5);
        paragraph.setSpacingBefore(5);
        document.add(paragraph);
        }catch (Exception e){
            Log.e("addParagraph", e.toString());
        }
    }
    public void createTable(String[] header, ArrayList<String[]> client){
        try {
        paragraph=new Paragraph();
        paragraph.setFont(fText);
        PdfPTable pdfPTable=new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        PdfPCell pdfPCell;
        int indexC=0;
        while (indexC<header.length){
            pdfPCell=new PdfPCell(new Phrase(header[indexC++], fSubTitle));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.GREEN);
            pdfPTable.addCell(pdfPCell);
        }
        for(int indexR=0; indexR<client.size(); indexR++){
            String[]row= client.get(indexR);
            for(indexC=0; indexC<header.length; indexC++){
                pdfPCell=new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(40);
            }
        }
        paragraph.add(pdfPTable);
        document.add(paragraph);
        }catch (Exception e){
            Log.e("createTable", e.toString());
        }
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
