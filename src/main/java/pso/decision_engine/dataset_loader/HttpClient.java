package pso.decision_engine.dataset_loader;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {

	static public void send(Config c, File f) throws IOException {
		
		String authorizationHeader=null;
		if (c.getDecisionEngineUser()!=null && !c.getDecisionEngineUser().isEmpty()) {
			authorizationHeader="Basic "+Base64.getEncoder().encodeToString((c.getDecisionEngineUser()+":"+c.getDecisionEnginePassword()).getBytes());
		}
		
		OkHttpClient.Builder builder=new OkHttpClient.Builder()
			.writeTimeout(1, TimeUnit.HOURS)
			.readTimeout(1, TimeUnit.HOURS)
			.connectTimeout(1, TimeUnit.HOURS)
		    .hostnameVerifier(new HostnameVerifier() {
		      @Override
		      public boolean verify(String hostname, SSLSession session) {
		        return true;
		      }
		    });

		if (c.isUseProxyServer()) {
			builder=builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(c.getProxyServerHost(), c.getProxyServerPort())));
		};
		
		OkHttpClient okHttpClient=builder.build();
		
		Request.Builder requestBuilder=new Request.Builder()
        .url(c.getDecisionEngineUrl()+"/upload/"+c.getDataSetType()+"/"+URLEncoder.encode(c.getDataSetName(),"UTF-8").replace("+", "%20"))
        .post(RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), f));
        if (authorizationHeader!=null) {
        	requestBuilder=requestBuilder.addHeader("Authorization", authorizationHeader);
        }
 		
		Request request=requestBuilder.build();
	    try (Response response = okHttpClient.newCall(request).execute()) {
	        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

	        System.out.println("Response from Decision Engine: "+response.body().string());
	     }
			
	}
 }
