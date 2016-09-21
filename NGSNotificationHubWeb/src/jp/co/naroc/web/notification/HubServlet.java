package jp.co.naroc.web.notification;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpPost;

import com.windowsazure.messaging.AppleRegistration;
import com.windowsazure.messaging.NamespaceManager;
import com.windowsazure.messaging.Notification;
import com.windowsazure.messaging.NotificationHub;
import com.windowsazure.messaging.NotificationHubDescription;

public class HubServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;



	public HubServlet() {
		super();
	}

	String endpoint = "";
	String SasKeyName = "";
	String SasKeyValue ="";
	 private static final String APIVERSION = "?api-version=2013-10";
	 private static final String CONTENT_LOCATION_HEADER = "Location";
	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String hubPath="samplePushNotifications";
		//String connectionString = "Endpoint=sb://samplepushnotificationsns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=O5gMt7Fp+ZJk+YHmZMB7TDpAShBQUYZy5baqCuz8d+E=";
		String connectionString = "Endpoint=sb://samplepushnotificationsns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=hkNBNedPPL4Pag5GnKxPMsIlSulkjFv7Z/oMO9k4+XA=";



		NotificationHub hub = new NotificationHub(connectionString,hubPath );

//		AppleRegistration reg = new AppleRegistration("EF41BA95A9F1BFDD241658DAA8FA1AF12A0FD6F3");
//		reg.getTags().add("myTag");
//		reg.getTags().add("myOtherTag");
//		hub.upsertRegistration(reg);

	  Calendar c = Calendar.getInstance();
	  c.add(Calendar.DATE, 1);
//	  Notification n = Notification.createWindowsNotification("WNS body");
//	  hub.scheduleNotification(n, c.getTime());


		String alert = "{\"aps\":{\"alert\":\"こんにちわ\"}}";
//		String alert = "";

		Notification n2 = Notification.createAppleNotifiation(alert);
		hub.sendNotification(n2);


		return;

	}

	 private String generateSasToken(URI uri) {
		  String targetUri;
		  try {
		   targetUri = URLEncoder
		     .encode(uri.toString().toLowerCase(), "UTF-8")
		     .toLowerCase();

		   long expiresOnDate = System.currentTimeMillis();
		   int expiresInMins = 60; // 1 hour
		   expiresOnDate += expiresInMins * 60 * 1000;
		   long expires = expiresOnDate / 1000;
		   String toSign = targetUri + "\n" + expires;

		   // Get an hmac_sha1 key from the raw key bytes
		   byte[] keyBytes = SasKeyValue.getBytes("UTF-8");
		   SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

		   // Get an hmac_sha1 Mac instance and initialize with the signing key
		   Mac mac = Mac.getInstance("HmacSHA256");
		   mac.init(signingKey);

		   // Compute the hmac on input data bytes
		   byte[] rawHmac = mac.doFinal(toSign.getBytes("UTF-8"));

		   // Convert raw bytes to Hex
		   String signature = URLEncoder.encode(
		     Base64.encodeBase64String(rawHmac), "UTF-8");

		   // construct authorization string
		   String token = "SharedAccessSignature sr=" + targetUri + "&sig="
		     + signature + "&se=" + expires + "&skn=" + SasKeyName;
		   return token;
		  } catch (Exception e) {
		   throw new RuntimeException(e);
		  }
		 }
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
