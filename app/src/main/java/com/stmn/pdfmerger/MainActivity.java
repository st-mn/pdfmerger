package com.stmn.pdfmerger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_PDF_FILES = 1;
    private Button selectPDF;
    private Button mergePDF;
    private List<Uri> mSelectedPdfs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectPDF = findViewById(R.id.select_pdf_button);
        mergePDF = findViewById(R.id.merge_pdf_button);

        selectPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent, REQUEST_PICK_PDF_FILES);
            }
        });

        mergePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedPdfs.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please select file!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{

                    String filename = System.currentTimeMillis() + "_merged.pdf";

                    File mergedPdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filename);

                    FileOutputStream outputStream = new FileOutputStream(mergedPdfFile);

                    Document document = new Document();

                    PdfCopy copy = new PdfCopy(document,outputStream);

                    document.open();

                    for(Uri pdfUri: mSelectedPdfs){
                        PdfReader reader = new PdfReader(getContentResolver().openInputStream(pdfUri));
                        copy.addDocument(reader);
                        reader.close();
                    }

                    document.close();
                    outputStream.close();
                    Toast.makeText(MainActivity.this,"PDF files merged successfully.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Intent.ACTION_VIEW);

                  // Uri fileUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", mergedPdfFile);
                    Uri fileUri = FileProvider.getUriForFile(MainActivity.this,"com.stmn.pdfmerger.provider", mergedPdfFile);

                    intent.setDataAndType(fileUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                   // Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_PICK_PDF_FILES && resultCode == RESULT_OK){
            mSelectedPdfs.clear();
            if(data.getData() != null){
                mSelectedPdfs.add(data.getData());
            }else if(data.getClipData() != null){
                for(int i=0; i<data.getClipData().getItemCount(); i++){
                    mSelectedPdfs.add(data.getClipData().getItemAt(i).getUri());
                }
            }
            Toast.makeText(this,"Selected " + mSelectedPdfs.size() + "PDF Files.", Toast.LENGTH_SHORT).show();

        }

    }
}