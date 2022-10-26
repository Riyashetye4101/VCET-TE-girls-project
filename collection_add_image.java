/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recogfacematch;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Akash Sahu
 */
public class collection_add_image {
    public static final String collectionId = "Records";
    public static final String bucket = "riyashetye";
//    public static final String photo = "f1-006-01.jpg";=
    
    
    public static List<String> getFiles(){
            List<String> results = new ArrayList<String>();

            
            File[] files = new File("C:/Users/Riyas/Downloads/Project Code (forensic face sketch)/Project Code (forensic face sketch)/recog/src/main/java/com/mycompany/recogfacematch").listFiles();
            //If this pathname does not denote a directory, then listFiles() returns null. 

            for (File file : files) {
                if (file.isFile()) {
                    String filename=file.getName();
                    int ind=filename.lastIndexOf(".");
                    if(filename.charAt(ind+2)=='p'){
                        
                    results.add(file.getName());
                    System.out.println(file.getName());
                    }
                }
            }
            return results;
    }
    public static void main(String[] args) throws Exception {

        List<String> photos=getFiles();
        for(String photo:photos){
           AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

           Image image = new Image()
                   .withS3Object(new S3Object()
                   .withBucket(bucket)
                   .withName(photo));

           IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                   .withImage(image)
                   .withCollectionId(collectionId)
                   .withExternalImageId(photo)
                   .withDetectionAttributes("DEFAULT");

           IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

           System.out.println("Results for " + photo);
           System.out.println("Faces indexed:");
           List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
           for (FaceRecord faceRecord : faceRecords) {
               System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
               System.out.println("  Location:" + faceRecord.getFaceDetail().getBoundingBox().toString());
            }
           }
       
    }
}
