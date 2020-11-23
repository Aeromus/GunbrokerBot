import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.activation.*;
import javax.mail.internet.*;
import javax.mail.*;

public class GunBroker implements Runnable{

	//Globals
	static Map<String, String> cellCarrierDic = new HashMap<String, String>();
	static ArrayList<String> itemurls = new ArrayList<>();
	static ArrayList<Item> items = new ArrayList<>();
	static String botEmail;
	static String phoneNum;
	static String notifyEmail;
	static String cellCarrier;
	static boolean isrunning;

	public static void main(String[] args) throws IOException{

	/*	if(args.length == 0 || args[0].equalsIgnoreCase("-h")){

			System.out.println("java GunBroker <URL> \n\t URL must be a valid item listing on Gunbroker.com \n\t Used to display info about an auction or sale page.");
			System.exit(1);
		}
	*/
	
		//Loading Important Files and vars
		buildCarrierDict();
		loadConfig();
		loadItems();

		//Clear the terminal screen for fancy-ness
		System.out.print("\033[H\033[2J");
		System.out.flush();

		//Startup Titling
		boolean end = false;
		isrunning = false;
		Thread bot = new Thread(new GunBroker());
		String figletTitle = " ____            _                  ____        _   " 
						   + "\n| __ ) _ __ ___ | | _____ _ __     | __ )  ___ | |_ "
						   + "\n|  _ \\| '__/ _ \\| |/ / _ \\ '__|____|  _ \\ / _ \\| __|"
						   + "\n| |_) | | | (_) |   <  __/ | |_____| |_) | (_) | |_ "
						   + "\n|____/|_|  \\___/|_|\\_\\___|_|       |____/ \\___/ \\__| ";

		System.out.println(figletTitle);
		System.out.println("\n\t︻デ═一 Created By: Andrew Knoblach");

		String menuString ="\n\nBrokerbot Menu:\n[1] Add items\n[2] List items\n[3] Delete items\n[4] Rebuild Config File \n[5] Start/Stop bot\n[6] Exit\n\n>";

		//Main menu loop
		while(!end){
			//Menu options
			Scanner scan = new Scanner(System.in);
			System.out.print(menuString);
			int choice = scan.nextInt();
			if(choice ==  1){
				addItem();
			}
			if(choice == 2){
				listItems();
			}
			if(choice == 3){
				//Nothing yet
			}
			if(choice == 4 ){
				genConfig();
			}
			if(choice == 5){
				//Starting the bot
				if(!isrunning){
					bot.start();
					isrunning = true;
					System.out.println("Broker bot started running in the background!");
				}
				else{
					isrunning = false;
					System.out.println("Broker bot has stopped.");
				}
			}
			if(choice == 6){
				isrunning = false;
				end = true;
			}
		}
		

		/*
		 * try{
		 * 
		 * //Getting the Data URL target = new URL(args[0]); InputStream in =
		 * target.openStream(); String HTML = new String(in.readAllBytes()); in.close();
		 * 
		 * //Regex Parseing
		 * 
		 * System.out.println("Getting Title ...");
		 * 
		 * Pattern itemTitlePattern = Pattern.compile("title: \"(.*?)\"",
		 * Pattern.CASE_INSENSITIVE); Matcher m = itemTitlePattern.matcher(HTML);
		 * m.find(); String title = ""; if(m.groupCount() > 0) title = m.group(1);
		 * 
		 * System.out.println("Getting ID ...");
		 * 
		 * Pattern itemIDPattern = Pattern.compile("itemID: (\\d*)"); Matcher n =
		 * itemIDPattern.matcher(HTML); n.find(); String ID = ""; if(n.groupCount() >
		 * 0); ID = n.group(1);
		 * 
		 * 
		 * System.out.println("Getting End Date ..."); Pattern endingDatePattern =
		 * Pattern.compile("endingDate: \"(.*?)\""); Matcher l =
		 * endingDatePattern.matcher(HTML); l.find(); String endingDate = "";
		 * if(l.groupCount() > 0) endingDate = l.group(1);
		 * 
		 * System.out.println("Getting Price ..."); Pattern pricePattern =
		 * Pattern.compile("price: ([^,]*)"); Matcher j = pricePattern.matcher(HTML);
		 * j.find(); String price = ""; if(j.groupCount() > 0) price = j.group(1);
		 * 
		 * System.out.println("Getting Manufacturer ..."); Pattern manuPattern =
		 * Pattern.compile("manufacturer: \"(.*?)\""); Matcher k =
		 * manuPattern.matcher(HTML); k.find();
		 * 
		 * String manufacturer = ""; if(k.groupCount() > 0) manufacturer = k.group(1);
		 * 
		 * System.out.println("Getting Model ..."); Pattern modelPattern =
		 * Pattern.compile("model: \"(.*?)\""); Matcher q = modelPattern.matcher(HTML);
		 * q.find(); String model = ""; if(q.groupCount() > 0 ) model = q.group(1);
		 * 
		 * System.out.println("Getting Caliber ..."); Pattern caliberPattern =
		 * Pattern.compile("caliber: \"(.*?)\""); Matcher r =
		 * caliberPattern.matcher(HTML); r.find()System.out.
		 * println("No Config file found, either it was delete or this is your first time running brokerbot"
		 * ); System.out.println("To create a new config press any key....");
		 * System.in.read(); genConfig(); ; String caliber = ""; if(r.groupCount() > 0)
		 * caliber = r.group(1);
		 * 
		 * 
		 * //Regex for End date not working Fix some other time //Output
		 * 
		 * System.out.println("\nInformation for Gunbroker Item");
		 * System.out.println("<---------------------------->");
		 * System.out.println("\tTitle:        " + title);
		 * System.out.println("\tItem ID:      " + ID);
		 * System.out.println("\tEnding Date:  " + endingDate);
		 * System.out.println("\tPrice:       $" + price);
		 * 
		 * System.out.println("\nItem Specs");
		 * System.out.println("<--------------------------->");
		 * System.out.println("\tManufacturer: " + manufacturer);
		 * System.out.println("\tModel       : " + model);
		 * System.out.println("\tCaliber     : " + caliber);
		 * 
		 * 
		 * } catch(Exception e){
		 * 
		 * System.out.println("Error:\n\t" + e);
		 * 
		 * }
		 */			

		 saveItems();
		 System.exit(0);
	}


	public static void loadConfig() throws IOException{

		//Load Settings from config
		System.out.println("Loading Config...");
		try{
		File settings = new File("BrokerBot.conf");
		Scanner scan = new Scanner(settings);
		String prestring =  scan.nextLine();
		botEmail = prestring.split("=")[1];
		prestring = scan.nextLine();
		notifyEmail = prestring.split("=")[1];
		prestring = scan.nextLine();			
		phoneNum = prestring.split("=")[1];
		prestring = scan.nextLine();			
		cellCarrier = prestring.split("=")[1];
		scan.close();
		System.out.println("Config Loaded Successfully!");
		return;
		}
		catch(Exception e){
			// If there is no config prompt creation
			System.out.println("No Config file found, either it was delete or this is your first time running brokerbot");
			System.out.println("To create a new config press any key....");
			System.in.read();
			genConfig();
			return;
		} 
	}

	public static void genConfig() throws IOException{
		//Generate the config file
		FileWriter config = new FileWriter("BrokerBot.conf");
		Scanner scan = new Scanner(System.in);
		System.out.println("Bot Sending Email: ");
		String botEmail_ = scan.nextLine();
		System.out.println("Recciever Email: ");
		String notifyEmail_ = scan.nextLine();
		System.out.println("Phone number (entered as 10 digits, no punctuation): ");
		String phoneNum_ = scan.nextLine();
		String carrier;
		System.out.println("Which of these is your phone provider: ");
		System.out.println("[1] AT&T\n[2] Boost Mobile\n[3] Cricket Wireless\n[4] Google Project Fi\n[5] Sprint\n[6] T-Mobile\n[7] Verizon\n[8] Virgin Mobile");
		int s =  scan.nextInt();
		scan.close();
		switch(s){
			case 1:
				carrier = "AT&T";
				break;
			case 2:
				carrier = "Boost Mobile";
				break;
			case 3:
				carrier = "Cricket Wireless";
				break;
			case 4:
				carrier = "Google Project Fi";
				break;
			case 5:
				carrier = "Sprint";
				break;
			case 6:
				carrier = "T-Mobile";
				break;
			case 7:
				carrier = "Verizon";
				break;
			case 8:
				carrier = "Virgin Mobile";
				break;
			default:
				carrier = "Other";
				break;
		}
		config.write("botEmail=" + botEmail_ + "\n");
		config.write("notifyEmail=" + notifyEmail_ + "\n");
		config.write("phone=" + phoneNum_ + "\n");
		config.write("cellCarrier=" + carrier + "\n");
		config.close();
		System.out.println("Created config file!");
		return;
	}


	public static void loadItems() throws IOException{
		//Try to load a list of items
		try{
			System.out.println("Loading items...");
			File itemList = new File("BrokerItems.txt");
			Scanner scan = new Scanner(itemList);
			while(scan.hasNextLine()){
				itemurls.add(scan.nextLine());
			}
			for(String url : itemurls){
					URL target =  new URL(url);
					InputStream in = target.openStream();
					String HTML = new String(in.readAllBytes());
					String title = reTitle(HTML);
					String id = reID(HTML);
					String endTime = reEndTime(HTML);
					double price = rePrice(HTML);
					boolean active = reActive(HTML);
					items.add(new Item(url, id, title, price, endTime, active));
			}
			System.out.println("Items Loaded Successfully!");
			return;
		}
		catch(Exception e){
			//If no items either ask to add or quit
			System.out.println("ERROR: " + e);
			System.out.println("No items to load!");
		}
	}

	public static void saveItems() throws IOException{
		//Saves Items to a file
		System.out.println("Saving Items...");
		FileWriter itemList = new FileWriter("BrokerItems.txt");
		for(String s : itemurls){
			itemList.write(s + "\n");
		}
		itemList.close();
		System.out.println("Items Saved!");
		return;
	}

	public static void deleteItem(){
		//Gives the option to delete an item
		// Will probably implement this after Final project as not 100% nessicary for mvp
	}

	public static void listItems(){
		//Shows the current items
		for(Item i : items){
			System.out.println("Title: " + i.title);
			System.out.println("ID: " + i.id);
			System.out.println("URL: " + i.url);
			System.out.println("Price : $" +i.price);
			System.out.println("<------------------------------>");
		}
	}

	//Potentally rework this for malformed url Exception & to verify if the site is actually Gunbroker
	public static void addItem() throws IOException, MalformedURLException{
		//
		boolean isValid = false;
		String url = "";
		String HTML = "";
		while(!isValid){
			System.out.print("\nPlease provide a link to the item you wish to track:\n\n>");
			Scanner scan = new Scanner(System.in);
			url = scan.nextLine();
			Pattern GunBrokerPattern = Pattern.compile("https?://www.gunbroker.com/item/.*",Pattern.CASE_INSENSITIVE);
			Matcher mm = GunBrokerPattern.matcher(url);
			if(mm.find()){
				try{
					URL target =  new URL(url);
					InputStream in = target.openStream();
					HTML = new String(in.readAllBytes());
					System.out.println("Verifying URL...");
					Pattern Pattern404 = Pattern.compile("<title>GunBroker.com - 404</title>",Pattern.CASE_INSENSITIVE);
					Matcher m = Pattern404.matcher(HTML);
					isValid = true;
					//404 page not found
				 	if(m.find()){
						System.out.println("The page you are looking for could not be found\nPlease check the URL and try again");
						isValid = false;
					}				 	
				}
				catch(MalformedURLException e){
					System.out.println("Error: Invalid URL");	
				}
			}
			else{
				System.out.println("Please enter a URL for Gunbroker.com.\nThe URL for item listings looks like: https://www.gunbroker.com/item/12345678");
			}
		}
		//Creating an item object and verifying 
		String title = reTitle(HTML);
		String id = reID(HTML);
		String endTime = reEndTime(HTML);
		double price = rePrice(HTML);
		boolean active = reActive(HTML);

		System.out.println("Is this the correct item?(y/n)");
		System.out.println("------------------------------\n");
		System.out.print("\nTitle: " + title + "\nID: " + id + "\nPrice: $" + price + "\n\n>");
		Scanner scan = new Scanner(System.in);
		String choice = scan.nextLine();
		if(choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("Yes")){
			items.add(new Item(url, id, title, price, endTime, active));
			itemurls.add(url);
			System.out.println("Item Added Succesfully!");
			return;
		}
		else
			return;
	}

	public static void buildCarrierDict(){
		cellCarrierDic.put("AT&T","@txt.att.net");
		cellCarrierDic.put("Boost Mobile","@sms.myboostmovile.com");
		cellCarrierDic.put("Cricket Wireless", "@mms.criketwireless.net");
		cellCarrierDic.put("Google Projet Fi","@msg.fi.google.com");
		cellCarrierDic.put("Sprint","@messaging.sprintpcs.com");
		cellCarrierDic.put("T-Mobile","@tmomail.net");
		cellCarrierDic.put("Verizon", "@vtext.com");
		cellCarrierDic.put("Virgin Mobile", "@vmobl.com");
		return;
	}

	public static String reTitle(String HTML){
		System.out.println("Fetching Title...");
		Pattern itemTitlePattern = Pattern.compile("title: \"(.*?)\"",Pattern.CASE_INSENSITIVE);
		Matcher m = itemTitlePattern.matcher(HTML);
		m.find();
		String title = ""; 
		if(m.groupCount() > 0) 
			title = m.group(1);
		return title;
	}

	public static String reID(String HTML){
		System.out.println("Fetching ID...");
		Pattern itemIDPattern = Pattern.compile("itemID: (\\d*)");
		Matcher n = itemIDPattern.matcher(HTML); n.find();
		String ID = "";
		if(n.groupCount() > 0);
		  ID = n.group(1);
		return ID;
	}

	public static String reEndTime(String HTML){
		System.out.println("Fetching end date...");
		Pattern endingDatePattern = Pattern.compile("endingDate: \"(.*?)\""); 
		Matcher l = endingDatePattern.matcher(HTML); 
		l.find(); 
		String endingDate = "";
		if(l.groupCount() > 0)
		 endingDate = l.group(1);
		return endingDate;
	}

	public static Boolean reActive(String HTML){
		System.out.println("Fetching activation status...");
		Pattern activePattern = Pattern.compile("isActive: (.*?),");
		Matcher m = activePattern.matcher(HTML);
		m.find();
		if(m.groupCount() > 0 && m.group(1).equalsIgnoreCase("true"))
			return true;
		return false;
	}

	public static double rePrice(String HTML){
		System.out.println("Fetching price...");
		//Had to change this patteren because it seems that the data tag at the top isn't always 100% accurate, hopefully this is
		//Pattern pricePattern = Pattern.compile("price: ([^,]*)");
		Pattern pricePattern = Pattern.compile("<div id=\"CurrentBid\" class=\"value\">\n\\$(\\d*.\\d*) <\\/div>");
		Matcher j = pricePattern.matcher(HTML);
		j.find();
		double price = -1.0;
		if(j.groupCount() > 0)
			price = Double.parseDouble(j.group(1));
		return price;
	}

	public static class Item{

		double price;
		String url;
		String id;
		String title;
		String endDate;
		boolean active;

		Item(String url, String id, String title, double price, String endDate, boolean active){
			this.url = url;
			this.id = id;
			this.title = title; 
			this.endDate = endDate;
			this.active = active;
			this.price = price;
		}
	}

	public static void checkItems() throws MalformedURLException, IOException, InterruptedException{

		//Will later expand this to be much more exaustive but for terms of final project this is a good start.
		//System.out.println("\n Checking items ....\n\n");
		for(Item i :  items){
			URL target =  new URL(i.url);
			InputStream in = target.openStream();
			String HTML = new String(in.readAllBytes());
			double newPrice = rePrice(HTML);
			boolean newActive = reActive(HTML);
			if(newPrice > i.price){
				//Ring terminal bell, Using this for debugging;
				System.out.println((char)7);
				sendEmailPriceAlert(i.title, i.url,newPrice);
				sendTextPriceAlert(i.title,i.url,newPrice);
				i.price = newPrice;
			}
			//Added this to try not to spam requests quickly, hopefully I don't piss off the website
			Thread.sleep(10000);
		}

		return;
	}

	//
	public static boolean sendEmailPriceAlert(String title, String url, double price){

		String host = "localhost";

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		try {

			Message message = new MimeMessage(session);
			String body = "The price on " + title + " has changed to $" + price + "\n" + url;
			message.setFrom(new InternetAddress(botEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(notifyEmail));
			message.setSubject("Broker-Bot Alert!");
			message.setText(body);

			Transport.send(message);

			return true;
		}
		catch(Exception e){

			System.out.println("ERROR: "  + e);
			return false;
		}
	}

	public static boolean sendTextPriceAlert(String title,String url, double price){

		String txtaddr = getTextAddress();
		String host = "localhost";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		try {
			Message message = new MimeMessage(session);
			String body = "The price on " + title + " has changed to $" + price + "\n"  + url;
			message.setFrom(new InternetAddress(botEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(txtaddr));
			message.setSubject("Broker-Bot");
			message.setText(body);

			Transport.send(message);
			return true;
		}
		catch(Exception e){
			System.out.println("ERROR: " +e);
			return false;
		}
	}

	public static String getTextAddress(){
		return phoneNum + cellCarrierDic.get(cellCarrier);
	}

	public void run(){
		//int loops = 0;
		while(isrunning){
			try{
				//for Debugging
				//System.out.println("____________________________________________________________________________________\n\n\n LOOPS = " + loops + "\n\n\n\n");
				checkItems();
				//5 minutes in Miliseconds, This may be subject to change later with settings or something
				Thread.sleep(300000);
				//loops++;
			}
			catch(Exception e){
				System.out.println("ERROR: "  +e);
			}
		}
	}
}