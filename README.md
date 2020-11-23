# GunbrokerBot

A terminal tool used to querey items from Gunbroker.com then sends Email and Text alerts .

## Requirements

- Java Version 11.0.9.1 (Not sure if previous versions work but its my version that works)
- Java MailAPI
- Java ActivationAPI
- Mailutils

## Setting Up

For this program to work one of the things you'll have to have is a working 
SMTP server on your device. I got mine working with mailutils `apt install mailutils`.

With mailutils installed I set it up so that it uses my personal email as a relay for
sending the messages. To achieve this I followed this tutorial 

https://www.howtoforge.com/tutorial/configure-postfix-to-use-gmail-as-a-mail-relay/

To compile and run the program you will need to include the Java MailAPI and ActivationAPI in your classpath.

This can be done by,

```javac -cp mail.jar:activation.jar GunBroker.java```

```java -cp mail.jar:activation.jar Gunbroker```


## Running the program

Once the program starts up the first time it will prompt you for some information
that the program will need to run, this includes
-Your phone number
-The email you want notified
-The email sending the notifications (it can be the same as the notify email)
-Your Cell carrier

It will take those and save it to a config file so you will not have to put them every time the program is run.

From there you will be taken to the main menu of the program. You must add items before running the bot (obviously).
Once you run the bot it will run on a seperate thread so you should be able to navigate the menu while the bot is running.
You can leaev the terminal open and the bot will continue to run until you choose to quit the program.

If the price of any of the tracked items changed you will get an email and text alert informing 
you of the Item, the new price, and the link to the item. 

### Future Upgrades
-Better Menuing
-More details and attributes for each item (make, model, caliber, etc)
-A viewing mode for each item
-Set the alert level for individual items
-Actually delete items
-More error checking
-Potential auto betting on Items?
