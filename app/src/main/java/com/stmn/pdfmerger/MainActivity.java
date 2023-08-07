/*
Android application written in Java that allows users to merge multiple PDF files into a single PDF file. 
The code starts with package and import statements, which bring in necessary Android and third-party libraries for PDF manipulation.
The MainActivity class extends AppCompatActivity, which is the base class for activities that use the support library action bar features.

Member variables:
private static final int REQUEST_PICK_PDF_FILES: A constant integer used as a request code when selecting PDF files from the file picker.
private Button selectPDF: A button used to trigger the PDF selection process.
private Button mergePDF: A button used to initiate the merging process.
private List<Uri> mSelectedPdfs: A list to store the URIs (Uniform Resource Identifiers) of the selected PDF files.
In the onCreate method, the layout is set using setContentView(R.layout.activity_main).

Two click listeners are assigned to the selectPDF and mergePDF buttons. 
The selectPDF button's listener opens the file picker to allow the user to select one or more PDF files, 
and the mergePDF button's listener initiates the merging process.

When the user selects PDF files in the file picker, the onActivityResult method is called. 
The selected URIs are stored in the mSelectedPdfs list, and a toast is displayed to indicate the number of selected PDF files.
When the user clicks the mergePDF button, the onClick method inside the click listener is executed.

In the mergePDF method:
It first checks if any PDF files are selected. If not, it displays a toast message prompting the user to select at least one file.
Then, it creates a new PDF file with a unique filename in the "Documents" directory using the current timestamp as part of the filename.
It opens an output stream to write the merged PDF content.
A new Document and PdfCopy objects are created from the iTextPDF library. 
These objects will be used to handle the merging process.
The selected PDF files' URIs are looped through, and each PDF file is opened using PdfReader. 
The content of each PDF file is copied to the new PdfCopy object.
After copying all PDFs, the document is closed, and the output stream is closed.
A toast message is displayed indicating the successful merge.
Finally, an Intent is created to view the merged PDF using a PDF viewer app. 
The FileProvider is used to create a content URI for the merged PDF file, granting read permissions to the viewer app.
If an exception occurs during the merging process, it is caught and printed to the console (e.g., logcat) for debugging purposes.
*/


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
            @Override //Open document dialog to select pdf files
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
                    //Create new pdf file into which we will merge
                    String filename = System.currentTimeMillis() + "_merged.pdf";
                    File mergedPdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filename);
                    FileOutputStream outputStream = new FileOutputStream(mergedPdfFile);
                    Document document = new Document();
                    PdfCopy copy = new PdfCopy(document,outputStream);
                    document.open();
                    
                    //Add all files in new merged pdf file
                    for(Uri pdfUri: mSelectedPdfs){
                        PdfReader reader = new PdfReader(getContentResolver().openInputStream(pdfUri));
                        copy.addDocument(reader);
                        reader.close();
                    }
                    //Close and save new merged pdf file
                    document.close();
                    outputStream.close();
                    Toast.makeText(MainActivity.this,"PDF files merged successfully.", Toast.LENGTH_SHORT).show();
                    
                    //Open and display new merged pdf file
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri fileUri = FileProvider.getUriForFile(MainActivity.this,"com.stmn.pdfmerger.provider", mergedPdfFile);
                    intent.setDataAndType(fileUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                   
                }
            }
        });

    }

    @Override 
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Display message with number of selected files
        //TODO: expand to allow reordering of files
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
