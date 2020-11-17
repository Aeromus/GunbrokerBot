import java.io.*;
import java.net.*;
import java.util.*;

public class GunBroker{


	
	public static void main(String[] args) throws IOException{

		if(args.length == 0 || args[0].equalsIgnoreCase("-h")){

			System.out.println("java GunBroker <URL> \n\t URL must be a valid item listing on Gunbroker.com \n\t Used to display info about an auction or sale page.");

		}

		

		try{

			//Getting the Data
			URL target = new URL(args[0]);
			InputStream in = target.openStream();
			String HTML = new String(in.readAllBytes());
			in.close();

			//Regex Parseing
			
			System.out.println("Getting Title ...");

			Pattern itemTitlePattern = Pattern.compile("title: \"(.*?)\"", Pattern.CASE_INSENSITIVE);
			Matcher m = itemTitlePattern.matcher(HTML);
			m.find();
			String title = "";
			if(m.groupCount() > 0)
				title = m.group(1);
			
			System.out.println("Getting ID ...");

			Pattern itemIDPattern = Pattern.compile("itemID: (\\d*)");
			Matcher n = itemIDPattern.matcher(HTML);
			n.find();
			String ID = "";
		        if(n.groupCount() > 0);	
				ID = n.group(1);


			System.out.println("Getting End Date ...");
			Pattern endingDatePattern = Pattern.compile("endingDate: \"(.*?)\"");
			Matcher l = endingDatePattern.matcher(HTML);
			l.find();
			String endingDate =  "";
			if(l.groupCount() > 0)
				endingDate = l.group(1);

			System.out.println("Getting Price ...");
			Pattern pricePattern = Pattern.compile("price: ([^,]*)");
			Matcher j = pricePattern.matcher(HTML);
			j.find();
			String price = "";
			if(j.groupCount() > 0)       
				price = j.group(1);

			System.out.println("Getting Manufacturer ...");
			Pattern manuPattern = Pattern.compile("manufacturer: \"(.*?)\"");
			Matcher k = manuPattern.matcher(HTML);
			k.find();

			String manufacturer = "";
			if(k.groupCount() > 0)
				manufacturer = k.group(1);

			System.out.println("Getting Model ...");
			Pattern modelPattern = Pattern.compile("model: \"(.*?)\"");
			Matcher q = modelPattern.matcher(HTML);
			q.find();
			String model =  "";
			if(q.groupCount() > 0 )
				model = q.group(1);

			System.out.println("Getting Caliber ...");
			Pattern caliberPattern = Pattern.compile("caliber: \"(.*?)\"");
			Matcher r = caliberPattern.matcher(HTML);
			r.find();
			String caliber = "";
			if(r.groupCount() > 0)
				caliber = r.group(1);


			//Regex for End date not working Fix some other time
			//Output
			
			System.out.println("\nInformation for Gunbroker Item");
			System.out.println("<---------------------------->");
			System.out.println("\tTitle:        " + title);
			System.out.println("\tItem ID:      " + ID);
			System.out.println("\tEnding Date:  " + endingDate);
			System.out.println("\tPrice:       $" + price);

			System.out.println("\nItem Specs");
			System.out.println("<--------------------------->");
			System.out.println("\tManufacturer: " + manufacturer);
			System.out.println("\tModel       : " + model);
			System.out.println("\tCaliber     : " + caliber);


		}
		catch(Exception e){

			System.out.println("Error:\n\t" + e);
		
		}
	}


	public void loadConfig(){

		//Load Settings from config
		try{
		File settings = new File("BrokerBot.conf");
		Scanner scan = new Scanner(settings);
		
		}
		catch(Exception e){
			// If there is no config prompt creation
		} 
		return;
	}

	public void genConfig(){
		//Generate the config file

		return;
	}


	public void loadItems(){
		//Try to load a list of items
		try{
			File itemList = new File("BrokerItems.txt");
			Scanner scan = new Scanner(itemList);

		}
		catch(Exception e){
			//If no items either ask to add or quit
			System.out.println("");
		}
	}

	public void saveItems(){
		//Saves Items to a file

	}

	public void addItem(){


	}

	//Cellphone carrier Dictionary, Used for sending texts
	public static Map<String, String> cellCarrierDic = new HashMap<String, String>();
	cellCarrierDic.put("AT&T","@txt.att.net");
	cellCarrierDic.put("Boost Mobile","@sms.myboostmovile.com");
	cellCarrierDic.put("Cricket Wireless", "@mms.criketwireless.net");
	cellCarrierDic.put("Google Projet Fi","@msg.fi.google.com");
	cellCarrierDic.put("Sprint","@messaging.sprintpcs.com");
	cellCarrierDic.put("T-Mobile","@tmomail.com");
	cellCarrierDic.put("Verizon", "@vtext.com");
	cellCarrierDic.put("Virgin Mobile", "@vmobl.com");




}
