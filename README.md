# pssms
Android SMS HTTP Gateway

Support commands:
* Send SMS
   
   http://IP-ADDRESS:9999/send?to=NUMBER&text=TEXT
   
* Reboot device (Root needed)
   
   http://IP-ADDRESS:9999/reboot
   
* Set HTTP callback URL
   
   http://IP-ADDRESS:9999/set_callback?url=YOUR_CALLBACK_URL
   
Support callbacks:
* Receive SMS
   
   http://YOUR_CALLBACK_URL:80/?from=NUMBER&status=STATUS&text=TEXT
   
* Receive SMS Sent status
   
   http://YOUR_CALLBACK_URL:80/?from=NUMBER&status=STATUS
   
* Receive SMS Delivered status
   
   http://YOUR_CALLBACK_URL:80/?from=NUMBER&status=STATUS
   
  
