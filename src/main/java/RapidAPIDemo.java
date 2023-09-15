import java.util.Scanner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import org.json.simple.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;



public class RapidAPIDemo {
	
	public static void main(String[] args) throws Exception {
		
		

		//I'm pretty sure this only works because we need the first 10, and it just lists 
		//a ton at once. 
		
		//REPLACE THE API KEY IF YOU'RE THE ONE RUNNING IT. THE "X-RAPIDAPI-KEY" PORTION.
		HttpResponse<String> response = Unirest.get("https://online-movie-database.p.rapidapi.com/title/get-top-rated-movies")
				.header("X-RapidAPI-Key", "")//same key as below. https://rapidapi.com/apidojo/api/online-movie-database
				.header("X-RapidAPI-Host", "online-movie-database.p.rapidapi.com")
				.asString();
		String resp = response.getBody();//converting this to a readable format
		Object obj = JSONValue.parse(resp);
		JSONArray array = (JSONArray)obj;
		String[] titleID = new String[10];
		
		for(int i = 0; i<=9;i++) {
			//We could clean this up later using the same method to grab data as shown later.
			titleID[i] = (array.get(i).toString()).substring(34,43);
			System.out.println(titleID[i]);
		}
		
		String[] titleNames = new String[10];
		String[] URLS = new String[10];
		//If there was a way to do this without making 10 individual calls that'd be great.
		for (int i = 0; i<=9; i++) {
			//REPLACE THE RAPIDAPI-KEY IF YOU'RE GOING TO RUN IT HERE AS WELL
			HttpResponse<String> response2 = Unirest.get("https://online-movie-database.p.rapidapi.com/title/get-details?tconst="+titleID[i])
					.header("X-RapidAPI-Key", "")//https://rapidapi.com/apidojo/api/online-movie-database java unirest key
					.header("X-RapidAPI-Host", "online-movie-database.p.rapidapi.com")
					.asString();
			
			String resp2 = response2.getBody();
			JSONObject jsonObj = new JSONObject(resp2);
			//Sidenote that we have to heirarchely traverse through the json file 
			JSONObject images =  jsonObj.getJSONObject("image");
			
			//Loads the json/s information on the to the string arrays.
			URLS[i]=  images.getString("url");
			titleNames[i] = jsonObj.getString("title");
			System.out.println(URLS[i]);
			System.out.println(titleNames[i]);
	
		}
		
		//main java frame, setting size/title
			JFrame f = new JFrame();
			f.setTitle("Top 10 movies");
			f.setSize(800,600);
			//creates a new jpanel to load images in
			JPanel image=new JPanel();

			image.setLayout(new BoxLayout(image, BoxLayout.Y_AXIS));

			for (String URL:URLS) {
				try {
					// for each image, load the image through the URl
					BufferedImage img = ImageIO.read(new URL(URL));
					//Makes new label for the panel
	                JLabel label = new JLabel(new ImageIcon(img));
					//adds the image to the label
	                image.add(label);
					}catch (IOException e) {
						e.printStackTrace();
					}
			}

			JScrollPane scrollPane = new JScrollPane(image);
			//adds scrollbar to the pane
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	        
	        f.add(scrollPane);
	        f.setVisible(true);
				
	}		
	
}
