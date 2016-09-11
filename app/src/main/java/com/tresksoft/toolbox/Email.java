package com.tresksoft.toolbox;

import android.content.Context;
import android.content.Intent;

public class Email{
	
	public void sendEmail(Context context, String address, String subject, String body) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {address});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
}
